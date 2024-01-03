package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.entity.effect.CaffeineOverdoseEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModStatusEffects {

    public static final StatusEffect CAFFEINE_OVERDOSE = new CaffeineOverdoseEffect(StatusEffectCategory.HARMFUL, 0x0b1428);

    private static void register(String id, StatusEffect effect) {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(PlutosCoffee.MOD_ID, id), effect);
    }

    public static void init() {
        register("caffeine_overdose", CAFFEINE_OVERDOSE);
    }

}
