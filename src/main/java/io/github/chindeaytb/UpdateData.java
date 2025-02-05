package io.github.chindeaytb;

public class UpdateData {
    private static  String version;
    private final String downloadUrl;
    private final String filename;

    public UpdateData(String version, String downloadUrl, String filename) {
        UpdateData.version = version;
        this.downloadUrl = downloadUrl;
        this.filename = filename;
    }

    static public String getVersion() {
        return version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getFilename() {
        return filename;
    }
}
