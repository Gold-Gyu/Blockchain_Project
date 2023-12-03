package main

import (
	"context"
	"database/sql"
	"encoding/json"
	"flag"
	"fmt"
	"os"
	"os/exec"
	"strconv"
	"strings"
	"time"

	"eticket.org/blockchain-minter/internal/minter"
	persistence "eticket.org/blockchain-minter/internal/persistence/generated"
	"github.com/BurntSushi/toml"

	_ "github.com/go-sql-driver/mysql"
)

type Environment struct {
	IPFSGatewayUrl            string `toml:"IPFS_GATEWAY_URL"`
	IPFSUploaderUrl           string `toml:"IPFS_UPLOADER_URL"`
	BlockchainRpcUrl          string `toml:"BLOCKCHAIN_RPC_URL"`
	BlockchainChainId         string `toml:"BLOCKCHAIN_CHAIN_ID"`
	BlockchainPrivateKey      string `toml:"BLOCKCHAIN_PRIVATE_KEY"`
	BlockchainContractAddress string `toml:"BLOCKCHAIN_CONTRACT_ADDRESS"`
	DatasourceUrl             string `toml:"DATASOURCE_URL"`
	DatasourceUsername        string `toml:"DATASOURCE_USERNAME"`
	DatasourcePassword        string `toml:"DATASOURCE_PASSWORD"`
}

func must[T any](v T, err error) T {
	if err == nil {
		return v
	}
	// https://stackoverflow.com/questions/25927660/how-to-get-the-current-function-name
	// pc := make([]uintptr, 15)
	// n := runtime.Callers(2, pc)
	// frames := runtime.CallersFrames(pc[:n])
	// frame, _ := frames.Next()
	panic(err)
}

func loadEnv(path string) (env *Environment) {
	_, err := os.Stat(path)
	if err != nil {
		if os.IsNotExist(err) {
			fmt.Fprintf(os.Stderr, "Given environment file \"%s\" does not exist.", path)
		} else {
			fmt.Fprintf(os.Stderr, "Unexpected error: %s", err.Error())
		}
		os.Exit(1)
	}

	env = new(Environment)
	if _, err := toml.DecodeFile(path, env); err != nil {
		fmt.Fprintf(os.Stderr, "Failed to parse environment file: %s", err.Error())
	}

	return
}

func newMintHelperWithEnv(env *Environment) *minter.MintHelper {
	initOpts := minter.MintHelperInitOpts{
		EthRpcUrl:          env.BlockchainRpcUrl,
		EthChainId:         env.BlockchainChainId,
		EthPrivateKey:      env.BlockchainPrivateKey,
		EthContractAddress: env.BlockchainContractAddress,
	}
	mintHelper, err := minter.NewMintHelper(initOpts)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Failed to create rpc client: %s", err.Error())
		os.Exit(1)
	}
	return mintHelper
}

func newDBClientWithEnv(env *Environment) *sql.DB {
	conn, err := sql.Open("mysql", fmt.Sprintf("%s:%s@tcp(%s)/eticket?parseTime=true",
		env.DatasourceUsername, env.DatasourcePassword, env.DatasourceUrl))
	if err != nil {
		fmt.Fprintf(os.Stderr, "Failed to create database connection: %s", err.Error())
		os.Exit(1)
	}
	return conn
}

func setDefaultContent(env *Environment, performanceScheduleID uint32, defaultContentPath string) {
	mintHelper := newMintHelperWithEnv(env)
	defer mintHelper.Close()

	db := newDBClientWithEnv(env)
	defer db.Close()

	cover := struct {
		Hash string
		Name string
		Size int
	}{}
	cmd := exec.Command("curl", "-X", "POST", "-F", "file=@"+defaultContentPath, env.IPFSUploaderUrl+"/api/v0/upload")
	json.Unmarshal(must(cmd.Output()), &cover)

	queries := persistence.New(db)
	performance := must(queries.PerformanceScheduleDetails(context.Background(), int32(performanceScheduleID)))
	seats := map[string]any{}
	for _, seat := range must(queries.PerformanceSeats(context.Background(), int32(performanceScheduleID))) {
		seats[fmt.Sprintf("%d.json", seat.SeatID)] = map[string]any{
			"name":        performance.Title + " #Eticket",
			"description": performance.Description.String,
			"date":        performance.StartDateTime.Unix(),
			"image":       env.IPFSGatewayUrl + cover.Hash,
			"attributes": []map[string]any{
				{
					"trait_type": "Seat ID",
					"value":      seat.SeatID,
				},
				{
					"trait_type": "Seat class",
					"value":      seat.SeatClass,
				},
			},
		}
	}

	upload := struct {
		Hash  string
		Name  string
		Items []map[string]any
	}{}
	cmd = exec.Command("curl",
		"-X", "POST",
		"-H", "Content-type: application/json; charset=utf-8",
		"-d", string(must(json.Marshal(map[string]any{
			"dirname": fmt.Sprintf("performance_schedule_%d", performanceScheduleID),
			"jsons":   seats,
		}))),
		env.IPFSUploaderUrl+"/api/v0/upload/json")
	if err := json.Unmarshal(must(cmd.Output()), &upload); err != nil {
		fmt.Fprintln(os.Stderr, fmt.Errorf("failed to upload metadatas: %w", err).Error())
		os.Exit(1)
	}

	if !must(mintHelper.IsPerformanceScheduled(context.Background(), performanceScheduleID)) {
		if err := mintHelper.SchedulePerformance(context.Background(), &minter.SchedulePerformanceOpts{
			PerformanceScheduleId:       performanceScheduleID,
			TicketExpirationTime:        performance.StartDateTime.Add(48 * time.Hour),
			TicketDefaultContentBaseUrl: env.BlockchainRpcUrl + upload.Hash + "/",
			TicketSpecialContentBaseUrl: "",
		}); err != nil {
			fmt.Fprintln(os.Stderr, fmt.Errorf("failed to schedule performance: %w", err).Error())
			os.Exit(1)
		}
	} else {
		err := mintHelper.SetTicketDefaultContentBaseUrl(
			context.Background(), performanceScheduleID, env.IPFSGatewayUrl+upload.Hash+"/")
		if err != nil {
			fmt.Fprintln(os.Stderr, fmt.Errorf("failed to update ticket default content base url: %w", err).Error())
			os.Exit(1)
		}
	}

	fmt.Printf("successfully schedule performance:\n  - metadatas: %s%s/\n", env.IPFSGatewayUrl, upload.Hash)
}

