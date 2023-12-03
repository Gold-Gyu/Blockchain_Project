#!/bin/bash

MAIN_DIR="$(realpath "$(dirname "$0")"/..)"
IPFS_DIR="$MAIN_DIR/ipfs"
# KUBO_IMAGE="ipfs/kubo:v0.22.0"

if [[ $* == *--stop* ]]; then
    docker-compose -f "$MAIN_DIR/docker-compose/services/docker-compose-kubo.yaml" down
    exit $?
fi

if ! test -f "$IPFS_DIR/swarm.key"; then
    mkdir -p "$IPFS_DIR"
    echo "/key/swarm/psk/1.0.0/" >"$IPFS_DIR/swarm.key"
    echo "/base16/" >>"$IPFS_DIR/swarm.key"
    tr -dc 'a-f0-9' </dev/urandom | head -c64 >>"$IPFS_DIR/swarm.key"
fi

# docker run -d --name eticket_ipfs \
#     -e LIBP2P_FORCE_PNET=1 \
#     -e IPFS_SWARM_KEY_FILE=/swarm.key \
#     -v "$IPFS_DIR/swarm.key:/data/ipfs/swarm.key" \
#     -v "$IPFS_DIR/ipfs_data:/data/ipfs" \
#     -v "$IPFS_DIR/ipfs_staging:/export" \
#     -p 60880:8080 -p 60881:4001 -p 4001:4001/udp -p 127.0.0.1:60882:5001 \
#     $KUBO_IMAGE

docker-compose -f "$MAIN_DIR/docker-compose/services/docker-compose-kubo.yaml" up -d
