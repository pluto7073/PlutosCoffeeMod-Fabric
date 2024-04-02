package ml.pluto7073.plutoscoffee.mixins.client;


import ml.pluto7073.plutoscoffee.version.VersionChecker;
import net.minecraft.client.ResourceLoadStateTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourceLoadStateTracker.class)
public class ResourceLoadStateTrackerMixin {

    @Inject(at = @At("TAIL"), method = "finishReload")
    public void plutoscoffee_onFinishResourceReload(CallbackInfo ci) {
        VersionChecker.showWarningScreen();
    }

}