func setSpecialContent(env *Environment, performanceScheduleID uint32, defaultContentPath string, specialContentPath string) {
	mintHelper := newMintHelperWithEnv(env)
	defer mintHelper.Close()

	if !must(mintHelper.IsPerformanceScheduled(context.Background(), performanceScheduleID)) {
		fmt.Fprintln(os.Stderr, fmt.Errorf("performance does not scheduled").Error())
		os.Exit(1)
	}

	db := newDBClientWithEnv(env)
	defer db.Close()

	defaultContent := struct {
		Hash string
		Name string
		Size int
	}{}
	cmd := exec.Command("curl", "-X", "POST", "-F", "file=@"+defaultContentPath, env.IPFSUploaderUrl+"/api/v0/upload")
	json.Unmarshal(must(cmd.Output()), &defaultContent)

	specialContents := struct {
		Hash  string
		Name  string
		Items []struct {
			Hash string
			Name string
			Size int
		}
	}{}
	cmd = exec.Command("curl", "-X", "POST", "-F", "file=@"+specialContentPath, env.IPFSUploaderUrl+"/api/v0/upload/zip")
	json.Unmarshal(must(cmd.Output()), &specialContents)

	specialContentFileNames := map[uint32]string{}
	for _, item := range specialContents.Items {
		fileName := item.Name[:strings.LastIndex(item.Name, ".")]
		specialContentFileNames[uint32(must(strconv.ParseUint(fileName, 10, 32)))] = item.Name
	}

	queries := persistence.New(db)
	performance := must(queries.PerformanceScheduleDetails(context.Background(), int32(performanceScheduleID)))
	seats := map[string]any{}
	for _, seat := range must(queries.PerformanceSeats(context.Background(), int32(performanceScheduleID))) {
		var image string
		if name, ok := specialContentFileNames[uint32(seat.SeatID)]; !ok {
			image = env.IPFSGatewayUrl + defaultContent.Hash
		} else {
			image = env.IPFSGatewayUrl + specialContents.Hash + "/" + name
		}

		seats[fmt.Sprintf("%d.json", seat.SeatID)] = map[string]any{
			"name":        performance.Title + " #Eticket",
			"description": performance.Description.String,
			"date":        performance.StartDateTime.Unix(),
			"image":       image,
			"attributes": []map[string]any{
				{
					"trait_type": "Seat ID",
					"value":      seat.SeatID,
				},
				{
					"trait_type": "Seat class",
					"value":      seat.SeatClass,
				},
			},
		}
	}

	upload := struct {
		Hash  string
		Name  string
		Items []map[string]any
	}{}
	cmd = exec.Command("curl",
		"-X", "POST",
		"-H", "Content-type: application/json; charset=utf-8",
		"-d", string(must(json.Marshal(map[string]any{
			"dirname": fmt.Sprintf("performance_schedule_%d", performanceScheduleID),
			"jsons":   seats,
		}))),
		env.IPFSUploaderUrl+"/api/v0/upload/json")
	if err := json.Unmarshal(must(cmd.Output()), &upload); err != nil {
		fmt.Fprintln(os.Stderr, fmt.Errorf("failed to upload metadatas: %w", err).Error())
		os.Exit(1)
	}

	err := mintHelper.SetTicketDefaultContentBaseUrl(
		context.Background(), performanceScheduleID, env.BlockchainRpcUrl+upload.Hash+"/")
	if err != nil {
		fmt.Fprintln(os.Stderr, fmt.Errorf("failed to update ticket special content base url: %w", err).Error())
		os.Exit(1)
	}

	fmt.Println("successfully change special contents:")
	fmt.Printf("- special contents: %s%s/\n", env.IPFSGatewayUrl, specialContents.Hash)
	fmt.Printf("- metadatas: %s%s/\n", env.IPFSGatewayUrl, upload.Hash)
}

func main() {
	envFile := flag.String("env_file", "", "")
	flag.Parse()
	arg0 := flag.Arg(0)

	if len(*envFile) == 0 {
		fmt.Fprintf(os.Stderr, "Missing required command line argument \"--env_file\".")
		os.Exit(1)
	}
	env := loadEnv(*envFile)

	switch arg0 {
	case "default":
		arg1 := flag.Arg(1)
		arg2 := flag.Arg(2)
		perfScheduleId := uint32(must(strconv.ParseUint(arg1, 10, 32)))
		setDefaultContent(env, perfScheduleId, arg2)

	case "special":
		arg1 := flag.Arg(1)
		arg2 := flag.Arg(2)
		arg3 := flag.Arg(3)
		perfScheduleId := uint32(must(strconv.ParseUint(arg1, 10, 32)))
		setSpecialContent(env, perfScheduleId, arg2, arg3)

	default:
		fmt.Fprintf(os.Stdout, "\"%s\"\n", arg0)
	}
}
