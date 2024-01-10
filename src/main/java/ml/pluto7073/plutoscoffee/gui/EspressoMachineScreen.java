package ml.pluto7073.plutoscoffee.gui;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class EspressoMachineScreen extends HandledScreen<EspressoMachineScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(PlutosCoffee.MOD_ID, "textures/gui/container/espresso_machine.png");
    private static final int[] BUBBLE_PROGRESS = {29, 24, 20, 16, 11, 6, 0};

    public EspressoMachineScreen(EspressoMachineScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        int k = handler.getWater();
        int l = MathHelper.clamp((18 * k + 24 - 1) / 24, 0, 18);
        if (l > 0) {
            context.drawTexture(TEXTURE, i + 60, j + 44, 176, 29, l, 4);
        }

        int m = handler.getPullTime();
        if (m > 0) {
            int n = (int) (28.0F * (1.0F - m / 400.0F));
            if (n > 0) {
                context.drawTexture(TEXTURE, i + 97, j + 16, 176, 0, 9, n);
            }

            n = BUBBLE_PROGRESS[m / 2 % 7];
            if (n > 0) {
                context.drawTexture(TEXTURE, i + 63, j + 14 + 29 - n, 185, 29 - n, 12, n);
            }
        }

        m = handler.getSteamTime();
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
                context.drawTexture(TEXTURE, i + 128, j + 52 - n, tempU, 33, 5, n);
            }

            n = BUBBLE_PROGRESS[(600 - m) / 2 % 7];
            if (n > 0) {
                context.drawTexture(TEXTURE, i + 138, j + 44 + 29 - n, 185, 29 - n, 12, n);
            }
        }
    }

}
