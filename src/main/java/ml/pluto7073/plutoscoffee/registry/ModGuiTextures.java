package ml.pluto7073.plutoscoffee.registry;

import com.mojang.blaze3d.systems.RenderSystem;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;

public enum ModGuiTextures {

    CAFFEINE_DISPLAY_OUTLINE(0, 0, 80, 8),
    CAFFEINE_DISPLAY_FILL(0, 8, 80, 8)

    ;

    public final ResourceLocation id;
    public int width, height;
    public int startX, startY;
    public int fullWidth, fullHeight;

    ModGuiTextures(String id, int width, int height) {
        this(id, 0, 0, width, height, width, height);
    }

    ModGuiTextures(int startX, int startY, int width, int height) {
        this("pc_icons", startX, startY, width, height, 80, 16);
    }

    ModGuiTextures(String id, int startX, int startY, int width, int height, int fullWidth, int fullHeight) {
        this.id = PlutosCoffee.asId("textures/gui/" + id + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
        this.fullHeight = fullHeight;
        this.fullWidth = fullWidth;
    }

    @Environment(EnvType.CLIENT)
    public void bind() {
        RenderSystem.setShaderTexture(0, id);
    }

    @Environment(EnvType.CLIENT)
    public void render(GuiGraphics graphics, int x, int y) {
        renderSection(graphics, x, y, width, height);
    }

    @Environment(EnvType.CLIENT)
    public void renderSection(GuiGraphics graphics, int x, int y, int w, int h) {
        if (fullWidth != w || fullHeight != h) {
            graphics.blit(id, x, y, startX, startY, w, h, fullWidth, fullHeight);
            return;
        }
        graphics.blit(id, x, y, startX, startY, w, h);
    }

}
