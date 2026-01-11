/*
 * Copyright (c) 2022 Linnea Gräf.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * under the BSD-2-Clause license. See LICENSE file for details.
 */
package io.github.chindeaone.modrinthautoupdater;

import lombok.val;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public interface UpdateTarget {
    List<UpdateAction> createActions(UpdateSetup update);

    static UpdateTarget deleteAndSaveInTheSameFolder(Class<?> containedClass) {
        File file = getJarFileContainingClass(containedClass);
        return new ReplaceWithNewUpdateTarget(file);
    }

    static File getJarFileContainingClass(Class<?> clazz) {
        val location = clazz.getProtectionDomain().getCodeSource().getLocation();
        if (location == null)
            return null;
        String path = location.toString();
        path = path.split("!", 2)[0];
        if (path.startsWith("jar:")) {
            path = path.substring(4);
        }
        try {
            return new File(new URI(path));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

}