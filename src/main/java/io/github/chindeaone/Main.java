/*
 * Based on libautoupdate by Linnea Gräf (c) 2022.
 * Modified by Chindea Eduard (c) 2025.
 *
 * Licensed under BSD-2-Clause. See LICENSE file for details.
 */
package io.github.chindeaone;

public class Main {
    public static void main(String[] args) {
        UpdateContext updater = new UpdateContext(
                "sct",
                "1.8.9",
                "1.1.0",
                "beta",
                "SkyblockCollectionTracker",
                UpdateTarget.deleteAndSaveInTheSameFolder(Main.class)
        );
        updater.cleanup();

        updater.checkUpdate().thenCompose(UpdateSetup::launchUpdate).join();
    }
}
