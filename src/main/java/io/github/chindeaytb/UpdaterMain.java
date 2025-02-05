package io.github.chindeaytb;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class UpdaterMain {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid arguments for updater.");
            return;
        }

        String modFolderPath = args[0];
        String newModPath = args[1];

        File modFolder = new File(modFolderPath);
        File newModFile = new File(newModPath);

        try {
            if (newModFile.exists() && modFolder.exists()) {
                File targetFile = new File(modFolder, newModFile.getName());

                String targetFileName = "SkyblockCollectionTracker-" + UpdateData.getVersion() +".jar";

                File[] oldMods = modFolder.listFiles((dir, name) -> name.equals(targetFileName));

                if (oldMods != null && oldMods.length > 0) {
                    deleteWhenUnlocked(oldMods[0]);
                }

                Files.move(newModFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Mod updated successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteWhenUnlocked(File file) {
        int maxAttempts = 10;
        int attempt = 0;
        boolean deleted = false;

        while (attempt < maxAttempts && !deleted) {
            try {
                if (file.exists() && file.canWrite()) {
                    deleted = file.delete();
                    if (deleted) {
                        System.out.println("Deleted old mod: " + file.getName());
                    }
                }
                if (!deleted) {
                    System.out.println("File is locked, retrying...");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            attempt++;
        }

        if (!deleted) {
            System.out.println("Failed to delete file after " + maxAttempts + " attempts.");
        }
    }
}
