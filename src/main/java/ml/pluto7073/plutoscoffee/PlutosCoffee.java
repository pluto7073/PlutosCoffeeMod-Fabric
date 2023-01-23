package ml.pluto7073.plutoscoffee;

import ml.pluto7073.plutoscoffee.effects.ModStatusEffects;
import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.potions.ModPotions;
import ml.pluto7073.plutoscoffee.registry.ModStats;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.screen.WarningScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlutosCoffee implements ModInitializer {

    public static final String MOD_ID = "plutoscoffee";
    public static final Logger logger = LogManager.getLogger("PlutosCoffeeMod");
    public static final int MOD_VERSION = 5;

    @Override
    public void onInitialize() {
        ModBlocks.registerBlocks();
        ModItems.registerItems();
        ModPotions.registerPotions();
        ModStats.registerStats();
        ModStatusEffects.init();
        logger.info("Pluto's Coffee Mod Initialized");
    }

}
