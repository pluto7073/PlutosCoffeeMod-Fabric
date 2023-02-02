package ml.pluto7073.plutoscoffee.registry;

import com.mojang.datafixers.types.Type;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.blocks.CoffeeBrewerBlock;
import ml.pluto7073.plutoscoffee.blocks.CoffeeBrewerBlockEntity;
import ml.pluto7073.plutoscoffee.blocks.CoffeeCrop;
import ml.pluto7073.plutoscoffee.blocks.CoffeeWorkstationBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static Block COFFEE_CROP;
    public static Block COFFEE_WORKSTATION;
    public static Block COFFEE_BREWER;

    public static BlockEntityType<CoffeeBrewerBlockEntity> COFFEE_BREWER_BLOCK_ENTITY_TYPE;

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(PlutosCoffee.MOD_ID, id), block);
    }

    private static <T extends BlockEntity> BlockEntityType<T> create(String id, BlockEntityType.Builder<T> builder) {
        Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, id);
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, id, builder.build(type));
    }

    public static void init() {
        COFFEE_CROP = register("coffee_plant", new CoffeeCrop(AbstractBlock.Settings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).nonOpaque()));
        COFFEE_BREWER = register("coffee_brewer", new CoffeeBrewerBlock(AbstractBlock.Settings.of(Material.METAL).requiresTool().strength(0.5F).luminance((state) -> 1).nonOpaque()));
        COFFEE_WORKSTATION = register("coffee_workstation", new CoffeeWorkstationBlock(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD)));
        COFFEE_BREWER_BLOCK_ENTITY_TYPE = create("coffee_brewer", BlockEntityType.Builder.create(CoffeeBrewerBlockEntity::new, COFFEE_BREWER));
    }
}
