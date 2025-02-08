package io.github.chindeaytb;

import lombok.Value;

import java.net.MalformedURLException;
import java.net.URL;

public class UpdateData {
    String version;
    String downloadUrl;
    String filename;

    public UpdateData(String version, String downloadUrl, String filename) {
        this.version = version;
        this.downloadUrl = downloadUrl;
        this.filename = filename;
    }

    public URL getDownloadUrl() throws MalformedURLException {
        return new URL(downloadUrl);
    }
}
