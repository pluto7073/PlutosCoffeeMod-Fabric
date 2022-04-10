package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModStats {

    public static final Identifier DRINK_COFFEE = new Identifier(PlutosCoffee.MOD_ID, "drink_coffee");

    public static void registerStats() {
        Registry.register(Registry.CUSTOM_STAT, DRINK_COFFEE.getPath(), DRINK_COFFEE);
        Stats.CUSTOM.getOrCreateStat(DRINK_COFFEE, StatFormatter.DEFAULT);
    }

}
