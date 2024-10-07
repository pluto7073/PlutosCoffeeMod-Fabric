package ml.pluto7073.plutoscoffee.mixins.client;

import ml.pluto7073.pdapi.util.DrinkUtil;
import ml.pluto7073.plutoscoffee.Client;
import ml.pluto7073.plutoscoffee.CoffeeUtil;
import ml.pluto7073.plutoscoffee.registry.ModGuiTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class InGameHudMixin {

    @Shadow @Final private Minecraft minecraft;

    @Shadow protected abstract Player getCameraPlayer();

    @Shadow private int screenWidth;

    @Shadow private int screenHeight;

    @Shadow protected abstract LivingEntity getPlayerVehicleWithHealth();

    @Inject(at = @At("HEAD"), method = "render")
    public void plutoscoffee_renderCaffeineContentDisplay(GuiGraphics graphics, float partialTick, CallbackInfo ci) {
        if (!Client.CONFIG.shouldShowCoffeeBar()) return;
        Player playerEntity = this.getCameraPlayer();
        //noinspection DataFlowIssue
        if (this.minecraft.gameMode.getPlayerMode() == GameType.CREATIVE) return;
        if (playerEntity == null) return;
        this.minecraft.getProfiler().push("caffeineDisplay");

        int centerX = this.screenWidth / 2;
        int baseYValue = this.screenHeight - 49;

        int max = playerEntity.getMaxAirSupply();
        int maxOrCurrent = Math.min(playerEntity.getAirSupply(), max);

        if (playerEntity.isEyeInFluid(FluidTags.WATER) || maxOrCurrent < max) {
            baseYValue -= 10;
        }

        LivingEntity ridden = this.getPlayerVehicleWithHealth();

        if (FabricLoader.getInstance().isModLoaded("dehydration") && !playerEntity.isCreative() && ridden == null) {
            baseYValue -= 10;
        }

        if (ridden != null) {
            if (ridden.getHealth() > 0){
                int height = CoffeeUtil.calculateHealthBarHeightPixels((int) ridden.getHealth(), 10, 10);
                baseYValue -= height;
            }
        }

        float currentCaffeine = Math.min(3000f, DrinkUtil.getPlayerCaffeine(playerEntity));
        int scaledCaffeineOutput = Math.round(currentCaffeine * (71f/3000f));
        //graphics.blit(ICONS, centerX + 10, baseYValue, 0, 0, 80, 8, 80, 16);
        //graphics.blit(ICONS, centerX + 10, baseYValue, 0, 9, scaledCaffeineOutput, 8, 80, 16);
        ModGuiTextures.CAFFEINE_DISPLAY_OUTLINE.renderSection(graphics, centerX + 10, baseYValue, 80, 8);
        ModGuiTextures.CAFFEINE_DISPLAY_FILL.renderSection(graphics, centerX + 10, baseYValue - 1, scaledCaffeineOutput, 8);

        this.minecraft.getProfiler().pop();
    }

}
