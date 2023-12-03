#!/bin/bash

GETH_IMAGE=ethereum/client-go:v1.13.0
BOOTNODE_IMAGE=eticket/bootnode:latest
MAIN_DIR="$(realpath "$(dirname "$0")/..")"
BLOCKCHAIN_DIR="$MAIN_DIR/blockchain"
CHAIN_ID="12345"

require_command() {
    if ! command -v "$1" 1>/dev/null 2>/dev/null; then
        echo "missing command \"$1\"."
        exit 1
    fi
}

require_bootnode_image() {
    # shellcheck disable=SC2046
    if [ $(docker image ls --format "{{.Repository}}:{{.Tag}}" | grep -c "$BOOTNODE_IMAGE") != 0 ]; then
        return 0
    fi

    mkdir -p "$BLOCKCHAIN_DIR/Dockerfile"
    cat <<EOF >"$BLOCKCHAIN_DIR/Dockerfile/Dockerfile"
FROM ubuntu:22.04

RUN apt-get update
RUN apt-get install -y software-properties-common
RUN add-apt-repository -y ppa:ethereum/ethereum
RUN apt-get update
RUN apt-get install -y bootnode
EOF

    docker build -t "$BOOTNODE_IMAGE" "$BLOCKCHAIN_DIR/Dockerfile"
    if test $? -ne 0; then
        exit 1
    fi
}

generate_genesis() {
    cat <<EOF >"$BLOCKCHAIN_DIR/genesis.json"
{
  "config": {
    "chainId": $1,
    "homesteadBlock": 0,
    "eip150Block": 0,
    "eip155Block": 0,
    "eip158Block": 0,
    "byzantiumBlock": 0,
    "constantinopleBlock": 0,
    "petersburgBlock": 0,
    "istanbulBlock": 0,
    "muirGlacierBlock": 0,
    "berlinBlock": 0,
    "londonBlock": 0,
    "arrowGlacierBlock": 0,
    "grayGlacierBlock": 0,
    "clique": {
      "period": 10,
      "epoch": 30000
    }
  },
  "difficulty": "0x4000",
  "gasLimit": "800000000",
  "extradata": "0x0000000000000000000000000000000000000000000000000000000000000000${2}0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
  "alloc": {
    "$2": { "balance": "50000000000000000000000" }
  }
}
EOF
}

require_command docker
require_command jq
require_bootnode_image

if ! [ -d "$BLOCKCHAIN_DIR/accounts" ]; then
    mkdir -p "$BLOCKCHAIN_DIR/accounts"
    echo "hello_eticket" >"$BLOCKCHAIN_DIR/accounts/passphrase"

    docker run --rm -it \
        --user "${UID}:${GID}" \
        --volume "$BLOCKCHAIN_DIR:/workspace" \
        "$BOOTNODE_IMAGE" /bin/sh -c "bootnode -genkey /workspace/boot.key && bootnode -nodekey /workspace/boot.key -writeaddress >/workspace/bootnode-address"
    if test $? -ne 0; then
        echo "failed to initialize bootnode."
        rm -rf "$BLOCKCHAIN_DIR"
        exit 1
    fi

    docker run --rm -it \
        --user "${UID}:${GID}" \
        --volume "$BLOCKCHAIN_DIR/accounts:/workspace" \
        $GETH_IMAGE --datadir /workspace/node account new --password /workspace/passphrase
    if test $? -ne 0; then
        echo "failed to initialize node."
        rm -rf "$BLOCKCHAIN_DIR"
        exit 1
    fi

    # shellcheck disable=all
    NODE_ACCOUNT="$(cat $BLOCKCHAIN_DIR/accounts/node/keystore/* | jq -r ".address")"
    generate_genesis "$CHAIN_ID" "$NODE_ACCOUNT"

    docker run --rm -it \
        --user "${UID}:${GID}" \
        --volume "$BLOCKCHAIN_DIR:/workspace" \
        $GETH_IMAGE init --datadir "/workspace/accounts/node" /workspace/genesis.json

    if test $? -ne 0; then
        echo "failed to initialize node."
        rm -rf "$BLOCKCHAIN_DIR"
        exit 1
    fi
fi
