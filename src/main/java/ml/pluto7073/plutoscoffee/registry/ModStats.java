package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class ModStats {

    public static final Identifier DRINK_COFFEE = new Identifier(PlutosCoffee.MOD_ID, "drink_coffee");
    public static final Identifier INTERACT_WITH_COFFEE_BREWER = new Identifier(PlutosCoffee.MOD_ID, "interact_with_coffee_brewer");
    public static final Identifier INTERACT_WITH_COFFEE_GRINDR = new Identifier(PlutosCoffee.MOD_ID, "interact_with_coffee_grinder");
    public static final Identifier INTERACT_WITH_COFFEE_WORKSTATION = new Identifier(PlutosCoffee.MOD_ID, "interact_with_coffee_workstation");
    public static final Identifier INTERACT_WITH_ESPRESSO_MACHINE = new Identifier(PlutosCoffee.MOD_ID, "interact_with_espresso_machine");

    public static void registerStats() {
        Registry.register(Registries.CUSTOM_STAT, DRINK_COFFEE.getPath(), DRINK_COFFEE);
        Stats.CUSTOM.getOrCreateStat(DRINK_COFFEE, StatFormatter.DEFAULT);
        Registry.register(Registries.CUSTOM_STAT, INTERACT_WITH_COFFEE_BREWER.getPath(), INTERACT_WITH_COFFEE_BREWER);
        Stats.CUSTOM.getOrCreateStat(INTERACT_WITH_COFFEE_BREWER, StatFormatter.DEFAULT);
        Registry.register(Registries.CUSTOM_STAT, INTERACT_WITH_COFFEE_WORKSTATION.getPath(), INTERACT_WITH_COFFEE_WORKSTATION);
        Stats.CUSTOM.getOrCreateStat(INTERACT_WITH_COFFEE_WORKSTATION, StatFormatter.DEFAULT);
        Registry.register(Registries.CUSTOM_STAT, INTERACT_WITH_COFFEE_GRINDR.getPath(), INTERACT_WITH_COFFEE_GRINDR);
        Stats.CUSTOM.getOrCreateStat(INTERACT_WITH_COFFEE_GRINDR, StatFormatter.DEFAULT);
        Registry.register(Registries.CUSTOM_STAT, INTERACT_WITH_ESPRESSO_MACHINE.getPath(), INTERACT_WITH_ESPRESSO_MACHINE);
        Stats.CUSTOM.getOrCreateStat(INTERACT_WITH_ESPRESSO_MACHINE, StatFormatter.DEFAULT);
    }

}
