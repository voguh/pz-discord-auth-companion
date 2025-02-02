#!/bin/sh

SCRIPT_PATH="$(realpath "$0")";
ROOT_PATH="$(dirname "$SCRIPT_PATH")";

JVM_CLASSPATH=":$(find "$ROOT_PATH/lib" -type f -print | paste -sd: -):$ROOT_PATH";
JVM_ARGS="-Xms32m -Xmx128m";

if [ ! -f "$ROOT_PATH/config.ini" ]; then
    while IFS= read -r line; do
        line=$(echo "$line" | sed 's/#.*//' | xargs)

        if [ -n "$line" ]; then
        JVM_ARGS="$JVM_ARGS -D$line";
        fi
    done < "$ROOT_PATH/config.ini"
fi



if [ "$(uname -m)" = "x86_64" ]; then
    echo "Running over Zulu OpenJDK 17 x64"
    export LD_LIBRARY_PATH="$ROOT_PATH:$ROOT_PATH/lib:$ROOT_PATH/jre64/lib:${LD_LIBRARY_PATH}";
    "$ROOT_PATH/jre64/bin/java" -classpath "$JVM_CLASSPATH" $JVM_ARGS "me/voguh/zomboid/authcompanion/Main" "$@";
else
    echo "Running over Zulu OpenJDK 17 x32"
    export LD_LIBRARY_PATH="$ROOT_PATH:$ROOT_PATH/lib:$ROOT_PATH/jre/lib:${LD_LIBRARY_PATH}";
    "$ROOT_PATH/jre/bin/java" -classpath "$JVM_CLASSPATH" $JVM_ARGS "me/voguh/zomboid/authcompanion/Main" "$@";
fi
