package ml.pluto7073.plutoscoffee.coffee;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

public record MachineWaterSource(Ingredient ingredient, int waterAmount) {

    public static final StreamCodec<RegistryFriendlyByteBuf, MachineWaterSource> STREAM_CODEC =
            StreamCodec.of(MachineWaterSource::write, MachineWaterSource::read);

    public static MachineWaterSource read(RegistryFriendlyByteBuf buf) {
        return new MachineWaterSource(Ingredient.CONTENTS_STREAM_CODEC.decode(buf), buf.readInt());
    }

    public static void write(RegistryFriendlyByteBuf buf, MachineWaterSource source) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, source.ingredient);
        buf.writeInt(source.waterAmount);
    }

}
