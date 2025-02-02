#!/bin/sh

SCRIPT_PATH="$(realpath "$0")";
TOOLS_PATH="$(dirname "$SCRIPT_PATH")";
ROOT_PATH="$(dirname "$TOOLS_PATH")";



echo "===================================================[ Cleanup ]==================================================="
if [ ! -d "$ROOT_PATH/dist" ]; then
    echo "Missing dist folder.";
fi

echo "============================================[ Checking for jre dirs ]============================================"
if [ -d "$ROOT_PATH/dist/jre" ]; then
    echo "Removing existing dist/jre folder..."
    rm -rf "$ROOT_PATH/dist/jre";
fi

if [ -d "$ROOT_PATH/dist/jre64" ]; then
    echo "Removing existing dist/jre64 folder..."
    rm -rf "$ROOT_PATH/dist/jre64";
fi

echo "=========================================[ Downloading Zulu OpenJDK 17 ]========================================="
JRE_VER="zulu17.30.15-ca-jre17.0.1"
cd "$ROOT_PATH/dist" && curl -O "https://cdn.azul.com/zulu/bin/$JRE_VER-win_i686.zip";
cd "$ROOT_PATH/dist" && curl -O "https://cdn.azul.com/zulu/bin/$JRE_VER-win_x64.zip";

cd "$ROOT_PATH/dist" && unzip "$JRE_VER-win_i686.zip" && mv "$JRE_VER-win_i686" "jre" && rm -rf "$JRE_VER-win_i686.zip";
cd "$ROOT_PATH/dist" && unzip "$JRE_VER-win_x64.zip" && mv "$JRE_VER-win_x64" "jre64" && rm -rf "$JRE_VER-win_x64.zip";

echo "============================================[ Copying windows start ]============================================"
cp "$TOOLS_PATH/start.bat" "$ROOT_PATH/dist";

echo "================================================[ Build Success ]================================================"
cd "$ROOT_PATH" && exit 0;
