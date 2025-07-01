package io.github.chindeaytb;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class ModrinthUpdateSource {

    private final String projectId;

    public ModrinthUpdateSource(String projectId) {
        this.projectId = projectId;
    }

    public CompletableFuture<ModrinthData> checkUpdate() {
        return CompletableFuture.supplyAsync(() -> {
            int updatePreference = 0;
            String currentVersion = UpdateContext.getCurrentVersion();

            if (UpdateContext.getStream().equalsIgnoreCase("beta")) {
                updatePreference = 2;
            } else if (UpdateContext.getStream().equalsIgnoreCase("release")) {
                updatePreference = 1;
            }

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

                    String targetMcVersion = UpdateContext.getMinecraftVersion();
                    JsonObject selectedVersion = null;
                    String selectedGameVersion = null;

                    for (JsonElement version : versions) {
                        JsonObject versionObj = version.getAsJsonObject();
                        String versionNumber = versionObj.get("version_number").getAsString();
                        boolean isStable = !versionObj.get("version_type").getAsString().equalsIgnoreCase("beta");

                        // Check if this version supports the target Minecraft version
                        JsonArray gameVersions = versionObj.getAsJsonArray("game_versions");
                        boolean supportsTargetVersion = false;
                        for (JsonElement gv : gameVersions) {
                            if (gv.getAsString().equals(targetMcVersion)) {
                                supportsTargetVersion = true;
                                break;
                            }
                        }
                        if (!supportsTargetVersion) continue;

                        if ((updatePreference == 1 && isStable) || updatePreference == 2) {
                            if (currentVersion.equals(versionNumber)) {
                                System.out.println("No update needed, latest version already installed: " + currentVersion);
                                return null;
                            }
                            selectedVersion = versionObj;
                            selectedGameVersion = targetMcVersion;
                            break;
                        }
                    }

                    if (selectedVersion != null) {
                        JsonArray files = selectedVersion.getAsJsonArray("files");
                        for (JsonElement file : files) {
                            JsonObject fileObj = file.getAsJsonObject();
                            if (fileObj.get("primary").getAsBoolean()) {
                                return new ModrinthData(
                                        selectedGameVersion,
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
}
