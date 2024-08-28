package ml.pluto7073.plutoscoffee;

import ml.pluto7073.plutoscoffee.coffee.CoffeeGrounds;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSources;
import ml.pluto7073.plutoscoffee.registry.*;
import ml.pluto7073.plutoscoffee.version.VersionChecker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
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
        ModMisc.init();
        ModScreens.init();
        ModStats.registerStats();
        CoffeeGrounds.init();
        ModRecipes.init();
        ModSounds.init();
        ModPointOfInterests.init();
        ModVillagerProfessions.init();

        ModVillagerProfessions.postInit();
        ServerLifecycleEvents.SERVER_STARTING.register(PlutosCoffee::initStructurePoolElements);
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new MachineWaterSources());

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
        Registry<StructureTemplatePool> templatePoolRegistry = server.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);

        ResourceLocation snowyLocation = new ResourceLocation("minecraft:village/snowy/houses");
        CoffeeUtil.addElementToStructurePool(templatePoolRegistry, snowyLocation, "village/snowy/houses/snowy_cafe", 2);

        ResourceLocation plainsLocation = new ResourceLocation("minecraft:village/plains/houses");
        CoffeeUtil.addElementToStructurePool(templatePoolRegistry, plainsLocation, "village/plains/houses/plains_cafe", 2);

        ResourceLocation desertLocation = new ResourceLocation("minecraft:village/desert/houses");
        CoffeeUtil.addElementToStructurePool(templatePoolRegistry, desertLocation, "village/desert/houses/desert_cafe", 2);

        ResourceLocation savannaLocation = new ResourceLocation("minecraft:village/savanna/houses");
        CoffeeUtil.addElementToStructurePool(templatePoolRegistry, savannaLocation, "village/savanna/houses/savanna_cafe", 2);

        ResourceLocation taigaLocation = new ResourceLocation("minecraft:village/taiga/houses");
        CoffeeUtil.addElementToStructurePool(templatePoolRegistry, taigaLocation, "village/taiga/houses/taiga_cafe", 2);
    }

    public static ResourceLocation asId(String id) {
        return new ResourceLocation(MOD_ID, id);
    }

}
