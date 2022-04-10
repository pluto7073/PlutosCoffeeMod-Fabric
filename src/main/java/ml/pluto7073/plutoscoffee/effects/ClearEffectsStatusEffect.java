package ml.pluto7073.plutoscoffee.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClearEffectsStatusEffect extends StatusEffect {

    public ClearEffectsStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xFFFFFF);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        Map<StatusEffect, StatusEffectInstance> effects = entity.getActiveStatusEffects();
        List<StatusEffect> effectsToRemove = new ArrayList<>();
        effects.forEach((effect, instance) -> {
            if (effect instanceof ClearEffectsStatusEffect) {
                return;
            }
            effectsToRemove.add(effect);
        });
        for (StatusEffect e : effectsToRemove) {
            entity.removeStatusEffect(e);
        }
    }

}
