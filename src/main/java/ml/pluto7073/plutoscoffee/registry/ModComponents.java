package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.coffee.CoffeeType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public class ModComponents {

    public static final DataComponentType<CoffeeType> COFFEE_TYPE = register("coffee_type", builder ->
            builder.persistent(CoffeeType.COMPONENT_CODEC).networkSynchronized(CoffeeType.STREAM_COMPONENT_CODEC));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, PlutosCoffee.asId(id), (operator.apply(DataComponentType.builder())).build());
    }

}
