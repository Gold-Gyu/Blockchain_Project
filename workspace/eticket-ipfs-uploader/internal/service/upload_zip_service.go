package service

import (
	"archive/zip"
	"bytes"
	"context"
	"io"
	"path"

	kuborpc "eticket.org/blockchain-ipfs-uploader/internal/kubo/api/v0"
	"eticket.org/blockchain-ipfs-uploader/internal/kubo/common"
)

type UploadZipService struct {
	kuborpc *kuborpc.ApiV0
}

func (s *UploadZipService) appendAll(dm *common.DirectoryMultipart, zipReader *zip.Reader) error {
	for _, file := range zipReader.File {
		if fileInfo := file.FileInfo(); !fileInfo.IsDir() {
			fileContentReader, err := file.Open()
			if err != nil {
				return err
			}

			defer fileContentReader.Close()
			fileContent, err := io.ReadAll(fileContentReader)
			if err != nil {
				return err
			}

			filePath := path.Join(dm.Directory(), fileInfo.Name())
			if err := dm.AppendFile(filePath, fileContent); err != nil {
				return err
			}
		}
	}

	return nil
}

func (s *UploadZipService) Upload(ctx context.Context, dirname string, zipReader *zip.Reader) ([]map[string]any, error) {
	buf := new(bytes.Buffer)

	if dm, err := common.NewDirectoryMultipart(dirname, buf); err != nil {
		return nil, err
	} else {
		if err := s.appendAll(dm, zipReader); err != nil {
			return nil, err
		}
		if err := dm.Close(); err != nil {
			return nil, err
		}

		return s.kuborpc.AddDirectory(ctx, buf, dm.Boundary())
	}
}

func NewUploadZipService(kuborpc *kuborpc.ApiV0) *UploadZipService {
	return &UploadZipService{kuborpc}
}
