# Selected Block Highlighter

A client-side Fabric mod for Minecraft that highlights all nearby blocks matching the item in your hand.

## Features

- **Visual Block Highlighting**: Hold any block item and see all matching blocks highlighted in the world
- **Toggleable**: Press `H` (configurable) to turn the highlighter on/off
- **Highly Configurable**: Customize range, colors, and appearance through ModMenu
- **Performance Optimized**: Smart scanning that only updates when needed
- **Multi-Version Support**: Works with Minecraft 1.20.1, 1.21, and 1.21.1

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/)
2. Download and install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download and install [Cloth Config](https://modrinth.com/mod/cloth-config)
4. (Optional) Download [ModMenu](https://modrinth.com/mod/modmenu) for in-game configuration
5. Download the appropriate version of Selected Block Highlighter for your Minecraft version
6. Place the JAR file in your `.minecraft/mods` folder

## Usage

1. Hold any block item in your hand (e.g., diamond ore, cobblestone, dirt)
2. Press `H` to toggle the highlighter (keybind is customizable)
3. All blocks of that type within range will be highlighted with a colored outline
4. The highlighter status shows in your action bar

## Configuration

Open ModMenu and click on **Selected Block Highlighter** to access the config screen.

### General Settings

- **Enabled**: Toggle the mod on/off (also controllable via keybind)
- **Scan Range**: How far to search for matching blocks (1-128 blocks, default: 32)

### Appearance Settings

- **Red/Green/Blue**: Customize the highlight color (0.0-1.0 for each channel)
- **Alpha (Opacity)**: Control transparency (0.0 = invisible, 1.0 = opaque, default: 0.4)
- **Line Width**: Adjust outline thickness (0.5-10.0, default: 2.0)

### Default Settings

- **Color**: Yellow (`RGB: 1.0, 1.0, 0.0`)
- **Opacity**: 40%
- **Range**: 32 blocks
- **Keybind**: `H`

Configuration is automatically saved to `.minecraft/config/selected-block-highlighter.json`

## Keybinds

- **Toggle Highlighter**: `H` (default)
  - Customizable in Minecraft's Controls settings under "Selected Block Highlighter"

## Supported Versions

| Minecraft Version | Fabric Loader | Fabric API | Cloth Config | ModMenu |
|------------------|---------------|------------|--------------|---------|
| 1.20.1           | 0.17.3+       | 0.92.6+    | 11.1.118+    | 7.2.2+  |
| 1.21             | 0.16.5+       | 0.102.0+   | 15.0.127+    | 11.0.1+ |
| 1.21.1           | 0.16.5+       | 0.105.0+   | 15.0.128+    | 11.0.2+ |

## Building from Source

See [BUILD.md](BUILD.md) for detailed build instructions.

Quick build:

```bash
# Build for default version (1.21.1)
./gradlew build

# Build for specific version
./gradlew build -Pstonecutter.active=1.20.1

# Build all versions at once (Windows)
build-all.bat

# Build all versions at once (Linux/Mac)
./build-all.sh
```

## Technical Details

- **Client-Side Only**: This mod only needs to be installed on the client
- **Smart Scanning**: Only rescans when you move or change held items
- **Render Optimization**: Uses efficient batch rendering for highlights
- **Multi-Version**: Built with Stonecutter for seamless multi-version support

## Dependencies

### Required
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Cloth Config](https://modrinth.com/mod/cloth-config)

### Optional
- [ModMenu](https://modrinth.com/mod/modmenu) - For in-game configuration GUI

## License

This project is licensed under CC0-1.0.

## Contributing

Contributions are welcome! Please feel free to submit issues or pull requests.

## Credits

- Built with [Fabric](https://fabricmc.net/)
- Uses [Cloth Config](https://github.com/shedaniel/ClothConfig) for configuration
- Uses [ModMenu](https://github.com/TerraformersMC/ModMenu) for config screen integration
- Multi-version support via [Stonecutter](https://github.com/kikugie/stonecutter)
