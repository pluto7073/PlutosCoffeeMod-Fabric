package ml.pluto7073.plutoscoffee.mixins;

import ml.pluto7073.plutoscoffee.registry.ModMisc;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends EntityMixin {

    @Inject(at = @At("TAIL"), method = "initDataTracker")
    public void plutoscoffee_initCustomDataTrackers(CallbackInfo ci) {
        this.dataTracker.startTracking(ModMisc.PLAYER_CAFFEINE_AMOUNT, 0F);
        this.dataTracker.startTracking(ModMisc.PLAYER_ORIGINAL_CAFFEINE_AMOUNT, 0F);
        this.dataTracker.startTracking(ModMisc.PLAYER_TICKS_SINCE_LAST_CAFFEINE, 0);
    }

}
