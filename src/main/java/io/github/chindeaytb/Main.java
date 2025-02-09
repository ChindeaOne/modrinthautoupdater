package io.github.chindeaytb;

public class Main {
    public static void main(String[] args) {
        UpdateContext updater = new UpdateContext(
                "sct",
                "1.1.0",
                "beta",
                "SkyblockCollectionTracker",
                UpdateTarget.deleteAndSaveInTheSameFolder(Main.class)
        );
        updater.cleanup();

        updater.checkUpdate().thenCompose(UpdateSetup::launchUpdate).join();
    }
}
