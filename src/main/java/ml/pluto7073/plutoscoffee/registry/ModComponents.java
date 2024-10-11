package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.coffee.CoffeeType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public class ModComponents {

    public static final DataComponentType<CoffeeType> COFFEE_TYPE = coffeeType(builder ->
            builder.persistent(CoffeeType.BY_ID_CODEC).networkSynchronized(CoffeeType.BY_ID_STREAM_CODEC));

    private static <T> DataComponentType<T> coffeeType(UnaryOperator<DataComponentType.Builder<T>> operator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, PlutosCoffee.asId("coffee_type"), (operator.apply(DataComponentType.builder())).build());
    }

    public static void init() {}

}
