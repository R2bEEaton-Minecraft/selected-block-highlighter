# Building Selected Block Highlighter

This mod uses **Stonecutter** for multi-version support, allowing a single codebase to build for multiple Minecraft versions.

## Requirements

- **Java 17** or newer (Java 21 recommended for 1.21+)
- Gradle 8.x (included via wrapper)

## Supported Versions

- Minecraft 1.20.1 (requires Java 17+)
- Minecraft 1.21 (requires Java 17+, Java 21 recommended)
- Minecraft 1.21.1 (requires Java 17+, Java 21 recommended)

## Building for a Specific Version

To build for a specific Minecraft version:

```bash
# For 1.20.1
./gradlew build -Pstonecutter.active=1.20.1

# For 1.21
./gradlew build -Pstonecutter.active=1.21

# For 1.21.1 (default)
./gradlew build -Pstonecutter.active=1.21.1
# or simply
./gradlew build
```

The built JAR will be in `build/libs/` with the version in the filename:
- `selected-block-highlighter-1.20.1-1.0.0.jar`
- `selected-block-highlighter-1.21-1.0.0.jar`
- `selected-block-highlighter-1.21.1-1.0.0.jar`

## Building for All Versions

To build for all supported versions at once:

```bash
./gradlew chiseledBuild
```

This will create JARs for all versions in the `build/libs/` directory.

## Running in Development

To test the mod in development:

```bash
# Run client for specific version
./gradlew runClient -Pstonecutter.active=1.21.1

# Run server for specific version
./gradlew runServer -Pstonecutter.active=1.20.1
```

## Version-Specific Code

The mod uses Stonecutter preprocessing comments for version-specific code:

```java
//? if >=1.21 {
/*code for 1.21 and higher*/
//?} else {
code for 1.20.1
//?}
```

## Version-Specific Dependencies

Dependencies are configured per version in:
- `versions/1.20.1/gradle.properties`
- `versions/1.21/gradle.properties`
- `versions/1.21.1/gradle.properties`

## Project Structure

```
selected-block-highlighter/
├── src/                          # Source code (shared across versions)
├── versions/                     # Version-specific configurations
│   ├── 1.20.1/
│   │   └── gradle.properties    # Dependencies for 1.20.1
│   ├── 1.21/
│   │   └── gradle.properties    # Dependencies for 1.21
│   └── 1.21.1/
│       └── gradle.properties    # Dependencies for 1.21.1
├── build.gradle                  # Main build script
├── stonecutter.gradle           # Stonecutter configuration
└── settings.gradle              # Stonecutter plugin setup
```

## Switching Active Version in IDE

To develop for a specific version in your IDE, change the active version in `build.gradle`:

```gradle
stonecutter.active '1.21.1'  // Change this to your desired version
```

Then refresh your Gradle project in your IDE.

## Adding New Versions

To add support for a new Minecraft version:

1. Add the version to `settings.gradle`:
   ```gradle
   versions '1.20.1', '1.21', '1.21.1', '1.21.2'  // Add new version
   ```

2. Create a new directory in `versions/`:
   ```bash
   mkdir versions/1.21.2
   ```

3. Create `versions/1.21.2/gradle.properties` with appropriate dependencies

4. Add version-specific code if needed using Stonecutter comments

5. Build with:
   ```bash
   ./gradlew build -Pstonecutter.active=1.21.2
   ```
