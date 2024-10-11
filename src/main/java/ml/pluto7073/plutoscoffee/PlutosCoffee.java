package ml.pluto7073.plutoscoffee;

import ml.pluto7073.plutoscoffee.coffee.CoffeeGrounds;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSources;
import ml.pluto7073.plutoscoffee.network.ModPacketsS2C;
import ml.pluto7073.plutoscoffee.registry.*;
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
    public static final Logger LOGGER = LogManager.getLogger("PlutosCoffeeMod");

    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModComponents.init();
        ModPacketsS2C.registerPackets();
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

        LOGGER.info("Pluto's Coffee Mod Initialized");
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
