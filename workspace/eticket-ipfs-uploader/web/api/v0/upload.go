package apiV0

import (
	"context"
	"io"
	"net/http"

	"eticket.org/blockchain-ipfs-uploader/internal/service"
	"github.com/gin-gonic/gin"
)

func RegisterUploadApi(e *gin.Engine, s *service.UploadService) {
	e.POST("/api/v0/upload", func(ctx *gin.Context) {
		fileHeader, err := ctx.FormFile("file")
		if err != nil {
			ctx.AbortWithStatusJSON(http.StatusBadRequest, map[string]any{
				"message": "Missing multipart content.",
			})
			return
		}

		fileContentReader, err := fileHeader.Open()
		if err != nil {
			ctx.AbortWithStatusJSON(http.StatusInternalServerError, map[string]any{
				"message": "Unexpected error has occurred when reading a request body.",
			})
			return
		}

		defer fileContentReader.Close()
		fileContent, err := io.ReadAll(fileContentReader)
		if err != nil {
			ctx.AbortWithStatusJSON(http.StatusInternalServerError, map[string]any{
				"message": "Unexpected error has occurred when reading a request body.",
			})
		}

		uploadInfo, err := s.Upload(context.Background(), fileHeader.Filename, fileContent)
		if err != nil {
			ctx.AbortWithStatusJSON(http.StatusInternalServerError, map[string]any{
				"message": "Unexpected error occurred during processing request.",
			})
			return
		}

		ctx.JSON(http.StatusCreated, uploadInfo)
	})
}
