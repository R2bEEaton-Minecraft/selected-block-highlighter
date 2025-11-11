#!/bin/bash

echo "Building Selected Block Highlighter for all Minecraft versions..."
echo ""

echo "Building for Minecraft 1.20.1..."
./gradlew build -Pstonecutter.active=1.20.1
if [ $? -ne 0 ]; then
    echo "Failed to build for 1.20.1"
    exit 1
fi
echo ""

echo "Building for Minecraft 1.21..."
./gradlew build -Pstonecutter.active=1.21
if [ $? -ne 0 ]; then
    echo "Failed to build for 1.21"
    exit 1
fi
echo ""

echo "Building for Minecraft 1.21.1..."
./gradlew build -Pstonecutter.active=1.21.1
if [ $? -ne 0 ]; then
    echo "Failed to build for 1.21.1"
    exit 1
fi
echo ""

echo "========================================"
echo "All versions built successfully!"
echo "Check build/libs/ for the JAR files"
echo "========================================"
