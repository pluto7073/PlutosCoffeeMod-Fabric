package ml.pluto7073.plutoscoffee.mixins;


import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.version.VersionChecker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.ResourceReloadLogger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourceReloadLogger.class)
public class ResourceReloadLoggerMixin {

    @Inject(at = @At("TAIL"), method = "finish")
    public void onFinishResourceReload(CallbackInfo ci) {
        VersionChecker.showWarningScreen();
    }

}
