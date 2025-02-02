#!/bin/sh

SCRIPT_PATH="$(realpath "$0")";
TOOLS_PATH="$(dirname "$SCRIPT_PATH")";
ROOT_PATH="$(dirname "$TOOLS_PATH")";



echo "===========================================[ Checking for target dir ]==========================================="
if [ ! -d "$ROOT_PATH/target" ]; then
    echo "Missing target folder.";
    exit 1;
fi

echo "============================================[ Checking for dist dir ]============================================"
if [ -d "$ROOT_PATH/dist" ]; then
    echo "Removing existing dist folder..."
    rm -rf "$ROOT_PATH/dist";
fi

echo "============================================[ Copying files to dist ]============================================"
mkdir "$ROOT_PATH/dist";
cp -R "$ROOT_PATH/target/lib" "$ROOT_PATH/dist/lib";
cp -R "$ROOT_PATH"/target/classes/* "$ROOT_PATH/dist";
cp "$ROOT_PATH/.env.example" "$ROOT_PATH/dist/config.ini";
cp "$ROOT_PATH/LICENSE" "$ROOT_PATH/dist/LICENSE";
cp "$ROOT_PATH/README.md" "$ROOT_PATH/dist/README.md";

echo "========================================[ Configuring default log level ]========================================"
sed -i 's/<root level="DEBUG">/<root level="INFO">/' "$ROOT_PATH/dist/logback.xml"

echo "================================================[ Build Success ]================================================"
cd "$ROOT_PATH" && exit 0;
