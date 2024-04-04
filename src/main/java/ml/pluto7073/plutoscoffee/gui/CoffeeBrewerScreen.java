package ml.pluto7073.plutoscoffee.gui;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class CoffeeBrewerScreen extends AbstractContainerScreen<CoffeeBrewerMenu> {

    private static final ResourceLocation TEXTURE = PlutosCoffee.asId("textures/gui/container/coffee_brewer.png");
    private static final int[] BUBBLE_PROGRESS = {29, 24, 20, 16, 11, 6, 0};

    public CoffeeBrewerScreen(CoffeeBrewerMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int k = menu.getFuel();
        int l = Mth.clamp((18 * k + 1000 - 1) / 1000, 0, 18);
        if (l > 0) {
            graphics.blit(TEXTURE, i + 60, j + 44, 176, 29, l, 4);
        }

        int m = menu.getBrewTime();
        if (m > 0) {
            int n = (int) (28.0F * (1.0F - (float) m / 600.0F));
            if (n > 0) {
                graphics.blit(TEXTURE, i + 97, j + 16, 176, 0, 9, n);
            }

            n = BUBBLE_PROGRESS[m / 2 % 7];
            if (n > 0) {
                graphics.blit(TEXTURE, i + 63, j + 14 + 29 - n, 185, 29 - n, 12, n);
            }
        }
    }

}
