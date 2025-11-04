@echo off
chcp 65001 >nul
echo Compiling with debug info...
echo.

REM Define reusable classpath (includes bin + MySQL connector)
set CLASSPATH=bin;lib\mysql-connector-j-9.5.0.jar

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin

REM Step 1: Compile Utils
echo Step 1: Compiling Utils...
javac -d bin -cp "%CLASSPATH%" -verbose src/View/Utils/*.java
if %errorlevel% neq 0 goto error

REM Step 2: Compile Components
echo Step 2: Compiling Components...
javac -d bin -cp "%CLASSPATH%" -verbose src/View/Components/*.java
if %errorlevel% neq 0 goto error

REM Step 3: Compile Views
echo Step 3: Compiling Views...
javac -d bin -cp "%CLASSPATH%" -verbose src/View/*.java
if %errorlevel% neq 0 goto error

REM Step 4: Compile Main and other classes
echo Step 4: Compiling main classes...
javac -d bin -cp "%CLASSPATH%" -verbose src/*.java
if %errorlevel% neq 0 goto error

echo.
echo All files compiled successfully!
echo.
echo Running App...
java -cp "%CLASSPATH%" Main
exit /b 0

:error
echo.
echo Compilation failed at step above.
exit /b 1
