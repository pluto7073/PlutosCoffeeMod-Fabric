package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.blocks.CoffeeCrop;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block COFFEE_CROP = new CoffeeCrop();

    static {

    }

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(PlutosCoffee.MOD_ID, "coffee_plant"), COFFEE_CROP);
    }
}
