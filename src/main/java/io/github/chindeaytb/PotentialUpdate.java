package io.github.chindeaytb;

import lombok.Value;
import lombok.val;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Value
public class PotentialUpdate {
    UpdatePackage update;
    UpdateContext context;
    UUID updateUUID = UUID.randomUUID();

    public PotentialUpdate(UpdatePackage update, UpdateContext context) {
        this.update = update;
        this.context = context;
    }

    public File getTempJarFile() {
        return getFile("temp.jar");
    }

    private File getFile(String name) {
        getUpdateDirectory().mkdirs();
        return new File(getUpdateDirectory(), name);
    }

    public String getFileName() {
        return update.filename;
    }

    public File getUpdateDirectory() {
        return new File(".autoupdates", context.getIdentifier() + "/" + updateUUID);
    }

    public CompletableFuture<Void> launchUpdate() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                executeUpdate();
            } catch (IOException e) {
                throw new CompletionException(e);
            }
            return null;
        });
    }

    public void prepareUpdate() throws IOException {
        extractUpdater();
        downloadUpdate();
    }

    public void executeUpdate() throws IOException {
        prepareUpdate();
        executePreparedUpdate();
    }

    public void extractUpdater() throws IOException {
        val file = getFile("updater.jar");
        try (val from = getClass().getResourceAsStream("/updater.jar");
             val to = new FileOutputStream(file)) {
            val buf = new byte[4096];
            int r;
            while ((r = from.read(buf)) != -1) {
                to.write(buf, 0, r);
            }
        }
    }

    public void executePreparedUpdate() {
        ShutdownHookManager.setExitHook(
                getContext().getIdentifier(),
                getUpdateUUID(),
                getFile("updater.jar"),
                context.getTarget().generateUpdateActions(this));
    }

    private void downloadUpdate() {
        try {
            val conn = update.getDownloadUrl().openConnection();
            val from = conn.getInputStream();
            val to = new FileOutputStream(getTempJarFile());

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = from.read(buffer)) != -1) {
                to.write(buffer, 0, bytesRead);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
