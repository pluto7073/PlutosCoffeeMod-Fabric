package ml.pluto7073.plutoscoffee.coffee;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

public record MachineWaterSource(Ingredient ingredient, int waterAmount) {

    public static final Codec<MachineWaterSource> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Ingredient.CODEC.fieldOf("source").forGetter(MachineWaterSource::ingredient),
                    Codec.INT.fieldOf("water").forGetter(MachineWaterSource::waterAmount))
                    .apply(instance, MachineWaterSource::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MachineWaterSource> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, MachineWaterSource::ingredient,
            ByteBufCodecs.INT, MachineWaterSource::waterAmount,
            MachineWaterSource::new
    );

}
