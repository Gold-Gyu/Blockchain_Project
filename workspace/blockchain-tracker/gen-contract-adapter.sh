#!/bin/bash

NODE_IMAGE=node:18
GOETH_IMAGE=ethereum/client-go:alltools-v1.13.1

if ! test -f ../blockchain-contract/contracts/Eticket.sol; then
    echoi "missing contract ../blockchain-contract/contracts/Eticket.sol"
    exit 1
fi

docker run --rm -it \
    -v "$(realpath ..):/workspace" \
    $NODE_IMAGE sh -c "cd /workspace/blockchain-contract && npm install && npx hardhat compile"
if [[ $? != 0 ]]; then
    exit 1
fi

rm -rf contract
mkdir -p contract/generated
jq -r .abi <../blockchain-contract/artifacts/contracts/Eticket.sol/Eticket.json >contract/Eticket.sol.abi
jq -r .bytecode <../blockchain-contract/artifacts/contracts/Eticket.sol/Eticket.json >contract/Eticket.sol.bin
docker run --rm -it \
    -v ".:/workspace" \
    $GOETH_IMAGE abigen \
    --abi /workspace/contract/Eticket.sol.abi \
    --bin /workspace/contract/Eticket.sol.bin \
    --pkg contract --out /workspace/contract/generated/eticket.go
