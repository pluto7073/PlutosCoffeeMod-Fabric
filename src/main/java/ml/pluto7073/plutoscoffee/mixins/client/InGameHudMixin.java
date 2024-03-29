package ml.pluto7073.plutoscoffee.mixins.client;

import ml.pluto7073.plutoscoffee.Client;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique private static final Identifier CAFFEINE_OUTLINE_TEXTURE = new Identifier(PlutosCoffee.MOD_ID, "hud/caffeine_content_outline");
    @Unique private static final Identifier CAFFEINE_FILL_TEXTURE = new Identifier(PlutosCoffee.MOD_ID, "hud/caffeine_content_fill");

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow protected abstract LivingEntity getRiddenEntity();

    @Inject(at = @At("TAIL"), method = "renderStatusBars")
    public void plutoscoffee_renderCaffeineContentDisplay(DrawContext context, CallbackInfo ci) {
        if (!Client.CONFIG.shouldShowCoffeeBar()) return;
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity == null) return;
        this.client.getProfiler().push("caffeineDisplay");

        int centerX = this.scaledWidth / 2;
        int baseYValue = this.scaledHeight - 49;

        int max = playerEntity.getMaxAir();
        int maxOrCurrent = Math.min(playerEntity.getAir(), max);

        if (playerEntity.isSubmergedIn(FluidTags.WATER) || maxOrCurrent < max) {
            baseYValue -= 10;
        }

        LivingEntity ridden = this.getRiddenEntity();

        if (FabricLoader.getInstance().isModLoaded("dehydration") && !playerEntity.isCreative() && ridden == null) {
            baseYValue -= 10;
        }

        if (ridden != null) {
            if (ridden.getHealth() > 0){
                int height = Utils.calculateHealthBarHeightPixels((int) ridden.getHealth(), 10, 10);
                baseYValue -= height;
            }
        }

        float currentCaffeine = Math.min(3000f, Utils.getPlayerCaffeine(playerEntity));
        int scaledCaffeineOutput = Math.round(currentCaffeine * (71f/3000f));

        context.drawGuiTexture(CAFFEINE_OUTLINE_TEXTURE, centerX + 10, baseYValue, 80, 8);
        context.drawGuiTexture(CAFFEINE_FILL_TEXTURE, 80, 8, 0, 0, centerX + 10, baseYValue, scaledCaffeineOutput, 8);

        this.client.getProfiler().pop();
    }

}
