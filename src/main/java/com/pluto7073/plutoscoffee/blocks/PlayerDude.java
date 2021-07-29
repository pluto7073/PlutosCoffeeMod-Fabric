package com.pluto7073.plutoscoffee.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class PlayerDude extends Block {
    public PlayerDude() {
        super(FabricBlockSettings.of(Material.METAL).nonOpaque().sounds(BlockSoundGroup.CHAIN).breakInstantly().noCollision());
    }

}
