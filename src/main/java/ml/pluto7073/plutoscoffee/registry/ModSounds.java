package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    public static final SoundEvent COFFEE_GRINDER = SoundEvent.createVariableRangeEvent(PlutosCoffee.asId("block.coffee_grinder.grind"));

    public static void init() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, PlutosCoffee.asId("block.coffee_grinder.grind"), COFFEE_GRINDER);
    }

}
