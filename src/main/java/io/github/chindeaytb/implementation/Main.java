package io.github.chindeaytb.implementation;

import io.github.chindeaytb.implementation.updater.Updater;

public class Main {
    public static void main(String[] args) {
        Updater.downloadJarFromUrl();
        Updater.replaceJarFile();
        Updater.deleteDirectoryAfterDelay();
    }
}