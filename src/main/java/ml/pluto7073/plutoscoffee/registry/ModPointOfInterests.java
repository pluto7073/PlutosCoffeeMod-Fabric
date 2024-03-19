package ml.pluto7073.plutoscoffee.registry;

import com.google.common.collect.ImmutableSet;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.poi.PointOfInterestType;

public class ModPointOfInterests {

    public static final RegistryKey<PointOfInterestType> ESPRESSO_MACHINE;

    static {
        ESPRESSO_MACHINE = create("espresso_machine", 1, 1, ModBlocks.ESPRESSO_MACHINE);
    }

    private static RegistryKey<PointOfInterestType> create(String name, int ticketCount, int searchDistance, Block block) {
        RegistryKey<PointOfInterestType> key =  RegistryKey.of(Registries.POINT_OF_INTEREST_TYPE.getKey(), PlutosCoffee.asId(name));
        PointOfInterestHelper.register(PlutosCoffee.asId(name), ticketCount, searchDistance, ImmutableSet.copyOf(block.getStateManager().getStates()));
        return key;
    }

    public static void init() {
        PlutosCoffee.logger.info("Espresso Machine POI Registered");
    }

    private static PointOfInterestType get(RegistryKey<PointOfInterestType> key) {
        return Registries.POINT_OF_INTEREST_TYPE.get(key);
    }

}
