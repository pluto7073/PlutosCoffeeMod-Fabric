package ml.pluto7073.plutoscoffee.gui;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class EspressoMachineScreen extends AbstractContainerScreen<EspressoMachineMenu> {

    private static final ResourceLocation TEXTURE = PlutosCoffee.asId("textures/gui/container/espresso_machine.png");
    private static final int[] BUBBLE_PROGRESS = {29, 24, 20, 16, 11, 6, 0};

    public EspressoMachineScreen(EspressoMachineMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        context.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int k = menu.getWater();
        int l = Mth.clamp((18 * k + 24 - 1) / 24, 0, 18);
        if (l > 0) {
            context.blit(TEXTURE, i + 60, j + 44, 176, 29, l, 4);
        }

        int m = menu.getPullTime();
        if (m > 0) {
            int n = (int) (28.0F * (1.0F - m / 400.0F));
            if (n > 0) {
                context.blit(TEXTURE, i + 97, j + 16, 176, 0, 9, n);
            }

            n = BUBBLE_PROGRESS[m / 2 % 7];
            if (n > 0) {
                context.blit(TEXTURE, i + 63, j + 14 + 29 - n, 185, 29 - n, 12, n);
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
                int tempU = switch (temp) {
                    case 0 -> 181;
                    case 1 -> 186;
                    case 2 -> 176;
                    default -> 191;
                };
                context.blit(TEXTURE, i + 128, j + 52 - n, tempU, 33, 5, n);
            }

            n = BUBBLE_PROGRESS[(600 - m) / 2 % 7];
            if (n > 0) {
                context.blit(TEXTURE, i + 138, j + 44 + 29 - n, 185, 29 - n, 12, n);
            }
        }
    }

}
