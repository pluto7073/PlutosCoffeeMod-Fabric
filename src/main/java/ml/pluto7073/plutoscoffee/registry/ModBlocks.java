package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.blocks.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ModBlocks {

    public static final Block COFFEE_CROP = new CoffeeCrop(FabricBlockSettings.create().mapColor(MapColor.COLOR_GREEN).noCollision().ticksRandomly().breakInstantly().sounds(SoundType.CROP).pistonBehavior(PushReaction.DESTROY));
    public static final Block COFFEE_BREWER = new CoffeeBrewerBlock(FabricBlockSettings.create().mapColor(MapColor.COLOR_GRAY).requiresTool().strength(0.5F).luminance((state) -> 1).nonOpaque());
    public static final Block COFFEE_GRINDR = new CoffeeGrindrBlock(FabricBlockSettings.create().mapColor(MapColor.COLOR_GRAY).requiresTool().strength(0.5F).luminance((state) -> 1).nonOpaque());
    public static final Block ESPRESSO_MACHINE = new EspressoMachineBlock(FabricBlockSettings.create().mapColor(MapColor.COLOR_GRAY).requiresTool().strength(0.5f).luminance(state -> 1).nonOpaque());

    public static final BlockEntityType<CoffeeBrewerBlockEntity> COFFEE_BREWER_BLOCK_ENTITY_TYPE = create(BlockEntityType.Builder.of(CoffeeBrewerBlockEntity::new, COFFEE_BREWER));
    public static final BlockEntityType<CoffeeGrindrBlockEntity> COFFEE_GRINDR_BLOCK_ENTITY_TYPE = create(BlockEntityType.Builder.of(CoffeeGrindrBlockEntity::new, COFFEE_GRINDR));
    public static final BlockEntityType<EspressoMachineBlockEntity> ESPRESSO_MACHINE_BLOCK_ENTITY_TYPE = create(BlockEntityType.Builder.of(EspressoMachineBlockEntity::new, ESPRESSO_MACHINE));

    private static void register(String id, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(PlutosCoffee.MOD_ID, id), block);
    }

    private static <T extends BlockEntity> void register(String id, BlockEntityType<T> entityType) {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, entityType);
    }

    private static <T extends BlockEntity> BlockEntityType<T> create(BlockEntityType.Builder<T> builder) {
        return builder.build(null);
    }

    public static void init() {
        register("coffee_plant", COFFEE_CROP);
        register("coffee_brewer", COFFEE_BREWER);
        register("coffee_grinder", COFFEE_GRINDR);
        register("espresso_machine", ESPRESSO_MACHINE);

        register("coffee_brewer", COFFEE_BREWER_BLOCK_ENTITY_TYPE);
        register("coffee_grinder", COFFEE_GRINDR_BLOCK_ENTITY_TYPE);
        register("espresso_machine", ESPRESSO_MACHINE_BLOCK_ENTITY_TYPE);
    }
}
