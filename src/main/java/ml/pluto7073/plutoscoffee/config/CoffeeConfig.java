package ml.pluto7073.plutoscoffee.config;

import ml.pluto7073.pdapi.config.BaseConfig;
import ml.pluto7073.plutoscoffee.PlutosCoffee;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class CoffeeConfig extends BaseConfig {

    public CoffeeConfig() {
        super("plutoscoffee", "client", PlutosCoffee.logger);
    }
    public boolean shouldShowCoffeeBar() {
        return getBoolean("shouldShowCoffeeBar");
    }

    public void setShouldShowCoffeeBar(boolean state) {
        setBoolean("shouldShowCoffeeBar", state);
    }

    @Override
    public void initConfig() {
        setBoolean("shouldShowCoffeeBar", true);
    }
}
