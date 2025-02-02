#!/bin/sh

SCRIPT_PATH="$(realpath "$0")";
ROOT_PATH="$(dirname "$SCRIPT_PATH")";

JVM_ARGS="-Xms32m -Xmx128m";
JVM_CLASSPATH="${ROOT_PATH}";
for file in "${ROOT_PATH}/lib"/*; do
  if [ -f "$file" ]; then
    JVM_CLASSPATH="$JVM_CLASSPATH:$file";
  fi
done



if [ "$(uname -m)" = "x86_64" ]; then
    echo "Running over Zulu OpenJDK 17 x64"
    export LD_LIBRARY_PATH="${ROOT_PATH}/jre64/lib:${LD_LIBRARY_PATH}";
    "${ROOT_PATH}/jre64/bin/java" -classpath "$JVM_CLASSPATH" $JVM_ARGS "me.voguh.zomboid.authcompanion.Main" "$@";
else
    echo "Running over Zulu OpenJDK 17 x32"
    export LD_LIBRARY_PATH="${ROOT_PATH}/jre/lib:${LD_LIBRARY_PATH}";
    "${ROOT_PATH}/jre/bin/java" -classpath "$JVM_CLASSPATH" $JVM_ARGS "me.voguh.zomboid.authcompanion.Main" "$@";
fi
