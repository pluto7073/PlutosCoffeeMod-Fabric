package ml.pluto7073.plutoscoffee.config;

import ml.pluto7073.pdapi.config.BaseConfig;
import ml.pluto7073.plutoscoffee.PlutosCoffee;

public class CoffeeConfig extends BaseConfig {

    public CoffeeConfig() {
        super("plutoscoffee", "client", PlutosCoffee.LOGGER);
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
