#!/bin/bash

MAIN_DIR="$(realpath "$(dirname "$0")"/..)"
BLOCKCHAIN_DIR="$MAIN_DIR/blockchain"
GETH_IMAGE=ethereum/client-go:v1.13.0

# shellcheck disable=all
ACCOUNT=$(cat $BLOCKCHAIN_DIR/accounts/node/keystore/* | jq -r ".address")
docker run --rm -d \
    --user "${UID}:${GID}" \
    --volume "$BLOCKCHAIN_DIR:/workspace" --network host \
    $GETH_IMAGE \
    --networkid 55 \
    --bootnodes "enode://$(cat "$BLOCKCHAIN_DIR/bootnode-address")@127.0.0.1:0?discport=60000" \
    --allow-insecure-unlock \
    --datadir "/workspace/accounts/node" \
    --port 60101 --authrpc.port 60102 \
    --http --http.addr "0.0.0.0" --http.port 60103 --http.vhosts=* --http.corsdomain "*" --http.api "admin,eth,debug,miner,net,txpool,personal,web3" \
    --unlock 0 --password /workspace/accounts/passphrase \
    --mine --miner.etherbase "$ACCOUNT" \
    --syncmode full --gcmode archive
