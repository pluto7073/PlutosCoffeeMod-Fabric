package ml.pluto7073.plutoscoffee.config;

import ml.pluto7073.plutoscoffee.PlutosCoffee;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class CoffeeConfig {

    private final Properties properties;

    public CoffeeConfig(File configFile) {
        properties = new Properties();
        loadDefaults();

        if (!configFile.exists()) return;
        try {
            properties.load(new FileReader(configFile));
        } catch (IOException e) {
            PlutosCoffee.logger.error("Failed to load mod config", e);
        }
    }

    private void loadDefaults() {
        properties.put("shouldShowCoffeeBar", "true");
    }

    public void save(File configFile) {
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                PlutosCoffee.logger.error("Failed to create config file", e);
            }
        }

        try (FileWriter writer = new FileWriter(configFile)) {
            properties.store(writer, "Pluto's Coffee Mod Config");
        } catch (IOException e) {
            PlutosCoffee.logger.error("Failed to save mod config", e);
        }
    }

    public boolean shouldShowCoffeeBar() {
        return Boolean.parseBoolean((String) properties.get("shouldShowCoffeeBar"));
    }

    public void setShouldShowCoffeeBar(boolean state) {
        properties.put("shouldShowCoffeeBar", String.valueOf(state));
    }

}
