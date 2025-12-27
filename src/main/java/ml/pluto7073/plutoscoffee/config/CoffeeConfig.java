package ml.pluto7073.plutoscoffee.config;

import ml.pluto7073.plutonium.annotations.BooleanOption;
import ml.pluto7073.plutonium.config.ClientConfig;
import ml.pluto7073.plutoscoffee.PlutosCoffee;

public class CoffeeConfig extends ClientConfig {

    public static final CoffeeConfig INSTANCE = new CoffeeConfig();

    @BooleanOption(defaultVal = true) public boolean shouldShowCoffeeBar;

    public CoffeeConfig() {
        super("plutoscoffee", PlutosCoffee.LOGGER, true);
    }
}
