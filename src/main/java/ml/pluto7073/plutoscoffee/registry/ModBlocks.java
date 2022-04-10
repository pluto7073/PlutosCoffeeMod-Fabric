package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.blocks.CoffeeCrop;
import ml.pluto7073.plutoscoffee.blocks.CoffeeMachineBlock;
import ml.pluto7073.plutoscoffee.blocks.CoffeeMachineBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block COFFEE_CROP = new CoffeeCrop();
    public static final BlockEntityType COFFEE_MACHINE_BLOCK_ENTITY;
    public static final Block COFFEE_MACHINE = new CoffeeMachineBlock(FabricBlockSettings.of(Material.METAL));

    static {
        COFFEE_MACHINE_BLOCK_ENTITY =
                Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(PlutosCoffee.MOD_ID, "coffee_machine"),
                        BlockEntityType.Builder.create(CoffeeMachineBlockEntity::new, COFFEE_MACHINE).build(null));
    }

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(PlutosCoffee.MOD_ID, "coffee_plant"), COFFEE_CROP);
        Registry.register(Registry.BLOCK, new Identifier(PlutosCoffee.MOD_ID, "coffee_machine"), COFFEE_MACHINE);
    }
}
