package io.github.chindeaytb;

import java.util.concurrent.CompletableFuture;

public class UpdateContext {
    private final ModrinthUpdateSource source;
    private final String currentVersion;

    public UpdateContext(String projectId, String currentVersion) {
        this.source = new ModrinthUpdateSource(projectId);
        this.currentVersion = currentVersion;
    }

    public CompletableFuture<Void> checkAndUpdate() {
        return source.checkUpdate().thenAccept(update -> {
            if (update != null && isNewerVersion(update.getVersion())) {
                System.out.println("New version found: " + update.getVersion());
                new PotentialUpdate(update).launchUpdate();
            } else {
                System.out.println("No updates available.");
            }
        });
    }

    private boolean isNewerVersion(String newVersion) {
        return !newVersion.equalsIgnoreCase(currentVersion);
    }
}

