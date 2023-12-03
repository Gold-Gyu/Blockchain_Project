#!/bin/bash

if [[ $(pwd) =~ /scripts$ ]]; then
    cd ..
fi

MAIN_DIR="$(realpath "$(dirname "$0")"/..)"
WORKSPACE_DIR="$MAIN_DIR/workspace"
DOCKER_COMPOSE_DIR="$MAIN_DIR/docker-compose"

if [[ $* == *--stop* ]]; then
    docker-compose -f "$DOCKER_COMPOSE_DIR/docker-compose.yaml" down
    exit 0
fi

if [[ $* != *--no-build* ]]; then
    if [[ $* == *--bundle* ]]; then
        if ! /bin/bash "$MAIN_DIR/scripts/bundle.sh"; then
            exit 1
        fi
    fi

    BUILD_SCRIPTS="backend-waiting/build.sh blockchain-tracker/build.sh eticket-minter/scripts/build.sh"
    if [[ $* != *--without-backend* ]]; then
        BUILD_SCRIPTS="backend/build.sh $BUILD_SCRIPTS"
    fi

    OIFS=$IFS
    IFS=" "
    for BUILD_SCRIPT in $BUILD_SCRIPTS; do
        if ! test -f "$WORKSPACE_DIR/$BUILD_SCRIPT"; then
            echo "Colunt't find project build script \"$WORKSPACE_DIR/$BUILD_SCRIPT\"."
            exit 1
        fi

        if ! sh "$WORKSPACE_DIR/$BUILD_SCRIPT"; then
            exit 1
        fi
    done

    IFS=$OIFS
    cd "$MAIN_DIR" || exit 1
fi

if [[ "$(docker network ls --format "{{.Name}}" | grep -c --regex "^eticket_net$")" == 0 ]]; then
    if ! docker network create eticket_net --driver bridge 1>/dev/null; then
        echo "Failed to create docker network \"eticket_net\"."
        exit 1
    fi

    echo "A docker network \"eticket_net\" created."
fi

if [[ "$(docker volume ls --format "{{.Name}}" | grep -c --regex "^eticket_main_data$")" == 0 ]]; then
    if ! docker volume create eticket_main_data 1>/dev/null; then
        echo "Failed create docker volume \"eticket_main_data\"."
        exit 1
    fi
fi

TARGET_DOCKER_COMPOSE="$DOCKER_COMPOSE_DIR/docker-compose.yaml"
if [[ $* == *--no-backend* ]]; then
    TARGET_DOCKER_COMPOSE="$DOCKER_COMPOSE_DIR/docker-compose-no-backend.yaml"
fi

REBUILD_TARGETS="eticket_blockchain_tracker eticket_backend_waiting"
if [[ $* != *--no-backend* ]]; then
    REBUILD_TARGETS="$REBUILD_TARGETS eticket_backend"
fi

docker-compose -f "$TARGET_DOCKER_COMPOSE" down

OIFS=$IFS
IFS=" "
for SERVICE in $REBUILD_TARGETS; do
    if ! docker-compose -f "$TARGET_DOCKER_COMPOSE" up -d --force-recreate --no-deps --build "$SERVICE"; then
        echo "Failed to start service \"$SERVICE\"."
        exit 1
    fi
done
IFS=$OIFS

docker-compose -f "$TARGET_DOCKER_COMPOSE" up -d

if [[ $(docker images --filter "dangling=true" -q | wc -l) != 0 ]]; then
    # shellcheck disable=SC2046
    docker image rm $(docker images --filter "dangling=true" -q)
fi
