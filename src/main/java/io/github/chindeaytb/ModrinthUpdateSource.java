package io.github.chindeaytb;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class ModrinthUpdateSource {
    private static final String CONFIG_DIR;
    private static final String CONFIG_FILE = "config.json";

    static {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (os.contains("win")) {
            CONFIG_DIR = System.getenv("APPDATA") + File.separator + ".minecraft" + File.separator + "config" + File.separator + "sct";
        } else {
            CONFIG_DIR = System.getProperty("user.home") + File.separator + ".minecraft" + File.separator + "config" + File.separator + "sct";
        }
    }

    private final String projectId;

    public ModrinthUpdateSource(String projectId) {
        this.projectId = projectId;
    }

    public CompletableFuture<UpdateData> checkUpdate() {
        return CompletableFuture.supplyAsync(() -> {
            int updatePreference = getUpdateValue();
            try {
                URL url = new URL("https://api.modrinth.com/v2/project/" + projectId + "/version");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                if (connection.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    JsonParser parser = new JsonParser();
                    JsonArray versions = parser.parse(reader).getAsJsonArray();

                    reader.close();

                    JsonObject selectedVersion = null;

                    for (JsonElement version : versions) {
                        JsonObject versionObj = version.getAsJsonObject();
                        boolean isStable = !versionObj.get("version_type").getAsString().equalsIgnoreCase("beta");

                        // Decide based on user preference (1 = stable, 2 = any version)
                        if ((updatePreference == 1 && isStable) || updatePreference == 2) {
                            selectedVersion = versionObj;
                            break;
                        }
                    }

                    if (selectedVersion != null) {
                        JsonArray files = selectedVersion.getAsJsonArray("files");
                        for (JsonElement file : files) {
                            JsonObject fileObj = file.getAsJsonObject();
                            if (fileObj.get("primary").getAsBoolean()) {
                                return new UpdateData(
                                        selectedVersion.get("version_number").getAsString(),
                                        fileObj.get("url").getAsString(),
                                        fileObj.get("filename").getAsString()
                                );
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private static int getUpdateValue() {
        File configFile = new File(CONFIG_DIR, CONFIG_FILE);
        if (!configFile.exists()) {
            System.out.println("Config file not found! Defaulting to stable updates.");
            return 1; // Default to stable updates if no config is found
        }

        try (FileReader reader = new FileReader(configFile)) {
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(reader).getAsJsonObject();
            return json.getAsJsonObject("about").get("update").getAsInt();
        } catch (Exception e) {
            e.printStackTrace();
            return 1; // Default to stable updates in case of an error
        }
    }
}
