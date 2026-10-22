package ml.pluto7073.plutoscoffee.specialty;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ml.pluto7073.pdapi.specialty.SpecialtyDrinkBase;
import ml.pluto7073.pdapi.specialty.SpecialtyDrinkBaseSerializer;
import ml.pluto7073.plutoscoffee.coffee.CoffeeType;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class CoffeeBaseSerializer implements SpecialtyDrinkBaseSerializer {

    public static final MapCodec<CoffeeBase> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(CoffeeTypes.BY_ID_CODEC.fieldOf("coffee")
                    .forGetter(CoffeeBase::type))
                    .apply(instance, CoffeeBase::new));

    @Override
    public MapCodec<? extends SpecialtyDrinkBase> codec() {
        return CODEC;
    }

}
