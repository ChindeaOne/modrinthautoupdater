/*
 * Copyright (c) 2022 Linnea Gräf.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * under the BSD-2-Clause license. See LICENSE file for details.
 */
package io.github.chindeaytb;

import lombok.Getter;
import lombok.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.CompletableFuture;

@Value
public class UpdateContext {
    ModrinthUpdateSource source;
    @Getter
    static String minecraftVersion;
    @Getter
    static String currentVersion;
    @Getter
    static String stream;
    @Getter
    String identifier;
    UpdateTarget target;

    public UpdateContext(String projectId, String minecraftVersion ,String currentVersion, String stream, String identifier, UpdateTarget target) {
        this.source = new ModrinthUpdateSource(projectId);
        UpdateContext.minecraftVersion = minecraftVersion;
        UpdateContext.currentVersion = currentVersion;
        UpdateContext.stream = stream;
        this.identifier = identifier;
        this.target = target;
    }

    public void setStream(String stream) {
        UpdateContext.stream = stream;
    }

    public void cleanup() {
        File file = new File(".autoupdates", identifier).getAbsoluteFile();
        try {
            if (!file.exists()) return;
            Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<UpdateSetup> checkUpdate() {
        return source.checkUpdate()
                .thenApply(it -> new UpdateSetup(it, this));
    }
}

