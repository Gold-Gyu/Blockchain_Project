# Deploy BlockScout  

## Links  

[BlockScout - Github](https://github.com/blockscout/blockscout)  

## Deploy with Private Geth Network  

앞으로 작업 디렉토리는 BlockScout repository의 최상위 디렉토리로 가정한다.  

### Update Environment Variables  

`./docker-compose/envs/common-blockscout.yml` 파일에서 아래의 값들을 알맞은 값으로 변경한다.  

- `ETHEREUM_JSONRPC_HTTP_URL`, `ETHEREUM_JSONRPC_TRACE_URL` - 비공개 네트워크를 구성하는 Geth 노드의 HTTP RPC 주소로 설정한다.  
- `DATABASE_URL` - 블록체인 익스플로러에서 사용할 DB의 접속 주소. `docker-compose.yml`을 그대로 사용한다면 아래와 같이 설정한다.  
  
  ``` text
  DATABASE_URL=postgresql://postgres:@database:5432/blockscout?ssl=false
  ```

### Start Blockchain Explorer Services  

위의 작업을 마쳤다면, 이젠 일반적인 `docker-compose` 커맨드를 사용하여 서비스를 배포 할 수 있다.  

``` sh
$ docker-compose -f docker-compose/docker-compose.yml up -d
```
