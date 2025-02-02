#!/bin/sh

SCRIPT_PATH="$(realpath "$0")";
TOOLS_PATH="$(dirname "$SCRIPT_PATH")";
ROOT_PATH="$(dirname "$TOOLS_PATH")";



if [ ! -d "$ROOT_PATH/target" ]; then
    echo "Missing target folder.";
    exit 1;
fi



echo "===================================================[ Cleanup ]==================================================="
if [ -d "$ROOT_PATH/dist" ]; then
    rm -rf "$ROOT_PATH/dist";
fi



echo "===============================================[ Copying to dist ]==============================================="
mkdir "$ROOT_PATH/dist";
cp -R "$ROOT_PATH/target/lib" "$ROOT_PATH/dist/lib";
cp -R "$ROOT_PATH"/target/classes/* "$ROOT_PATH/dist";



echo "=========================================[ Downloading Zulu OpenJDK 17 ]========================================="
JRE_VER="zulu17.30.15-ca-jre17.0.1"
cd "$ROOT_PATH/dist" && curl -O "https://cdn.azul.com/zulu/bin/$JRE_VER-linux_i686.tar.gz";
cd "$ROOT_PATH/dist" && curl -O "https://cdn.azul.com/zulu/bin/$JRE_VER-linux_x64.tar.gz";

cd "$ROOT_PATH/dist" && tar -xzvf "$JRE_VER-linux_i686.tar.gz" && mv "$JRE_VER-linux_i686" "jre" && rm -rf "$JRE_VER-linux_i686.tar.gz";
cd "$ROOT_PATH/dist" && tar -xzvf "$JRE_VER-linux_x64.tar.gz" && mv "$JRE_VER-linux_x64" "jre64" && rm -rf "$JRE_VER-linux_x64.tar.gz";



echo "======================================[ Copying and Configure Extra Files ]======================================"
cp "$TOOLS_PATH/start.sh" "$ROOT_PATH/dist";
cp "$ROOT_PATH/.env.example" "$ROOT_PATH/dist/config.ini";
sed -i 's/<root level="DEBUG">/<root level="INFO">/' "$ROOT_PATH/dist/logback.xml"



echo "================================================[ Build Success ]================================================"
cd "$ROOT_PATH" && exit 0;
