# modrinthautoupdater

A Java library for creating auto-updates for Minecraft mods from Modrinth.

## Credits

This project is based on [libautoupdate](https://github.com/nea89o/libautoupdate) by nea89, licensed under BSD-2-Clause.

- **libautoupdate** provides auto-update functionality and is used as a foundation for this library.

## Installation

First, include the library in your build using a suitable method (shadow plugin for example).

```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    shadowImpl("com.github.ChindeaYTB:modrinthautoupdater:1.1.2")
}
```

If your target platform already includes Google's Gson library, consider excluding transitive dependencies.

## Usage

To integrate auto-updates into your application, instantiate and store an `UpdateContext`:

```java
UpdateContext context =new UpdateContext(
        "your-modrinth-project-id",
        "minecraft_version_used" // 1.8.9 or 1.21.5
        "1.0.0", // Current version
        "release", // Stream can be either "release" or "beta"
        "your-modid",
        UpdateTarget.deleteAndSaveInTheSameFolder(YourClassName.class)
        );
```

You need to provide:
- The project ID for identifying the update source.
- The Minecraft version you are playing on.
- The current application version.
- The update stream ("release" or "beta").
- A unique identifier for the application/mod.
- The update target (the class where "context" is created).

### Cleanup
On startup, it's recommended to call updateContext.cleanup() to remove any leftover files from previous updates.

### Sources

 - Modrinth Releases Source

Fetches the latest release from Modrinth for a given project ID. The update stream can be set to either "release" or "beta" to determine which versions are fetched. The download URL and metadata are retrieved from the Modrinth API.

### Targets

 - DeleteAndSaveInSameFolder

Deletes the original jar and downloads the new jar into the same directory, while keeping the name specified by the download url.

### Checking for an update

To verify if an update is available, call `context.checkUpdate()`.
Once an update is detected, execute `potentialUpdate.launchUpdate()`.
This will download the new version immediately and apply it after the application closes.
