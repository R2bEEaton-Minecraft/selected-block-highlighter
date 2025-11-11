#!/bin/bash

echo "Building Selected Block Highlighter for all Minecraft versions..."
echo ""

echo "Building for Minecraft 1.20.1..."
./gradlew "Set active project to 1.20.1"
if [ $? -ne 0 ]; then
    echo "Failed to set active project to 1.20.1"
    exit 1
fi
./gradlew build
if [ $? -ne 0 ]; then
    echo "Failed to build for 1.20.1"
    exit 1
fi
echo ""

echo "Building for Minecraft 1.21..."
./gradlew "Set active project to 1.21"
if [ $? -ne 0 ]; then
    echo "Failed to set active project to 1.21"
    exit 1
fi
./gradlew build
if [ $? -ne 0 ]; then
    echo "Failed to build for 1.21"
    exit 1
fi
echo ""

echo "Building for Minecraft 1.21.1..."
./gradlew "Set active project to 1.21.1"
if [ $? -ne 0 ]; then
    echo "Failed to set active project to 1.21.1"
    exit 1
fi
./gradlew build
if [ $? -ne 0 ]; then
    echo "Failed to build for 1.21.1"
    exit 1
fi
echo ""

echo "========================================"
echo "All versions built successfully!"
echo ""
echo "JAR files are located in:"
echo "  versions/1.20.1/build/libs/"
echo "  versions/1.21/build/libs/"
echo "  versions/1.21.1/build/libs/"
echo "========================================"
