package ml.pluto7073.plutoscoffee.registry;

import com.mojang.blaze3d.systems.RenderSystem;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public enum ModGuiTextures {

    CAFFEINE_DISPLAY_OUTLINE(0, 0, 80, 8),
    CAFFEINE_DISPLAY_FILL(0, 8, 80, 8),
    BREWER("container/coffee_brewer", 176, 166),
    PROGRESS_ARROW("container/coffee_brewer", 176, 0, 9, 28),
    PROGRESS_OUTLINE("container/coffee_brewer", 97, 16, 9, 28),
    PROGRESS_BUBBLE("container/coffee_brewer", 185, 29, 9, 29),
    WATER("container/coffee_brewer", 176, 29, 18, 4),
    GRINDR("container/coffee_grinder", 176, 166),
    ESPRESSO_MACHINE("container/espresso_machine", 176, 166),
    STEAM_COLD("container/espresso_machine", 181, 33, 5, 32),
    STEAM_HOT("container/espresso_machine", 186, 33, 5, 32),
    STEAM_BURNT("container/espresso_machine", 176, 33, 5, 32);

    public final ResourceLocation id;
    public final int width, height;
    public final int startX, startY;
    public final int fullWidth, fullHeight;

    ModGuiTextures(String id, int w, int h) {
        this(id, 0, 0, w, h);
    }

    ModGuiTextures(String id, int startX, int startY, int width, int height) {
        this(id, startX, startY, width, height, width, height);
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
        graphics.blit(id, x, y, startX, startY, width, height);
    }

    @Environment(EnvType.CLIENT)
    public void renderOnMenu(GuiGraphics graphics, int x, int y, int w, int h) {
        graphics.blit(id, x, y, startX, startY, w, h);
    }

    @Environment(EnvType.CLIENT)
    public void renderCustomUV(GuiGraphics graphics, int x, int y, int u, int v, int w, int h) {
        graphics.blit(id, x, y, u, v, w, h);
    }

    @Environment(EnvType.CLIENT)
    public void renderSection(GuiGraphics graphics, int x, int y, int w, int h) {
        graphics.blit(id, x, y, startX, startY, w, h, fullWidth, fullHeight);
    }

}
