package com.pluto7073.plutoscoffee.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class CaramelCrop extends Block {
    public CaramelCrop() {
        super(FabricBlockSettings.of(Material.PLANT).breakByHand(true).strength(0.0f, 0.0f).sounds(BlockSoundGroup.GRASS).nonOpaque().noCollision());
    }
}
