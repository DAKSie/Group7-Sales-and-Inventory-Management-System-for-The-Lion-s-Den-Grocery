@echo off
chcp 65001 >nul
echo Compiling project...
echo.

setlocal enabledelayedexpansion
set CLASSPATH=bin;lib\mysql-connector-j-9.5.0.jar

if not exist "bin" mkdir bin

REM === Collect all Java source files recursively ===
set FILES=
for /R src %%f in (*.java) do (
    set FILES=!FILES! "%%f"
)

REM === Compile all Java files ===
javac -d bin -cp "%CLASSPATH%" !FILES!

if %errorlevel% neq 0 (
    echo.
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo Running application...
echo.

REM === Change this depending on your package structure ===
REM If Main.java says 'package View;', use:
java -cp "%CLASSPATH%" Main

REM If Main.java has no package, use:
REM java -cp "%CLASSPATH%" Main

endlocal
