/*
 * Copyright (c) 2022 Linnea Gräf.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * under the BSD-2-Clause license. See LICENSE file for details.
 */
package io.github.chindeaytb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShutdownHookManager {

    private static boolean isHookRegistered = false;
    private static List<UpdateAction> actions;
    private static File updaterJar;
    private static boolean cancelled = false;
    private static String identifier;
    private static UUID uuid;

    public static synchronized void setExitHook(String identifier, UUID uuid, File updaterJar, List<UpdateAction> actions) {
        if (!isHookRegistered) {
            Runtime.getRuntime().addShutdownHook(new Thread(ShutdownHookManager::runExitHook));
            isHookRegistered = true;
        }
        ShutdownHookManager.identifier = identifier;
        ShutdownHookManager.uuid = uuid;
        ShutdownHookManager.cancelled = false;
        ShutdownHookManager.actions = actions;
        ShutdownHookManager.updaterJar = updaterJar;
    }

    private static synchronized String[] buildInvocation() {
        String javaBinary = new File(System.getProperty("java.home"), "bin/java" + (System.getProperty("os.name", "").startsWith("Windows") ? ".exe" : "")).getAbsolutePath();
        List<String> arguments = new ArrayList<>();
        arguments.add(javaBinary);
        arguments.add("-jar");
        arguments.add(updaterJar.getAbsolutePath());
        arguments.add(identifier);
        arguments.add(uuid.toString());
        actions.forEach(action -> action.encode(arguments));
        return arguments.toArray(new String[0]);
    }

    private static void runExitHook() {
        if (!cancelled) {
            try {
                Runtime.getRuntime().exec(buildInvocation());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}