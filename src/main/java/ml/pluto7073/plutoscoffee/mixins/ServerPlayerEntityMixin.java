package ml.pluto7073.plutoscoffee.mixins;

import ml.pluto7073.pdapi.entity.PDTrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "tick")
    public void plutoscoffee$caffeineLevelEffects(CallbackInfo ci) {
        float caffeine = this.getDataTracker().get(PDTrackedData.PLAYER_CAFFEINE_AMOUNT);

        if (caffeine >= 100) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600));
        } if (caffeine >= 150) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 600));
        } if (caffeine >= 450) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 1));
        } if (caffeine >= 500) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 600));
        } if (caffeine >= 600) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 600, 1));
        } if (caffeine >= 700) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 600, 1));
        }
    }

}
