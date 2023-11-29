package ml.pluto7073.plutoscoffee.mixins.client;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

    @Inject(at = @At("TAIL"), method = "renderStatusBars")
    public void plutoscoffee_renderCaffeineContentDisplay(DrawContext context, CallbackInfo ci) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity == null) return;
        this.client.getProfiler().push("caffeineDisplay");

        int centerX = this.scaledWidth / 2;
        int baseYValue = this.scaledHeight - 49;

        float currentCaffeine = Math.min(3000f, Utils.getPlayerCaffeine(playerEntity));
        int scaledCaffeineOutput = Math.round(currentCaffeine * (71f/3000f));

        context.drawGuiTexture(CAFFEINE_OUTLINE_TEXTURE, centerX + 9, baseYValue, 80, 8);
        context.drawGuiTexture(CAFFEINE_FILL_TEXTURE, 80, 8, 0, 0, centerX + 9, baseYValue, scaledCaffeineOutput, 8);

        this.client.getProfiler().pop();
    }

}
