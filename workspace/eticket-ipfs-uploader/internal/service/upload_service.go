package service

import (
	"bytes"
	"context"
	"mime/multipart"

	kuborpc "eticket.org/blockchain-ipfs-uploader/internal/kubo/api/v0"
)

type UploadService struct {
	kuborpc *kuborpc.ApiV0
}

func (s *UploadService) Upload(ctx context.Context, fileName string, fileContent []byte) (map[string]any, error) {
	buf := new(bytes.Buffer)
	writer := multipart.NewWriter(buf)

	body, err := writer.CreateFormFile("file", fileName)
	if err != nil {
		return nil, err
	}

	if _, err := body.Write(fileContent); err != nil {
		return nil, err
	}

	if err := writer.Close(); err != nil {
		return nil, err
	}

	return s.kuborpc.Add(ctx, buf, writer.Boundary())
}

func NewUploadService(kuborpc *kuborpc.ApiV0) *UploadService {
	return &UploadService{kuborpc}
}
