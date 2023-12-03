package apiV0

import (
	"archive/zip"
	"context"
	"log"
	"mime/multipart"
	"net/http"
	"strconv"
	"strings"

	"eticket.org/blockchain-ipfs-uploader/internal/service"
	"github.com/gin-gonic/gin"
)

func checkContentType(ctx *gin.Context) {
	if contentType := ctx.Request.Header.Get("Content-Type"); !strings.HasPrefix(contentType, "multipart/form-data") {
		ctx.AbortWithStatusJSON(http.StatusBadRequest, map[string]any{
			"message": "Not supported Content-Type: " + contentType,
		})
	}
}

func obtainContentLength(ctx *gin.Context) int64 {
	contentLengthRaw := ctx.Request.Header.Get("Content-Length")
	if len(contentLengthRaw) == 0 {
		ctx.AbortWithStatusJSON(http.StatusBadRequest, map[string]any{
			"message": "Missing requried header: Content-Length",
		})
		return -1
	}

	contentLength, err := strconv.ParseInt(contentLengthRaw, 10, 64)
	if err != nil {
		ctx.AbortWithStatusJSON(http.StatusBadRequest, map[string]any{
			"message": "Illegal header content: Content-Type: " + contentLengthRaw,
		})
		return -1
	}

	return contentLength
}

func obtainMultipartFileHeader(ctx *gin.Context) *multipart.FileHeader {
	file, err := ctx.FormFile("file")
	if err != nil {
		ctx.AbortWithStatusJSON(http.StatusBadRequest, map[string]any{
			"message": "Missing multipart content",
		})
		return nil
	}

	if !strings.HasSuffix(file.Filename, ".zip") {
		ctx.AbortWithStatusJSON(http.StatusBadRequest, map[string]any{
			"message": "Unsupported file type: " + file.Filename,
		})
		return nil
	}

	return file
}

func RegisterUploadZipApi(e *gin.Engine, uploadZipService *service.UploadZipService) {
	e.POST("/api/v0/upload/zip", func(ctx *gin.Context) {
		if checkContentType(ctx); ctx.IsAborted() {
			return
		}

		contentLength := obtainContentLength(ctx)
		if ctx.IsAborted() {
			return
		}

		multipartFileHeader := obtainMultipartFileHeader(ctx)
		if ctx.IsAborted() {
			return
		}

		multipartContent, err := multipartFileHeader.Open()
		if err != nil {
			ctx.AbortWithStatusJSON(http.StatusInternalServerError, map[string]any{
				"message": "Unexpected error has occurred when reading a request body",
			})
			return
		}

		defer multipartContent.Close()

		zipReader, err := zip.NewReader(multipartContent, contentLength)
		if err != nil {
			ctx.AbortWithStatusJSON(http.StatusBadRequest, map[string]any{
				"message": "Broken data",
			})
			return
		}

		dirname := multipartFileHeader.Filename[:len(multipartFileHeader.Filename)-4]
		uploads, err := uploadZipService.Upload(context.Background(), dirname, zipReader)
		if err != nil {
			log.Fatal(err.Error())
			ctx.AbortWithStatusJSON(http.StatusInternalServerError, map[string]any{
				"message": "Unexpected error occurred during processing request",
			})
			return
		}

		var uploadInfo map[string]any
		files := make([]map[string]any, 0, len(uploads)-1)

		for _, upload := range uploads {
			if upload["Name"].(string) == dirname {
				uploadInfo = upload
			} else {
				upload["Name"] = upload["Name"].(string)[len(dirname)+1:]
				files = append(files, upload)
			}
		}

		uploadInfo["Items"] = files
		ctx.JSON(http.StatusCreated, uploadInfo)
	})
}
