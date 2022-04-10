package ml.pluto7073.plutoscoffee.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class CoffeeCaffeineHigh extends StatusEffect {

    public CoffeeCaffeineHigh() {
        super(StatusEffectCategory.NEUTRAL, 0x291e14);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        int time = entity.getActiveStatusEffects().get(this).getDuration();
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, time));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, time));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, time));
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 15));
    }

}
