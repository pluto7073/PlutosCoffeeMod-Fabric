package ml.pluto7073.plutoscoffee.entity.effect;

import ml.pluto7073.plutoscoffee.entity.damage.ModDamageTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class CaffeineOverdoseEffect extends StatusEffect {

    public CaffeineOverdoseEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        entity.damage(ModDamageTypes.of(entity.getWorld(), ModDamageTypes.CAFFEINE_OVERDOSE), 1.0F);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
