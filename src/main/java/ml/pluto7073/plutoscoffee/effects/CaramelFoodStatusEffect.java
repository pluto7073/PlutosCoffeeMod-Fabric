package ml.pluto7073.plutoscoffee.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class CaramelFoodStatusEffect extends StatusEffect {

    public CaramelFoodStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x73471e);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        int time = entity.getActiveStatusEffects().get(this).getDuration();
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, time * 2, 5));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, time, 5));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, (int) (time * 0.5f), 5));
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 60));
    }

}
