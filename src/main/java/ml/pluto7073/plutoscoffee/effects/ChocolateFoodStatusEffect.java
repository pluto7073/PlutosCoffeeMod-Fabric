package ml.pluto7073.plutoscoffee.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class ChocolateFoodStatusEffect extends StatusEffect {

    public ChocolateFoodStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x1a1108);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if (entity instanceof PlayerEntity) {
            ((PlayerEntity) entity).getHungerManager().add(6, 5.0f);
        }
    }

}
