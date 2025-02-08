package io.github.chindeaytb;

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
    static String currentVersion;
    static String stream;
    String identifier;
    UpdateTarget target;

    public UpdateContext(String projectId, String currentVersion, String stream, String identifier, UpdateTarget target) {
        this.source = new ModrinthUpdateSource(projectId);
        this.currentVersion = currentVersion;
        this.stream = stream;
        this.identifier = identifier;
        this.target = target;
    }

    public static String getStream() {
        return stream;
    }

    public String getIdentifier() { return identifier;}

    public static String getCurrentVersion() {return currentVersion;}

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

    public CompletableFuture<PotentialUpdate> checkUpdate() {
        return source.checkUpdate()
                .thenApply(it -> new PotentialUpdate(it, this));
    }
}

