package ml.pluto7073.plutoscoffee;

import ml.pluto7073.plutoscoffee.effects.ModStatusEffects;
import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.potions.ModPotions;
import ml.pluto7073.plutoscoffee.registry.ModStats;
import ml.pluto7073.plutoscoffee.version.VersionChecker;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlutosCoffee implements ModInitializer {

    public static PlutosCoffee MOD;
    public static final String MOD_ID = "plutoscoffee";
    public static final Logger logger = LogManager.getLogger("PlutosCoffeeMod");
    public static final int MOD_VERSION = 6;

    private static boolean loadLaterDone = false;

    @Override
    public void onInitialize() {
        MOD = this;
        ModBlocks.registerBlocks();
        ModItems.registerItems();
        ModPotions.registerPotions();
        ModStats.registerStats();
        ModStatusEffects.init();
        logger.info("Pluto's Coffee Mod Initialized");
    }

    public void loadLater() {
        if (loadLaterDone) return;
        VersionChecker.checkOutdated();
        if (VersionChecker.isOutdated()) {
            logger.warn("Pluto's Coffee Mod is outdated, please download the new version from Curseforge or Github");
        } else {
            logger.info("Pluto's Coffee Mod is up to date");
        }
        loadLaterDone = true;
    }

}
