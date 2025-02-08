package io.github.chindeaytb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExitHookInvoker {

    private static boolean isExitHookRegistered = false;
    private static List<UpdateAction> actions;
    private static File updaterJar;
    private static boolean cancelled = false;
    private static String identifer;
    private static UUID uuid;

    public static synchronized void setExitHook(String identifier, UUID uuid, File updaterJar, List<UpdateAction> actions) {
        if (!isExitHookRegistered) {
            Runtime.getRuntime().addShutdownHook(new Thread(ExitHookInvoker::runExitHook));

            isExitHookRegistered = true;
        }
        ExitHookInvoker.identifer = identifier;
        ExitHookInvoker.uuid = uuid;
        ExitHookInvoker.cancelled = false;
        ExitHookInvoker.actions = actions;
        ExitHookInvoker.updaterJar = updaterJar;
    }

    private static synchronized String[] buildInvocation() {
        boolean isWindows = System.getProperty("os.name", "").startsWith("Windows");
        File javaBinary = new File(System.getProperty("java.home"), "bin/java" + (isWindows ? ".exe" : ""));


        List<String> arguments = new ArrayList<>();
        arguments.add(javaBinary.getAbsolutePath());
        arguments.add("-jar");
        arguments.add(updaterJar.getAbsolutePath());

        arguments.add(identifer);
        arguments.add(String.valueOf(uuid));

        for (UpdateAction action : actions) {
            action.encode(arguments);
        }

        return arguments.toArray(new String[0]);
    }

    private static void runExitHook() {
        try {
            if (cancelled) {
                return;
            }
            String[] invocation = buildInvocation();
            Runtime.getRuntime().exec(invocation);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


}
