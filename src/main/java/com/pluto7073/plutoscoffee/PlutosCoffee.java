package com.pluto7073.plutoscoffee;

import com.pluto7073.plutoscoffee.registry.ModBlocks;
import com.pluto7073.plutoscoffee.registry.ModItems;
import com.pluto7073.plutoscoffee.registry.ModPotions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class PlutosCoffee implements ModInitializer {

    public static final String MOD_ID = "plutoscoffee";


    @Override
    public void onInitialize() {
        System.out.println("Registering Blocks");
        ModBlocks.registerBlocks();
        System.out.println("Registering Items");
        ModItems.registerItems();
        System.out.println("Registering Potions");
        ModPotions.registerPotions();
    }

}
