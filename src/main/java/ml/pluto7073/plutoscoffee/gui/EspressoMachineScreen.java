package ml.pluto7073.plutoscoffee.gui;

import ml.pluto7073.plutoscoffee.registry.ModGuiTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class EspressoMachineScreen extends AbstractContainerScreen<EspressoMachineMenu> {

    private static final int[] BUBBLE_PROGRESS = {29, 24, 20, 16, 11, 6, 0};

    public EspressoMachineScreen(EspressoMachineMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
        if (mouseX >= i + 60 && mouseX <= i + 78 && mouseY >= j + 44 && mouseY <= j + 48) {
            context.renderTooltip(this.font, Component.translatable("container.machine.water_tooltip", menu.getWater() / 81), mouseX, mouseY);
        }
    }

    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        //context.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        ModGuiTextures.ESPRESSO_MACHINE.render(context, i, j);
        int k = menu.getWater();
        int l = Mth.clamp((18 * k + 81000 - 1) / 81000, 0, 18);
        if (l > 0) {
            //context.blit(TEXTURE, i + 60, j + 44, 176, 29, l, 4);
            ModGuiTextures.WATER.renderOnMenu(context, i + 60, j + 44, l, 4);
        }

        int m = menu.getPullTime();
        if (m > 0) {
            int n = (int) (28.0F * (1.0F - (float) m / menu.getTotalPullTime()));
            if (n > 0) {
                //context.blit(TEXTURE, i + 97, j + 16, 176, 0, 9, n);
                ModGuiTextures.PROGRESS_ARROW.renderOnMenu(context, i + 97, j + 16, 9, n);
            }

            n = BUBBLE_PROGRESS[m / 2 % 7];
            if (n > 0) {
                //context.blit(TEXTURE, i + 63, j + 14 + 29 - n, 185, 29 - n, 12, n);
                ModGuiTextures.PROGRESS_BUBBLE.renderCustomUV(context, i + 63, j + 43 - n, 185, 29 - n, 12, n);
            }
        }

        m = menu.getSteamTime();
        if (m > 0 && m <= 600) {
            int n = (int) (32.0F * (m / 600.0F));
            if (n > 0) {
                int temp;
                if (m < 400) temp = 0;
                else if (m < 500) temp = 1;
                else temp = 2;
                switch (temp) {
                    case 0 -> ModGuiTextures.STEAM_COLD.renderOnMenu(context, i + 128, j + 52 - n, 5, n);
                    case 1 -> ModGuiTextures.STEAM_HOT.renderOnMenu(context, i + 128, j + 52 - n, 5, n);
                    case 2 -> ModGuiTextures.STEAM_BURNT.renderOnMenu(context, i + 128, j + 52 - n, 5, n);
                };
                //context.blit(TEXTURE, i + 128, j + 52 - n, tempU, 33, 5, n);
            }

            n = BUBBLE_PROGRESS[(600 - m) / 2 % 7];
            if (n > 0) {
                ModGuiTextures.PROGRESS_BUBBLE.renderCustomUV(context, i + 138, j + 73 - n, 185, 29 - n, 12, n);
                //context.blit(TEXTURE, i + 138, j + 44 + 29 - n, 185, 29 - n, 12, n);
            }
        }
    }

}
