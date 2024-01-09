package ml.pluto7073.plutoscoffee.gui;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CoffeeGrindrScreen extends HandledScreen<CoffeeGrindrScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(PlutosCoffee.MOD_ID, "textures/gui/container/coffee_grinder.png");

    public CoffeeGrindrScreen(CoffeeGrindrScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

        int m = handler.getGrindTime();
        if (m > 0) {
            int n = (int) (28.0F * (1.0F - (float) m / 20.0F));
            if (n > 0) {
                context.drawTexture(TEXTURE, i + 97, j + 16, 176, 0, 9, n);
            }
        }
    }

}
