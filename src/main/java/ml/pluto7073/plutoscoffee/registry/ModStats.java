package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModStats {

    public static final Identifier DRINK_COFFEE = new Identifier(PlutosCoffee.MOD_ID, "drink_coffee");
    public static final Identifier INTERACT_WITH_COFFEE_BREWER = new Identifier(PlutosCoffee.MOD_ID, "interact_with_coffee_brewer");
    public static final Identifier INTERACT_WITH_COFFEE_WORKSTATION = new Identifier(PlutosCoffee.MOD_ID, "interact_with_coffee_workstation");

    public static void registerStats() {
        Registry.register(Registry.CUSTOM_STAT, DRINK_COFFEE.getPath(), DRINK_COFFEE);
        Stats.CUSTOM.getOrCreateStat(DRINK_COFFEE, StatFormatter.DEFAULT);
        Registry.register(Registry.CUSTOM_STAT, INTERACT_WITH_COFFEE_BREWER.getPath(), INTERACT_WITH_COFFEE_BREWER);
        Stats.CUSTOM.getOrCreateStat(INTERACT_WITH_COFFEE_BREWER, StatFormatter.DEFAULT);
        Registry.register(Registry.CUSTOM_STAT, INTERACT_WITH_COFFEE_WORKSTATION.getPath(), INTERACT_WITH_COFFEE_WORKSTATION);
        Stats.CUSTOM.getOrCreateStat(INTERACT_WITH_COFFEE_WORKSTATION, StatFormatter.DEFAULT);
    }

}
