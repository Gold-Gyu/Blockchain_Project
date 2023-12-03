# ETICKET Porting Manual 📔

1. [실행 환경 설정](#실행-환경-설정)  
   1.1. [이더리움 비공개 네트워크 구성하기](#이더리움-비공개-네트워크-구성하기)  
   1.2. [IPFS 비공개 네트워크 구성하기](#ipfs-비공개-네트워크-구성하기)
2. [필수 환경 변수 설정](#필수-환경-변수-설정)  
   2.1. [`docker-compose/env/eticket-dev-blockchain.env`](#docker-composeenveticket-dev-blockchainenv)  
   2.2. [`docker-compose/env/eticket-dev-datasource.env`](#docker-composeenveticket-dev-datasourceenv)  
   2.3. [`docker-compose/services/docker-compose-eticket-ipfs-uploader.yaml`](#docker-composeservicesdocker-compose-eticket-ipfs-uploaderyaml)
3. [실행](#실행)

## 실행 환경 설정

프로젝트 실행을 위해 이더리움 계정과 IPFS 구현체 중 하나인 Kubo의 RPC 서버가 필요하다. 프로젝트 내에 작성된 스크립트를 사용하면 각각에 대하여 비공개 네트워크를 구성 할 수 있다.

### 이더리움 비공개 네트워크 구성하기

`scripts/init-blockchain.sh`를 실행하여 이더리움 비공개 네트워크 구성을 위한 준비 작업을 할 수 있다. 그 다음 차례로 `scripts/run-bootnode.sh`, `scripts/run-node.sh`를 실행하면 단일 노드로 구성된 비공개 이더리움 네트워크가 구성된다. 스크립트에 의해 실행되는 geth의 이더리움 RPC 서버 포트는 `60103`이다.

### IPFS 비공개 네트워크 구성하기

`scripts/run-ipfs.sh`를 실행하여 IPFS 비공개 네트워크를 구성할 수 있다. 스크립트에 의해 실행되는 kubo의 RPC 서버 포트는 `608882`이다.

## 필수 환경 변수 설정

`docker-compose/env/` 디렉토리는 서비스 실행에 필요한 필수 환경 변수들을 정의한다. 실행 환경에 따라 해당 환경 변수 설정 파일의 값을 알맞은 값으로 설정해주어야 한다.

### `docker-compose/env/eticket-dev-blockchain.env`

- `ETICKET_BLOCKCHAIN_RPC_URL` - 이더리움 RPC 서버의 주소
- `ETICKET_BLOCKCHAIN_CHAIN_ID` - 사용할 이더리움 호환 블록체인의 chain ID
- `ETICKET_BLOCKCHAIN_PRIVATE_KEY` - 서비스에서 사용할 이더리움 계정의 private key
- `ETICKET_BLOCKCHAIN_CONTRACT_ADDRESS` - 배포된 에티켓 스마트 컨트랙트의 주소

### `docker-compose/env/eticket-dev-datasource.env`

- `ETICKET_DATASOURCE_URL`, `SPRING_DATASOURCE_URL` - 서비스에서 사용할 MySQL 서버의 주소
- `ETICKET_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_USERNAME` - 서비스에서 사용할 MySQL 계정의 계정명
- `ETICKET_DATASOURCE_PASSWORD`, `SPRING_DATASOURCE_PASSWORD` - 서비스에서 사용할 MySQL 게정의 비밀번호
- `ETICKET.REDIS.(.+).HOST` - 서비스에서 사용할 redis 서버의 주소
- `ETICKET.REDIS.(.+).PORT` - 서비스에서 사용할 redis 서버의 포트
- `ETICKET.REDIS.(.+).DATABASE` - 서비스에서 사용할 redis 데이터베이스

### `docker-compose/services/docker-compose-eticket-ipfs-uploader.yaml`

- `ETICKET_KUBO_RPC_URL` - 서비스에서 사용할 IPFS RPC 서버 주소

## 실행

`scripts/run.sh --bundle` 커맨드를 입력하여 서비스를 구동할 수 있다. 커맨드를 사용하기 전에 앞서 설명한 모든 필수 환경 변수를 알맞게 설정하여야 한다.

## 관련 문서

- [블록체인 익스플로러 배포하기](../docs/about-deploying-blockchain-explorer.md)
