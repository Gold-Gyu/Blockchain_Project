package common

import (
	"io"
	"mime/multipart"
	"net/textproto"
)

type DirectoryMultipart struct {
	dirname string
	writer  *multipart.Writer
}

func (dm *DirectoryMultipart) Boundary() string {
	return dm.writer.Boundary()
}

func (dm *DirectoryMultipart) Directory() string {
	return dm.dirname
}

func (dm *DirectoryMultipart) AppendFile(filepath string, filecontent []byte) error {
	body, err := dm.writer.CreatePart(textproto.MIMEHeader{
		"Abspath":             {filepath},
		"Content-Disposition": {"form-data; name=\"file\"; filename=\"" + filepath + "\""},
		"Content-Type":        {"application/octet-stream"},
	})
	if err != nil {
		return err
	}

	_, err = body.Write(filecontent)
	return err
}

func (dm *DirectoryMultipart) Close() error {
	return dm.writer.Close()
}

func NewDirectoryMultipart(dirname string, buf io.Writer) (*DirectoryMultipart, error) {
	writer := multipart.NewWriter(buf)
	if _, err := writer.CreatePart(textproto.MIMEHeader{
		"Content-Disposition": {"form-data; name=\"file\"; filename=\"" + dirname + "\""},
		"Content-Type":        {"application/x-directory"},
	}); err == nil {
		return &DirectoryMultipart{dirname, writer}, nil
	} else {
		return nil, err
	}
}
