package io.github.chindeaytb;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class UpdateContext {
    private final ModrinthUpdateSource source;
    private final String currentVersion;
    private final File modFileDirectory;

    public UpdateContext(String projectId, String currentVersion, File modFileDirectory) {
        this.source = new ModrinthUpdateSource(projectId);
        this.currentVersion = currentVersion;
        this.modFileDirectory = modFileDirectory;
    }

    public CompletableFuture<Void> checkAndUpdate() {
        return source.checkUpdate().thenAccept(update -> {
            if (update != null && isNewerVersion(update.getVersion())) {
                System.out.println("New version found: " + update.getVersion());
                new PotentialUpdate(update, modFileDirectory).launchUpdate();
            } else {
                System.out.println("No updates available.");
            }
        });
    }

    private boolean isNewerVersion(String newVersion) {
        return !newVersion.equalsIgnoreCase(currentVersion);
    }
}

