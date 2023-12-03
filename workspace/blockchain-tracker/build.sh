#!/bin/sh

GOLANG_IMAGE=golang:1.21.1
PROJECT_DIR="$(realpath "$(dirname "$0")")"

if test -d "$PROJECT_DIR/Dockerfile/data"; then
    rm -rf "$PROJECT_DIR/Dockerfile/data/"
fi

mkdir -p "$PROJECT_DIR/.gocache/gomod"
mkdir -p "$PROJECT_DIR/.gocache/gobuild"

docker run --rm -it \
    -v "$PROJECT_DIR:/workspace" \
    -e "GOCACHE=/workspace/.gocache/gomod" \
    -e "GOMODCACHE=/workspace/.gocache/gobuild" \
    $GOLANG_IMAGE sh -c "cd /workspace && go build -o ./Dockerfile/data/app . && chmod go+rw -R ./Dockerfile/data"

if ! test $? -eq 0; then
    exit 1
fi
