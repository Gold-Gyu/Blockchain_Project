#!/bin/sh

print_usage() {
    ARG0=$0
    echo "Usage: ${ARG0##*/} start|stop SERVICE_NAME [--no-build]"
    exit 0
}

CMD=$1
SERVICE_NAME=$2
MAIN_DIR="$(realpath "$(dirname "$0")"/..)"
TARGET_DOCKER_COMPOSE="$MAIN_DIR/docker-compose/services/docker-compose-${SERVICE_NAME}.yaml"

if ! echo "$CMD" | grep -Eq "^(start|stop)$"; then
    print_usage
fi
if [ ${#SERVICE_NAME} = 0 ]; then
    print_usage
fi

if ! [ -f "$TARGET_DOCKER_COMPOSE" ]; then
    echo "docker-compose.yaml of the service \"$SERVICE_NAME\" not found."
    exit 1
fi

if [ "$CMD" = "stop" ]; then
    docker-compose -f "$TARGET_DOCKER_COMPOSE" down
    exit $?
fi

if [ "$CMD" = "start" ]; then
    if ! echo "$*" | grep -q " --no-build"; then
        BUILD_SCRIPT=
        WORKSPACE_DIR="$MAIN_DIR/workspace"
        if [ -f "$WORKSPACE_DIR/$SERVICE_NAME/build.sh" ]; then
            BUILD_SCRIPT="$WORKSPACE_DIR/$SERVICE_NAME/build.sh"
        fi
        if [ -f "$WORKSPACE_DIR/$SERVICE_NAME/scripts/build.sh" ]; then
            BUILD_SCRIPT="$WORKSPACE_DIR/$SERVICE_NAME/scripts/build.sh"
        fi

        if [ ${#BUILD_SCRIPT} != 0 ] && ! sh "$BUILD_SCRIPT"; then
            echo "failed to build the service \"$SERVICE_NAME\"."
            exit 1
        fi
    fi

    docker-compose -f "$TARGET_DOCKER_COMPOSE" up -d --force-recreate --build

    if [ "$(docker images --filter "dangling=true" -q | wc -l)" != 0 ]; then
        # shellcheck disable=SC2046
        docker image rm $(docker images --filter "dangling=true" -q)
    fi
fi
