package io.github.chindeaytb;

import java.net.MalformedURLException;
import java.net.URL;

public class ModrinthData {
    String version;
    String downloadUrl;
    String filename;

    public ModrinthData(String version, String downloadUrl, String filename) {
        this.version = version;
        this.downloadUrl = downloadUrl;
        this.filename = filename;
    }

    public URL getDownloadUrl() throws MalformedURLException {
        return new URL(downloadUrl);
    }
}
