package apiV0

import (
	"context"
	"log"
	"net/http"

	"eticket.org/blockchain-ipfs-uploader/internal/service"
	"github.com/gin-gonic/gin"
)

type uploadJsonRequest struct {
	Dirname string                    `json:"dirname" binding:"required"`
	Jsons   map[string]map[string]any `json:"jsons" binding:"required"`
}

func RegisterUploadJsonApi(e *gin.Engine, uploadJsonService *service.UploadJsonService) {
	e.POST("/api/v0/upload/json", func(ctx *gin.Context) {
		var payload uploadJsonRequest
		if err := ctx.ShouldBindJSON(&payload); err != nil {
			ctx.AbortWithStatusJSON(http.StatusBadRequest, map[string]any{
				"message": "Bad request body",
			})
			return
		}

		uploads, err := uploadJsonService.Upload(context.Background(), payload.Dirname, payload.Jsons)
		if err != nil {
			log.Fatalln(err)
			ctx.AbortWithStatusJSON(http.StatusInternalServerError, map[string]any{
				"message": "Server encoutered a problem while processing request. Pleases try again later.",
			})
			return
		}

		var dirUpload map[string]any
		items := make([]map[string]any, 0, len(uploads)-1)

		for _, upload := range uploads {
			if upload["Name"].(string) == payload.Dirname {
				dirUpload = upload
			} else {
				upload["Name"] = upload["Name"].(string)[len(payload.Dirname)+1:]
				items = append(items, upload)
			}
		}

		dirUpload["Items"] = items
		ctx.JSON(http.StatusCreated, dirUpload)
	})
}
