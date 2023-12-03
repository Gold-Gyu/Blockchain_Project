package core

import "net/http"

type KuboRpcClient struct {
	*http.Client
	KuboRpcServerUrl string
}

func New(client *http.Client, kuboRpcServerUrl string) KuboRpcClient {
	return KuboRpcClient{client, kuboRpcServerUrl}
}
