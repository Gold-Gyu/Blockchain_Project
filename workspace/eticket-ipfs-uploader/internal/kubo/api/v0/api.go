package kuborpc

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"io"
	"net/http"

	"eticket.org/blockchain-ipfs-uploader/internal/kubo/core"
)

type ApiV0 struct {
	client core.KuboRpcClient
}

func (rpc ApiV0) add(ctx context.Context, payload io.Reader, boundary string) ([]byte, error) {
	req, err := http.NewRequestWithContext(ctx, "POST", rpc.client.KuboRpcServerUrl+"/api/v0/add", payload)
	if err != nil {
		return nil, err
	}

	req.Header.Set("Content-Type", "multipart/form-data; boundary="+boundary)
	defer req.Body.Close()

	resp, err := rpc.client.Do(req)
	if err != nil {
		return nil, err
	}

	defer resp.Body.Close()
	return io.ReadAll(resp.Body)
}

func (rpc ApiV0) Add(ctx context.Context, payload io.Reader, boundary string) (map[string]any, error) {
	resp, err := rpc.add(ctx, payload, boundary)
	if err != nil {
		return nil, fmt.Errorf("KuboRpcApiV0.AddDirectory(): %w", err)
	}

	uploadInfo := map[string]any{}
	json.Unmarshal(resp, &uploadInfo)
	return uploadInfo, nil
}

func (rpc ApiV0) AddDirectory(ctx context.Context, payload io.Reader, boundary string) ([]map[string]any, error) {
	resp, err := rpc.add(ctx, payload, boundary)
	if err != nil {
		return nil, fmt.Errorf("KuboRpcApiV0.AddDirectory(): %w", err)
	}

	uploads := []map[string]any{}
	for _, bytesLine := range bytes.Split(resp, []byte{'\n'}) {
		if 0 < len(bytesLine) {
			uploadInfo := map[string]any{}
			json.Unmarshal(bytesLine, &uploadInfo)
			uploads = append(uploads, uploadInfo)
		}
	}

	return uploads, nil
}

func NewApiV0(client *http.Client, kuboRpcServerUrl string) *ApiV0 {
	return &ApiV0{core.KuboRpcClient{Client: client, KuboRpcServerUrl: kuboRpcServerUrl}}
}
