#!/bin/bash

echo "Compiling project..."
echo

# Set classpath
CLASSPATH="bin:lib/mysql-connector-j-9.5.0.jar"

# Create bin directory if it doesn't exist
mkdir -p bin

# === Collect all Java source files recursively ===
FILES=$(find src -name "*.java")

# === Compile all Java files ===
javac -d bin -cp "$CLASSPATH" $FILES

if [ $? -ne 0 ]; then
    echo
    echo "Compilation failed!"
    exit 1
fi

echo
echo "Compilation successful!"
echo "Running application..."
echo

# === Run the application ===
# If Main.java has no package declaration, use:
java -cp "$CLASSPATH" Main

# If Main.java says 'package View;', use:
# java -cp "$CLASSPATH" View.Main

# If Main.java says 'package src;', use:
# java -cp "$CLASSPATH" src.Main