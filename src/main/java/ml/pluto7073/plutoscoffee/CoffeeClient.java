package ml.pluto7073.plutoscoffee;

import ml.pluto7073.pdapi.client.PDClientConfig;
import ml.pluto7073.pdapi.util.ColorUtil;
import ml.pluto7073.plutoscoffee.config.CoffeeConfig;
import ml.pluto7073.plutoscoffee.gui.CoffeeBrewerScreen;
import ml.pluto7073.plutoscoffee.gui.CoffeeGrindrScreen;
import ml.pluto7073.plutoscoffee.gui.EspressoMachineScreen;
import ml.pluto7073.plutoscoffee.items.BrewedCoffee;
import ml.pluto7073.plutoscoffee.registry.CoffeeBlocks;
import ml.pluto7073.plutoscoffee.registry.CoffeeItems;
import ml.pluto7073.plutoscoffee.registry.CoffeeMenus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class CoffeeClient implements ClientModInitializer {

    public void onInitializeClient() {
        FabricLoader.getInstance().getModContainer(PlutosCoffee.MOD_ID).ifPresent(container ->
                ResourceManagerHelper.registerBuiltinResourcePack(PlutosCoffee.asId("dark_gui"), container, Component.translatable("pack.plutoscoffee.dark_gui"),ResourcePackActivationType.NORMAL));

        MenuScreens.register(CoffeeMenus.BREWER_MENU_TYPE, CoffeeBrewerScreen::new);
        MenuScreens.register(CoffeeMenus.GRINDR_SCREEN_HANDLER_TYPE, CoffeeGrindrScreen::new);
        MenuScreens.register(CoffeeMenus.ESPRESSO_SCREEN_HANDLER_TYPE, EspressoMachineScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(CoffeeBlocks.COFFEE_CROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CoffeeBlocks.COFFEE_BREWER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CoffeeBlocks.COFFEE_GRINDR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CoffeeBlocks.ESPRESSO_MACHINE, RenderType.cutout());

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ColorUtil.getColorForDrinkWithDefault(stack, BrewedCoffee.DEFAULT_COLOUR, 2), CoffeeItems.BREWED_COFFEE);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ColorUtil.getColorForDrinkWithDefault(stack, 0xFFFFFF, 3), CoffeeItems.LATTE);

        PDClientConfig.INSTANCE.addManagedConfig(CoffeeConfig.INSTANCE);
    }

}
