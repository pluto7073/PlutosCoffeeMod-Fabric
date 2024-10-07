package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class ModStats {

    public static final ResourceLocation DRINK_COFFEE = new ResourceLocation(PlutosCoffee.MOD_ID, "drink_coffee");
    public static final ResourceLocation INTERACT_WITH_COFFEE_BREWER = new ResourceLocation(PlutosCoffee.MOD_ID, "interact_with_coffee_brewer");
    public static final ResourceLocation INTERACT_WITH_COFFEE_GRINDR = new ResourceLocation(PlutosCoffee.MOD_ID, "interact_with_coffee_grinder");
    public static final ResourceLocation INTERACT_WITH_ESPRESSO_MACHINE = new ResourceLocation(PlutosCoffee.MOD_ID, "interact_with_espresso_machine");

    public static void registerStats() {
        Registry.register(BuiltInRegistries.CUSTOM_STAT, DRINK_COFFEE.getPath(), DRINK_COFFEE);
        Stats.CUSTOM.get(DRINK_COFFEE, StatFormatter.DEFAULT);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, INTERACT_WITH_COFFEE_BREWER.getPath(), INTERACT_WITH_COFFEE_BREWER);
        Stats.CUSTOM.get(INTERACT_WITH_COFFEE_BREWER, StatFormatter.DEFAULT);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, INTERACT_WITH_COFFEE_GRINDR.getPath(), INTERACT_WITH_COFFEE_GRINDR);
        Stats.CUSTOM.get(INTERACT_WITH_COFFEE_GRINDR, StatFormatter.DEFAULT);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, INTERACT_WITH_ESPRESSO_MACHINE.getPath(), INTERACT_WITH_ESPRESSO_MACHINE);
        Stats.CUSTOM.get(INTERACT_WITH_ESPRESSO_MACHINE, StatFormatter.DEFAULT);
    }

}
