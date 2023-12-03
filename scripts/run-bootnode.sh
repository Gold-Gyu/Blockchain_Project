#!/bin/bash

BOOTNODE_IMAGE=eticket/bootnode:latest
MAIN_DIR="$(realpath "$(dirname "$0")/..")"
BLOCKCHAIN_DIR="$MAIN_DIR/blockchain"
BOOTNODE_IMAGE=eticket/bootnode:latest

docker run --rm -d \
    --user "${UID}:${GID}" \
    --volume "$BLOCKCHAIN_DIR:/workspace" \
    --network host \
    $BOOTNODE_IMAGE bootnode -nodekey /workspace/boot.key -addr :60000
