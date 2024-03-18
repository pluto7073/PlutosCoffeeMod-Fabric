package ml.pluto7073.plutoscoffee.registry;

import com.google.common.collect.ImmutableSet;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;

public class ModPointOfInterests {

    public static final RegistryKey<PointOfInterestType> COFFEE_WORKSTATION;

    static {
        COFFEE_WORKSTATION = create("coffee_workstation", 1, 1, ModBlocks.COFFEE_WORKSTATION);
    }

    private static RegistryKey<PointOfInterestType> create(String name, int ticketCount, int searchDistance, Block block) {
        RegistryKey<PointOfInterestType> key =  RegistryKey.of(Registries.POINT_OF_INTEREST_TYPE.getKey(), PlutosCoffee.asId(name));
        PointOfInterestHelper.register(PlutosCoffee.asId(name), ticketCount, searchDistance, ImmutableSet.copyOf(block.getStateManager().getStates()));
        return key;
    }

    public static void init() {
        PlutosCoffee.logger.info("Coffee Workstation POI Registered");
    }

    private static PointOfInterestType get(RegistryKey<PointOfInterestType> key) {
        return Registries.POINT_OF_INTEREST_TYPE.get(key);
    }

}
