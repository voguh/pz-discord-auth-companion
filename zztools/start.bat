@echo off
setlocal enabledelayedexpansion

set "ROOT_PATH=%~dp0"
set "ROOT_PATH=%ROOT_PATH:~0,-1%"

set "JVM_ARGS=-Xms32m -Xmx128m"
set "JVM_CLASSPATH=%ROOT_PATH%"
for %%f in (%ROOT_PATH%\lib\*) do (
    if exist "%%f" (
        set "JVM_CLASSPATH=!JVM_CLASSPATH!;%%f"
    )
)

if "%PROCESSOR_ARCHITECTURE%"=="AMD64" (
    echo Running over Zulu OpenJDK 17 x64
    set "LD_LIBRARY_PATH=%ROOT_PATH%\jre64\lib;%LD_LIBRARY_PATH%"
    "%ROOT_PATH%\jre64\bin\java.exe" %JVM_ARGS% -classpath "%JVM_CLASSPATH%" "me.voguh.zomboid.authcompanion.Main" %*
) else (
    echo Running over Zulu OpenJDK 17 x32
    set "LD_LIBRARY_PATH=%ROOT_PATH%\jre\lib;%LD_LIBRARY_PATH%"
    "%ROOT_PATH%\jre\bin\java.exe" %JVM_ARGS% -classpath "%JVM_CLASSPATH%" "me.voguh.zomboid.authcompanion.Main" %*
)

endlocal
