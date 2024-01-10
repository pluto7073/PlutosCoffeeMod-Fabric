package ml.pluto7073.plutoscoffee.registry;

import com.mojang.datafixers.types.Type;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.blocks.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class ModBlocks {

    public static final Block COFFEE_CROP = new CoffeeCrop(AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block COFFEE_WORKSTATION = new CoffeeWorkstationBlock(AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).instrument(Instrument.BASS).strength(2.5F).sounds(BlockSoundGroup.WOOD).burnable().sounds(BlockSoundGroup.WOOD));
    public static final Block COFFEE_BREWER = new CoffeeBrewerBlock(AbstractBlock.Settings.create().mapColor(MapColor.IRON_GRAY).requiresTool().strength(0.5F).luminance((state) -> 1).nonOpaque());
    public static final Block COFFEE_GRINDR = new CoffeeGrindrBlock(AbstractBlock.Settings.create().mapColor(MapColor.IRON_GRAY).requiresTool().strength(0.5F).luminance((state) -> 1).nonOpaque());
    public static final Block ESPRESSO_MACHINE = new EspressoMachineBlock(FabricBlockSettings.create().mapColor(MapColor.IRON_GRAY).requiresTool().strength(0.5f).luminance(state -> 1).nonOpaque());

    public static final BlockEntityType<CoffeeBrewerBlockEntity> COFFEE_BREWER_BLOCK_ENTITY_TYPE = create("coffee_brewer", BlockEntityType.Builder.create(CoffeeBrewerBlockEntity::new, COFFEE_BREWER));
    public static final BlockEntityType<CoffeeGrindrBlockEntity> COFFEE_GRINDR_BLOCK_ENTITY_TYPE = create("coffee_grinder", BlockEntityType.Builder.create(CoffeeGrindrBlockEntity::new, COFFEE_GRINDR));
    public static final BlockEntityType<EspressoMachineBlockEntity> ESPRESSO_MACHINE_BLOCK_ENTITY_TYPE = create("espresso_machine", BlockEntityType.Builder.create(EspressoMachineBlockEntity::new, ESPRESSO_MACHINE));

    private static void register(String id, Block block) {
        Registry.register(Registries.BLOCK, new Identifier(PlutosCoffee.MOD_ID, id), block);
    }

    private static <T extends BlockEntity> void register(String id, BlockEntityType<T> entityType) {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, id, entityType);
    }

    private static <T extends BlockEntity> BlockEntityType<T> create(String id, BlockEntityType.Builder<T> builder) {
        Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, id);
        return builder.build(type);
    }

    public static void init() {
        register("coffee_plant", COFFEE_CROP);
        register("coffee_brewer", COFFEE_BREWER);
        register("coffee_workstation", COFFEE_WORKSTATION);
        register("coffee_grinder", COFFEE_GRINDR);
        register("espresso_machine", ESPRESSO_MACHINE);

        register("coffee_brewer", COFFEE_BREWER_BLOCK_ENTITY_TYPE);
        register("coffee_grinder", COFFEE_GRINDR_BLOCK_ENTITY_TYPE);
        register("espresso_machine", ESPRESSO_MACHINE_BLOCK_ENTITY_TYPE);
    }
}
