package ml.pluto7073.plutoscoffee.gui;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.registry.ModGuiTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CoffeeGrindrScreen extends AbstractContainerScreen<CoffeeGrindrMenu> {

    public CoffeeGrindrScreen(CoffeeGrindrMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        //context.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        ModGuiTextures.GRINDR.render(context, i, j);

        int m = menu.getGrindTime();
        if (m > 0) {
            int n = (int) (28.0F * (1.0F - (float) m / 20.0F));
            if (n > 0) {
                //context.blit(TEXTURE, i + 97, j + 16, 176, 0, 9, n);
                ModGuiTextures.PROGRESS_ARROW.renderOnMenu(context, i + 97, j + 16, 9, n);
            }
        }
    }

}
