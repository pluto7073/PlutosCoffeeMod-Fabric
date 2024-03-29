package ml.pluto7073.plutoscoffee.mixins;

import ml.pluto7073.plutoscoffee.mixins.EntityMixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);
}
