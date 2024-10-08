package ml.pluto7073.plutoscoffee.gui;

import ml.pluto7073.plutoscoffee.registry.ModGuiTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class CoffeeBrewerScreen extends AbstractContainerScreen<CoffeeBrewerMenu> {

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
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.renderBackground(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);
        this.renderTooltip(graphics, mouseX, mouseY);
        if (mouseX >= i + 60 && mouseX <= i + 78 && mouseY >= j + 44 && mouseY <= j + 48) {
            graphics.renderTooltip(this.font, Component.translatable("container.machine.water_tooltip", menu.getFuel() / 81), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        //graphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        ModGuiTextures.BREWER.render(graphics, i, j);
        int k = menu.getFuel();
        int l = Mth.clamp((18 * k + 81000 - 1) / 81000, 0, 18);
        if (l > 0) {
            //graphics.blit(TEXTURE, i + 60, j + 44, 176, 29, l, 4);
            ModGuiTextures.WATER.renderOnMenu(graphics, i + 60, j + 44, l, 4);
        }

        int m = menu.getBrewTime();
        if (m > 0) {
            int n = (int) (28.0F * (1.0F - (float) m / 600.0F));
            if (n > 0) {
                ModGuiTextures.PROGRESS_ARROW.renderOnMenu(graphics, i + 97, j + 16, 9, n);
                //graphics.blit(TEXTURE, i + 97, j + 16, 176, 0, 9, n);
            }

            n = BUBBLE_PROGRESS[m / 2 % 7];
            if (n > 0) {
                ModGuiTextures.PROGRESS_BUBBLE.renderCustomUV(graphics, i + 63, j + 43 - n, 185, 29 - n, 12, n);
                //graphics.blit(TEXTURE, i + 63, j + 14 + 29 - n, 185, 29 - n, 12, n);
            }
        }
    }

}
