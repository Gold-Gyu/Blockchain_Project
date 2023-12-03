#!/bin/sh

require_command() {
    if test -z "$(command -v "$1")"; then
        echo "missing required command \"jq\"."
        exit 1
    fi
}

require_command sqlc

sqlc generate
