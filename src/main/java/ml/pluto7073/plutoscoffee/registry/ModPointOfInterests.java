package ml.pluto7073.plutoscoffee.registry;

import com.google.common.collect.ImmutableSet;
import ml.pluto7073.pdapi.block.PDBlocks;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;

public class ModPointOfInterests {

    public static final ResourceKey<PoiType> DRINK_WORKSTATION;

    static {
        DRINK_WORKSTATION = create("drink_workstation", 1, 1, PDBlocks.DRINK_WORKSTATION);
    }

    private static ResourceKey<PoiType> create(String name, int ticketCount, int searchDistance, Block block) {
        ResourceKey<PoiType> key =  ResourceKey.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE.key(), PlutosCoffee.asId(name));
        PointOfInterestHelper.register(PlutosCoffee.asId(name), ticketCount, searchDistance, ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates()));
        return key;
    }

    public static void init() {
        PlutosCoffee.logger.info("Drink Workstation POI Registered");
    }

    private static PoiType get(ResourceKey<PoiType> key) {
        return BuiltInRegistries.POINT_OF_INTEREST_TYPE.get(key);
    }

}
