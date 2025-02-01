package io.github.chindeaytb.implementation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;

public class Updater {

    private static final String MOD_DIR;
    private static final String CONFIG_DIR;
    private static final String TEMP_DIR;
    private static String fileUrl = "";

    static {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (os.contains("win")) {
            CONFIG_DIR = System.getenv("APPDATA") + File.separator + ".minecraft" + File.separator + "config" + File.separator + "sct";
            MOD_DIR = System.getenv("APPDATA") + File.separator + ".minecraft" + File.separator + "mods";
        } else {
            CONFIG_DIR = System.getProperty("user.home") + File.separator + ".minecraft" + File.separator + "config" + File.separator + "sct";
            MOD_DIR = System.getProperty("user.home") + File.separator + ".minecraft" + File.separator + "mods";
        }
        TEMP_DIR = MOD_DIR + File.separator + "NewModVersion";
    }

    private static int getUpdateValue() {
        File configFile = new File(CONFIG_DIR, "config.json");
        if (!configFile.exists()) {
            System.out.println("config.json not found!");
            return 0;
        }

        try (FileReader reader = new FileReader(configFile)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            return json.getAsJsonObject("about").get("update").getAsInt();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static String getLatestModrinthDownloadUrl() {
        String modrinthApiUrl = "https://api.modrinth.com/v2/project/sct/version";
        int update = getUpdateValue();

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(modrinthApiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonArray versions = JsonParser.parseString(response.toString()).getAsJsonArray();
                JsonObject selectedVersion = null;

                for (JsonElement version : versions) {
                    JsonObject versionObj = version.getAsJsonObject();
                    boolean isStable = !versionObj.get("version_type").getAsString().equalsIgnoreCase("beta");

                    if ((update == 1 && isStable) || update == 2) {
                        selectedVersion = versionObj;
                        break;
                    }
                }

                if (selectedVersion != null) {
                    JsonArray files = selectedVersion.getAsJsonArray("files");
                    for (JsonElement file : files) {
                        JsonObject fileObj = file.getAsJsonObject();
                        if (fileObj.get("primary").getAsBoolean()) {
                            return fileObj.get("url").getAsString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void downloadJarFromUrl() {
        try {
            fileUrl = getLatestModrinthDownloadUrl();
            if (fileUrl == null) {
                System.out.println("Failed to get download URL.");
                return;
            }

            File directory = new File(TEMP_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = getFile();

            System.out.println("Download completed: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getFile() throws IOException {
        String modName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        File file = new File(TEMP_DIR, modName);

        URL url = new URL(fileUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();

        try (InputStream inputStream = urlConnection.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return file;
    }

    public static void replaceJarFile() {
        File modDir = new File(MOD_DIR);
        File newVersionDir = new File(TEMP_DIR);

        try {
            File oldMod = Objects.requireNonNull(modDir.listFiles((dir, name) ->
                    name.startsWith("SkyblockCollectionTracker") && name.endsWith(".jar")))[0];

            Thread.sleep(2000);
            if (oldMod != null) {
                for (int i = 0; i < 5; i++) {
                    if (oldMod.delete()) {
                        break;
                    }
                    Thread.sleep(1000);
                }
            }


            String modName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            File newJarFile = new File(newVersionDir, modName);

            if (newJarFile.exists()) {
                File destinationFile = new File(modDir, modName);
                newJarFile.renameTo(destinationFile);
                System.out.println("Mod updated successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteDirectoryAfterDelay() {
        File directory = new File(TEMP_DIR);

        try {
            Thread.sleep(1000);
            if (directory.exists() && directory.isDirectory()) {
                directory.delete();
                System.out.println("Temporary files deleted.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

