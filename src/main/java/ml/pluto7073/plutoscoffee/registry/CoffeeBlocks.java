package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.blocks.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class CoffeeBlocks {

    public static final Block COFFEE_CROP = new CoffeeCrop(Properties.of().mapColor(MapColor.COLOR_GREEN).noCollission().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY));
    public static final Block COFFEE_BREWER = new CoffeeBrewerBlock(Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(0.5F).lightLevel((state) -> 1).noOcclusion());
    public static final Block COFFEE_GRINDR = new CoffeeGrindrBlock(Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(0.5F).lightLevel((state) -> 1).noOcclusion());
    public static final Block ESPRESSO_MACHINE = new EspressoMachineBlock(Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(0.5F).lightLevel((state) -> 1).noOcclusion());

    public static final BlockEntityType<CoffeeBrewerBlockEntity> COFFEE_BREWER_BLOCK_ENTITY_TYPE = create(BlockEntityType.Builder.of(CoffeeBrewerBlockEntity::new, COFFEE_BREWER));
    public static final BlockEntityType<CoffeeGrindrBlockEntity> COFFEE_GRINDR_BLOCK_ENTITY_TYPE = create(BlockEntityType.Builder.of(CoffeeGrindrBlockEntity::new, COFFEE_GRINDR));
    public static final BlockEntityType<EspressoMachineBlockEntity> ESPRESSO_MACHINE_BLOCK_ENTITY_TYPE = create(BlockEntityType.Builder.of(EspressoMachineBlockEntity::new, ESPRESSO_MACHINE));

    private static void register(String id, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, PlutosCoffee.asId(id), block);
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

        FluidStorage.SIDED.registerForBlockEntity((entity, dir) -> entity.insert, ESPRESSO_MACHINE_BLOCK_ENTITY_TYPE);
        FluidStorage.SIDED.registerForBlockEntity((entity, dir) -> entity.insert, COFFEE_BREWER_BLOCK_ENTITY_TYPE);
    }
}
