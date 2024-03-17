package ml.pluto7073.plutoscoffee;

import ml.pluto7073.plutoscoffee.coffee.CoffeeGrounds;
import ml.pluto7073.plutoscoffee.registry.*;
import ml.pluto7073.plutoscoffee.version.VersionChecker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerTypeHelper;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlutosCoffee implements ModInitializer {

    public static final String MOD_ID = "plutoscoffee";
    public static final Logger logger = LogManager.getLogger("PlutosCoffeeMod");
    public static final int MOD_VERSION = 10;

    private static boolean loadLaterDone = false;

    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModItems.init();
        ModStatusEffects.init();
        ModMisc.init();
        ModScreens.init();
        ModStats.registerStats();
        CoffeeGrounds.init();
        ModPointOfInterests.init();
        ModVillagerProfessions.init();
        ServerLifecycleEvents.SERVER_STARTING.register(PlutosCoffee::initStructurePoolElements);

        logger.info("Pluto's Coffee Mod Initialized");
    }

    public static void loadLater() {
        if (loadLaterDone) return;
        VersionChecker.checkOutdated();
        if (VersionChecker.isOutdated()) {
            logger.warn("Pluto's Coffee Mod is outdated, please download the new version from Curseforge or Github");
        } else {
            logger.info("Pluto's Coffee Mod is up to date");
        }
        loadLaterDone = true;
    }

    /**
     * Borrowed from FriendsAndFoes by Faboslav
     */
    public static void initStructurePoolElements(MinecraftServer server) {
        Registry<StructurePool> templatePoolRegistry = server.getRegistryManager().get(RegistryKeys.TEMPLATE_POOL);

        Identifier snowyLocation = new Identifier("minecraft:village/snowy/houses");
        CoffeeUtil.addElementToStructurePool(templatePoolRegistry, snowyLocation, "village/snowy/houses/snowy_cafe", 2);
    }

    public static Identifier asId(String id) {
        return new Identifier(MOD_ID, id);
    }

}
