@echo off
chcp 65001 >nul
echo Compiling with debug info...
echo.

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin

REM Compile step by step with verbose output
echo Step 1: Compiling Utils...
javac -d bin -verbose src/View/Utils/*.java
if %errorlevel% neq 0 goto error

echo Step 2: Compiling Components...
javac -d bin -cp "bin" -verbose src/View/Components/*.java
if %errorlevel% neq 0 goto error

echo Step 3: Compiling Views...
javac -d bin -cp "bin" -verbose src/View/*.java
if %errorlevel% neq 0 goto error

echo Step 4: Compiling main classes...
javac -d bin -cp "bin" -verbose src/*.java
if %errorlevel% neq 0 goto error

echo.
echo All files compiled successfully!
echo.
echo Running App...
java -cp "bin" Main
exit /b 0

:error
echo.
echo Compilation failed at step above.
exit /b 1
