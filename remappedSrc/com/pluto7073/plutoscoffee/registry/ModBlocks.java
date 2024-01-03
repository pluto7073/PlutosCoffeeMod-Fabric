package com.pluto7073.plutoscoffee.registry;

import com.pluto7073.plutoscoffee.PlutosCoffee;
import com.pluto7073.plutoscoffee.blocks.CoffeeCrop;
import net.minecraft.block.Block;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block COFFEE_CROP = new CoffeeCrop();


    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(PlutosCoffee.MOD_ID, "coffee_plant"), COFFEE_CROP);
    }
}
