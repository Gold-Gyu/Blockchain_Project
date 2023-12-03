#!/bin/sh

GRADLE_IMAGE=gradle:8.3.0-jdk17-jammy
PROJECT_DIR="$(realpath "$(dirname "$0")")"

docker run --rm -it \
    -v "$PROJECT_DIR:/workspace" \
    $GRADLE_IMAGE sh -c "cd /workspace && gradle build --build-cache -g .gradle.cache -x test"

if ! test $? -eq 0; then
    echo failed to build java project.
    exit 1
fi

# remove a previous build result
rm -f "$PROJECT_DIR/Dockerfile/data/app.jar"

mkdir -p "$PROJECT_DIR/Dockerfile/data"
find "$PROJECT_DIR/build/" -iregex ".+-SNAPSHOT[.]jar$" -print0 | xargs -I{} cp {} "$PROJECT_DIR/Dockerfile/data/app.jar" 1>/dev/null 2>/dev/null

# check whether .jar was successfully copied
if ! test -f "$PROJECT_DIR/Dockerfile/data/app.jar"; then
    echo failed to find build result. something had be gone wrong. sorry.
    exit 1
fi
