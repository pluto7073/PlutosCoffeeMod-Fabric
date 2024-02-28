package ml.pluto7073.plutoscoffee;

import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class Client implements ClientModInitializer {

    public void onInitializeClient() {
        FabricLoader.getInstance().getModContainer(PlutosCoffee.MOD_ID).ifPresent(container ->
                ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(PlutosCoffee.MOD_ID, "dark_gui"), container, ResourcePackActivationType.NORMAL));

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COFFEE_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COFFEE_BREWER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COFFEE_GRINDR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ESPRESSO_MACHINE, RenderLayer.getCutout());
    }

}
