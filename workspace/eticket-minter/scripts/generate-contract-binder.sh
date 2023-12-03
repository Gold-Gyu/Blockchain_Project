#!/bin/bash

require_command() {
    if test -z "$(command -v "$1")"; then
        echo "missing required command \"jq\"."
        exit 1
    fi
}

require_command jq
require_command docker

CONTRACT_WORKSPACE="$(realpath ../blockchain-contract)"

NODE_IMAGE="node:18"
GOETH_IMAGE="ethereum/client-go:alltools-v1.13.1"

if ! test -f "$CONTRACT_WORKSPACE/contracts/Eticket.sol"; then
    echo "missing contract \"$CONTRACT_WORKSPACE/contracts/Eticket.sol\"."
    exit 1
fi

if ! docker run --rm -it -v "$CONTRACT_WORKSPACE:/workspace" $NODE_IMAGE sh -c "cd /workspace && npm install && npx hardhat compile"; then
    echo "failed to compile contract."
    exit 1
fi

if ! mkdir -p internal/contract/generated; then
    echo "failed to create directory \"internal/contract/generated\""
    exit 1
fi

jq -r .abi <"$CONTRACT_WORKSPACE/artifacts/contracts/Eticket.sol/Eticket.json" >internal/contract/generated/Eticket.sol.abi
jq -r .bytecode <"$CONTRACT_WORKSPACE/artifacts/contracts/Eticket.sol/Eticket.json" >internal/contract/generated/Eticket.sol.bin

docker run --rm -it \
    --user "$UID:$GID" \
    --volume .:/workspace \
    $GOETH_IMAGE abigen \
    --abi /workspace/internal/contract/generated/Eticket.sol.abi \
    --bin /workspace/internal/contract/generated/Eticket.sol.bin \
    --pkg contract --out /workspace/internal/contract/generated/contract.go
if test $? -ne 0; then
    echo "failed to create binder."
    exit 1
fi
