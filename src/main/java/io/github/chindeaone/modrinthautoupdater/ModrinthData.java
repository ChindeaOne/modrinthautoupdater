/*
 * Based on libautoupdate by Linnea Gräf (c) 2022.
 * Modified by Chindea Eduard (c) 2025.
 *
 * Licensed under BSD-2-Clause. See LICENSE file for details.
 */
package io.github.chindeaone.modrinthautoupdater;

import java.net.MalformedURLException;
import java.net.URL;

public class ModrinthData {
    String minecraftVersion;
    String version;
    String downloadUrl;
    String filename;

    public ModrinthData(String minecraftVersion,String version, String downloadUrl, String filename) {
        this.minecraftVersion = minecraftVersion;
        this.version = version;
        this.downloadUrl = downloadUrl;
        this.filename = filename;
    }

    public URL getDownloadUrl() throws MalformedURLException {
        return new URL(downloadUrl);
    }
}
