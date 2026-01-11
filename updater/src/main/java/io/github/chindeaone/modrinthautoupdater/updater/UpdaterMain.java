/*
 * Copyright (c) 2022 Linnea Gräf.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * under the BSD-2-Clause license. See LICENSE file for details.
 */
package io.github.chindeaone.modrinthautoupdater.updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class UpdaterMain {

    public static void main(String[] args) throws InterruptedException, IOException {
        File libautoupdate_folder = new File(".autoupdates", "postexit.log");
        libautoupdate_folder.getParentFile().mkdirs();
        PrintStream printStream = new PrintStream(new FileOutputStream(libautoupdate_folder, true));
        System.setErr(printStream);
        System.setOut(printStream);
        for (int i = 2; i < args.length; i++) {
            switch (args[i].intern()) {
                case "delete":
                    File file = unlockedFile(args[++i]);
                    if (!file.delete()) {
                        System.out.println("Failed to delete " + file);
                    }
                    break;
                case "move":
                    File from = unlockedFile(args[++i]);
                    File to = unlockedFile(args[++i]);
                    System.out.println("Moving " + from + " to " + to);
                    Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    break;
                default:
                    System.exit(1);
            }
        }
    }

    public static File unlockedFile(String name) throws InterruptedException {
        File file = new File(name);
        while (file.exists() && !file.renameTo(file)) {
            Thread.sleep(1000L);
        }
        file.getParentFile().mkdirs();
        return file;
    }
}
