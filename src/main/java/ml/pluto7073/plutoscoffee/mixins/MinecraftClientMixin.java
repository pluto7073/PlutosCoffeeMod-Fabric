package ml.pluto7073.plutoscoffee.mixins;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(at = @At("HEAD"), method = "tick")
    public void onFirstTick(CallbackInfo ci) {
        PlutosCoffee.MOD.loadLater();
    }

}
