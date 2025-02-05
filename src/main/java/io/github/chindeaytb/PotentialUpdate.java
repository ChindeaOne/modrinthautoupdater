package io.github.chindeaytb;

import java.io.*;
import java.net.URL;

public class PotentialUpdate {
    private final UpdateData update;
    private final File modFileDirectory;

    public PotentialUpdate(UpdateData update, File modFileDirectory) {
        this.update = update;
        this.modFileDirectory = modFileDirectory;
    }

    public void launchUpdate() {
        try {
            File downloadedJar = downloadUpdate();
            extractUpdaterJar(); // <-- Extract updater.jar first
            runUpdater(downloadedJar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File downloadUpdate() throws IOException {
        System.out.println("Downloading update: " + update.getDownloadUrl());
        File tempFile = new File(modFileDirectory, update.getFilename());

        try (InputStream in = new URL(update.getDownloadUrl()).openStream();
             FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        System.out.println("Download complete: " + tempFile.getAbsolutePath());
        return tempFile;
    }

    private void extractUpdaterJar() throws IOException {
        File updaterFile = new File(modFileDirectory, "updater.jar");

        try (InputStream in = getClass().getResourceAsStream("/updater/updater.jar");
             FileOutputStream out = new FileOutputStream(updaterFile)) {

            if (in == null) {
                throw new FileNotFoundException("Updater JAR not found inside resources.");
            }

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        System.out.println("Updater extracted successfully.");
    }

    private void runUpdater(File newModFile) {
        try {
            File updaterJar = new File(modFileDirectory, "updater.jar");
            if (!updaterJar.exists()) {
                System.out.println("Updater not found!");
                return;
            }

            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-jar", updaterJar.getAbsolutePath(),
                    modFileDirectory.getAbsolutePath(),
                    newModFile.getAbsolutePath()
            );

            pb.inheritIO();
            pb.start();

            System.out.println("Updater scheduled. Exiting main application...");
            System.exit(0);  // Exit to allow updater to run
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
