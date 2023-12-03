package config

import (
	"fmt"
	"os"
	"strconv"

	"go.uber.org/zap"
)

type ApplicationConfig struct {
	BlockchainRpcUrl string
	ContractAddress  string
	LowerBlock       int64
}

func New(log *zap.Logger) (*ApplicationConfig, error) {
	PREFIX := "ETICKET_BLOCKCHAIN_"

	blockchainRpcUrl := os.Getenv(PREFIX + "RPC_URL")
	if len(blockchainRpcUrl) == 0 {
		return nil, fmt.Errorf("missing environment variable \"%s\"", PREFIX+"RPC_URL")
	}

	contractAddress := os.Getenv(PREFIX + "CONTRACT_ADDRESS")
	if len(contractAddress) == 0 {
		return nil, fmt.Errorf("missing environment variable \"%s\"", PREFIX+"CONTRACT_ADDRESS")
	}

	lowerBlock := int64(-1)
	if v := os.Getenv(PREFIX + "LOWER_BLOCK"); 0 < len(v) {
		var err error
		lowerBlock, err = strconv.ParseInt(v, 10, 64)
		if err != nil {
			return nil, fmt.Errorf("illegal block number: %v\n", v)
		}
	}

	return &ApplicationConfig{
		BlockchainRpcUrl: blockchainRpcUrl,
		ContractAddress:  contractAddress,
		LowerBlock:       lowerBlock,
	}, nil
}
