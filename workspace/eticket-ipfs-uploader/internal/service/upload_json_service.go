package service

import (
	"bytes"
	"context"
	"encoding/json"
	"path"
	"strings"

	kuborpc "eticket.org/blockchain-ipfs-uploader/internal/kubo/api/v0"
	"eticket.org/blockchain-ipfs-uploader/internal/kubo/common"
)

type UploadJsonService struct {
	kuborpc *kuborpc.ApiV0
}

func (s *UploadJsonService) appendAll(dm *common.DirectoryMultipart, jsons map[string]map[string]any) error {
	jsonBuf := make([]byte, 0, 1<<12)

	for jsonName, jsoncontent := range jsons {
		jsonBuf = jsonBuf[:0]

		if !strings.HasSuffix(jsonName, ".json") {
			continue
		}

		data, err := json.Marshal(jsoncontent)
		if err != nil {
			return err
		}

		dm.AppendFile(path.Join(dm.Directory(), jsonName), data)
	}

	return nil
}

func (s *UploadJsonService) Upload(ctx context.Context, dirname string, jsons map[string]map[string]any) ([]map[string]any, error) {
	buf := new(bytes.Buffer)

	dm, err := common.NewDirectoryMultipart(dirname, buf)
	if err != nil {
		return nil, err
	}
	if err := s.appendAll(dm, jsons); err != nil {
		return nil, err
	}
	if err := dm.Close(); err != nil {
		return nil, err
	}

	return s.kuborpc.AddDirectory(ctx, buf, dm.Boundary())
}

func NewUploadJsonService(kuborpc *kuborpc.ApiV0) *UploadJsonService {
	return &UploadJsonService{kuborpc}
}
