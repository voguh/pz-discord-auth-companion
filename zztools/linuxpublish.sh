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

cd "$ROOT_PATH/dist" && curl -O "https://cdn.azul.com/zulu/bin/$JRE_VER-linux_i686.tar.gz";
jre_checksum_expected="b061befb2d75729afb4d740e7d2b84eaf2016ac19bca5d624e75886c4d94eb32";
jre_checksum_calculated="$(sha256sum "$ROOT_PATH/dist/$JRE_VER-linux_i686.tar.gz" | awk '{print $1}')";
if [ "$jre_checksum_calculated" != "$jre_checksum_expected" ]; then
    echo "SHA256 checksum of jre didn't match!"
    exit 1;
else
    cd "$ROOT_PATH/dist" && tar -xzvf "$JRE_VER-linux_i686.tar.gz" && mv "$JRE_VER-linux_i686" "jre" && rm -rf "$JRE_VER-linux_i686.tar.gz";
fi

cd "$ROOT_PATH/dist" && curl -O "https://cdn.azul.com/zulu/bin/$JRE_VER-linux_x64.tar.gz";
jre64_checksum_expected="1fc247d0482ff5b0a9735bcc4cf617297ae4d606f84fdbcd09774dcf2bbb2fd7"
jre64_checksum_calculated="$(sha256sum "$ROOT_PATH/dist/$JRE_VER-linux_x64.tar.gz" | awk '{print $1}')";
if [ "$jre64_checksum_calculated" != "$jre64_checksum_expected" ]; then
    echo "SHA256 checksum of jre64 didn't match!"
    exit 1;
else
    cd "$ROOT_PATH/dist" && tar -xzvf "$JRE_VER-linux_x64.tar.gz" && mv "$JRE_VER-linux_x64" "jre64" && rm -rf "$JRE_VER-linux_x64.tar.gz";
fi

echo "=============================================[ Copying linux start ]============================================="
cp "$TOOLS_PATH/start.sh" "$ROOT_PATH/dist";

echo "================================================[ Build Success ]================================================"
cd "$ROOT_PATH" && exit 0;
