package io.github.chindeaytb;

public class UpdateData {
    private final String version;
    private final String downloadUrl;
    private final String filename;

    public UpdateData(String version, String downloadUrl, String filename) {
        this.version = version;
        this.downloadUrl = downloadUrl;
        this.filename = filename;
    }

    public String getVersion() {
        return version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getFilename() {
        return filename;
    }
}
