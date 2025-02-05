package io.github.chindeaytb;

import io.github.chindeaytb.UpdateContext;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        UpdateContext updater = new UpdateContext(
                "sct", // Modrinth Project ID
                "1.1.0", // Current Version
                new File(System.getenv("APPDATA") + "/.minecraft/mods/") // Use directory, not fixed filename
        );

        updater.checkAndUpdate().join();
    }
}
