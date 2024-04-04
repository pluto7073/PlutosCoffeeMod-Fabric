package ml.pluto7073.plutoscoffee.coffee;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;

public record MachineWaterSource(Ingredient ingredient, int waterAmount) {

    public static MachineWaterSource read(FriendlyByteBuf buf) {
        return new MachineWaterSource(Ingredient.fromNetwork(buf), buf.readInt());
    }

    public static void write(FriendlyByteBuf buf, MachineWaterSource source) {
        source.ingredient.toNetwork(buf);
        buf.writeInt(source.waterAmount);
    }

}
