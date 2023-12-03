#!/bin/bash

NODE_IMAGE="jitesoft/node-yarn:18"
MAIN_DIR="$(realpath "$(dirname "$0")"/..)"

if [ ! -d "$MAIN_DIR/workspace/frontend" ]; then
    echo "missing \"$MAIN_DIR/workspace/frontend\"."
    exit 1
fi

docker run --rm -it \
    --user "${UID}:${GID}" \
    --volume "$MAIN_DIR/workspace/frontend:/workspace" \
    $NODE_IMAGE sh -c "cd /workspace && yarn && yarn build"

if test $? -ne 0; then
    exit 1
fi

rm -rf "$MAIN_DIR/workspace/backend/src/main/resources/static"
cp -R "$MAIN_DIR/workspace/frontend/dist" "$MAIN_DIR/workspace/backend/src/main/resources/static"

if test $? -ne 0; then
    echo "failed to copy dists/"
    exit 1
fi
