package net.pandadev.sharedbackpacks.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private static final String BASE_DIR = "plugins/SharedBackpacks/backpacks";

    public static FileConfiguration createConfig(String configFileName) {
        File configFile = new File(BASE_DIR, configFileName + ".yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(configFile);
    }

    public static FileConfiguration loadConfig(String configFileName) {
        File configFile = new File(BASE_DIR, configFileName + ".yml");
        return YamlConfiguration.loadConfiguration(configFile);
    }

    public static void saveConfig(String configFileName, FileConfiguration config) {
        File configFile = new File(BASE_DIR, configFileName + ".yml");
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}