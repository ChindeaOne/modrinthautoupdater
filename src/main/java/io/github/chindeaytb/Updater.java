package io.github.chindeaytb;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;

public class Updater {

    private static final String CONFIG_DIR;

    static {
        CONFIG_DIR = System.getenv("APPDATA") + File.separator + ".minecraft" + File.separator + "config" + File.separator + "sct";
    }

    private static int getUpdateValue() {
        File configFile = new File(CONFIG_DIR, "config.json");
        if (!configFile.exists()) {
            System.out.println("config.json not found!");
            return 0;
        }

        try (FileReader reader = new FileReader(configFile)) {
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(reader).getAsJsonObject(); // OLD Gson 2.2.4 Method
            return json.getAsJsonObject("about").get("update").getAsInt();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
