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

                // Delete old mod versions
                File[] oldMods = modFolder.listFiles((dir, name) ->
                        name.startsWith("SkyblockCollectionTracker") && name.endsWith(".jar"));

                if (oldMods != null) {
                    for (File oldMod : oldMods) {
                        oldMod.delete();
                    }
                }

                // Move the new mod into place
                Files.move(newModFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Mod updated successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
