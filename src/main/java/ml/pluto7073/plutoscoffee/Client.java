package ml.pluto7073.plutoscoffee;

import ml.pluto7073.plutoscoffee.config.CoffeeConfig;
import ml.pluto7073.plutoscoffee.gui.CoffeeBrewerScreen;
import ml.pluto7073.plutoscoffee.gui.CoffeeGrindrScreen;
import ml.pluto7073.plutoscoffee.gui.EspressoMachineScreen;
import ml.pluto7073.plutoscoffee.network.ModPacketsS2C;
import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.registry.ModScreens;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.File;

@Environment(EnvType.CLIENT)
public class Client implements ClientModInitializer {

    public static CoffeeConfig CONFIG;

    public void onInitializeClient() {
        FabricLoader.getInstance().getModContainer(PlutosCoffee.MOD_ID).ifPresent(container ->
                ResourceManagerHelper.registerBuiltinResourcePack(new ResourceLocation(PlutosCoffee.MOD_ID, "dark_gui"), container, Component.translatable("pack.plutoscoffee.dark_gui"),ResourcePackActivationType.NORMAL));

        ModPacketsS2C.register();

        MenuScreens.register(ModScreens.BREWER_MENU_TYPE, CoffeeBrewerScreen::new);
        MenuScreens.register(ModScreens.GRINDR_SCREEN_HANDLER_TYPE, CoffeeGrindrScreen::new);
        MenuScreens.register(ModScreens.ESPRESSO_SCREEN_HANDLER_TYPE, EspressoMachineScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COFFEE_CROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COFFEE_BREWER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COFFEE_GRINDR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ESPRESSO_MACHINE, RenderType.cutout());

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : CoffeeUtil.getCoffeeColour(stack), ModItems.BREWED_COFFEE);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : CoffeeUtil.getLatteColour(stack), ModItems.LATTE);

        CONFIG = new CoffeeConfig();
    }

}
