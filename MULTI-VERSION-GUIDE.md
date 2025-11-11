# Multi-Version Build Guide

## Quick Start

### Build for a Specific Version

**Important**: You must set the active project BEFORE building!

```bash
# Windows
gradlew "Set active project to 1.20.1"
gradlew build

# Linux/Mac
./gradlew "Set active project to 1.20.1"
./gradlew build
```

### Build All Versions

```bash
# Windows
build-all.bat

# Linux/Mac
./build-all.sh
```

## Output Locations

Built JARs are located in version-specific directories:

- `versions/1.20.1/build/libs/selected-block-highlighter-mc1.20.1-1.0.0.jar`
- `versions/1.21/build/libs/selected-block-highlighter-mc1.21-1.0.0.jar`
- `versions/1.21.1/build/libs/selected-block-highlighter-mc1.21.1-1.0.0.jar`

## How Stonecutter Works

1. **Set Active Project**: Preprocesses source files for that version
   - Modifies files based on `//? if` preprocessor comments
   - Changes persist until you switch to a different version

2. **Build**: Compiles the preprocessed code for that version

3. **Important**: Always run "Set active project" before:
   - Building
   - Running in development
   - Opening in IDE

## Development Workflow

### IDE Setup

1. Set your preferred version:
   ```bash
   ./gradlew "Set active project to 1.21.1"
   ```

2. Refresh Gradle in your IDE

3. Code and test as normal

### Testing Different Versions

```bash
# Switch to 1.20.1
./gradlew "Set active project to 1.20.1"
./gradlew runClient

# Switch to 1.21.1
./gradlew "Set active project to 1.21.1"
./gradlew runClient
```

## Version-Specific Code

Use Stonecutter preprocessor comments for version differences:

```java
//? if >=1.21.1 {
/*buffer.addVertex(matrix, x, y, z).setColor(r, g, b, a);
*///?} else {
buffer.vertex(matrix, x, y, z).color(r, g, b, a).endVertex();
//?}
```

**Syntax**:
- `//? if >=1.21.1 {` - Condition
- `/*code for 1.21.1+*/` - Code for matching versions (commented out when inactive)
- `//?} else {` - Else branch
- `code` - Code for non-matching versions
- `//?}` - End condition

## Common Tasks

### Check Current Active Version

```bash
./gradlew tasks --group stonecutter
```

Look for the "Reset active project" task - it shows the default version.

### Refresh Active Project

If files get out of sync:

```bash
./gradlew "Refresh active project"
```

### Reset to Default (1.21.1)

```bash
./gradlew "Reset active project"
```

## Requirements

- **Java 21** (required for all versions, configured in `gradle.properties`)
- **Gradle 8+** (included via wrapper)

## Troubleshooting

### Build fails with "cannot find symbol" errors

- You forgot to set the active project first!
- Run: `./gradlew "Set active project to VERSION"`

### IDE shows red squiggles

- Refresh Gradle project after setting active version
- Make sure you set the active project for the version you want to develop

### Wrong version gets built

- Double-check you ran "Set active project" for the correct version
- The build uses whatever version is currently active

## Version-Specific Dependencies

Dependencies are defined in `versions/VERSION/gradle.properties`:

- **1.20.1**: Loom 1.8.9, Fabric API 0.92.6, ModMenu 7.2.2, Cloth Config 11.1.118
- **1.21**: Loom 1.8.9, Fabric API 0.102.0, ModMenu 11.0.1, Cloth Config 15.0.127
- **1.21.1**: Loom 1.8.9, Fabric API 0.105.0, ModMenu 11.0.2, Cloth Config 15.0.128
