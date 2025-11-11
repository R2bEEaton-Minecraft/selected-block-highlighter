@echo off
echo Building Selected Block Highlighter for all Minecraft versions...
echo.

echo Building for Minecraft 1.20.1...
call gradlew "Set active project to 1.20.1"
if errorlevel 1 (
    echo Failed to set active project to 1.20.1
    exit /b 1
)
call gradlew build
if errorlevel 1 (
    echo Failed to build for 1.20.1
    exit /b 1
)
echo.

echo Building for Minecraft 1.21...
call gradlew "Set active project to 1.21"
if errorlevel 1 (
    echo Failed to set active project to 1.21
    exit /b 1
)
call gradlew build
if errorlevel 1 (
    echo Failed to build for 1.21
    exit /b 1
)
echo.

echo Building for Minecraft 1.21.1...
call gradlew "Set active project to 1.21.1"
if errorlevel 1 (
    echo Failed to set active project to 1.21.1
    exit /b 1
)
call gradlew build
if errorlevel 1 (
    echo Failed to build for 1.21.1
    exit /b 1
)
echo.

echo ========================================
echo All versions built successfully!
echo.
echo JAR files are located in:
echo   versions\1.20.1\build\libs\
echo   versions\1.21\build\libs\
echo   versions\1.21.1\build\libs\
echo ========================================
pause
