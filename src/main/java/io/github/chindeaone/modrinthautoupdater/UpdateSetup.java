/*
 * Copyright (c) 2022 Linnea Gräf.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * under the BSD-2-Clause license. See LICENSE file for details.
 */
package io.github.chindeaone.modrinthautoupdater;

import lombok.Value;
import lombok.val;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Value
public class UpdateSetup {
    ModrinthData update;
    UpdateContext context;
    UUID updateUUID = UUID.randomUUID();

    public File getTempJarFile() {
        return getFile("temp.jar");
    }

    private File getFile(String name) {
        getUpdateDirectory().mkdirs();
        return new File(getUpdateDirectory(), name);
    }

    public File getUpdateDirectory() {
        return new File(".autoupdates", context.getIdentifier() + "/" + updateUUID);
    }

    public CompletableFuture<Void> launchUpdate() {
        return CompletableFuture.runAsync(() -> {
            try {
                executeUpdate();
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    public void executeUpdate() throws IOException {
        prepareUpdate();
        executeUpdater();
    }

    public void prepareUpdate() throws IOException {
        extractUpdater();
        downloadUpdate();
    }

    public void extractUpdater() throws IOException {
        try (val from = getClass().getResourceAsStream("/updater.jar");
             val to = new FileOutputStream(getFile("updater.jar"))) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = from.read(buffer)) != -1) {
                to.write(buffer, 0, bytesRead);
            }
        }
    }

    private void downloadUpdate() {
        try (val from = update.getDownloadUrl().openStream();
             val to = new FileOutputStream(getTempJarFile())) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = from.read(buffer)) != -1) {
                to.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeUpdater() {
        ShutdownHookManager.setExitHook(
                context.getIdentifier(),
                updateUUID,
                getFile("updater.jar"),
                context.getTarget().createActions(this)
        );
    }
}
