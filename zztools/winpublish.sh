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
jre_checksum_expected="858cbcdbf5a1f77c1947778562e60545d7645057a4fcde2a10d89fbf8a636d90";
jre_checksum_calculated="$(sha256sum "$ROOT_PATH/dist/$JRE_VER-win_i686.zip" | awk '{print $1}')";
if [ "$jre_checksum_calculated" != "$jre_checksum_expected" ]; then
    echo "SHA256 checksum of jre didn't match!"
    exit 1;
else
    cd "$ROOT_PATH/dist" && unzip "$JRE_VER-win_i686.zip" && mv "$JRE_VER-win_i686" "jre" && rm -rf "$JRE_VER-win_i686.zip";
fi

cd "$ROOT_PATH/dist" && curl -O "https://cdn.azul.com/zulu/bin/$JRE_VER-win_x64.zip";
jre64_checksum_expected="e35add53b3279b1954c17cfee2d45a50fbe6473ea0780d68a64e15df59881cd1"
jre64_checksum_calculated="$(sha256sum "$ROOT_PATH/dist/$JRE_VER-win_x64.zip" | awk '{print $1}')";
if [ "$jre64_checksum_calculated" != "$jre64_checksum_expected" ]; then
    echo "SHA256 checksum of jre64 didn't match!"
    exit 1;
else
    cd "$ROOT_PATH/dist" && unzip "$JRE_VER-win_x64.zip" && mv "$JRE_VER-win_x64" "jre64" && rm -rf "$JRE_VER-win_x64.tar.gz";
fi

echo "============================================[ Copying windows start ]============================================"
cp "$TOOLS_PATH/start.bat" "$ROOT_PATH/dist";

echo "================================================[ Build Success ]================================================"
cd "$ROOT_PATH" && exit 0;
