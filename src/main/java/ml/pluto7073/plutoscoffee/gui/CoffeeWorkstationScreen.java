package ml.pluto7073.plutoscoffee.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CoffeeWorkstationScreen extends ForgingScreen<CoffeeWorkstationScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(PlutosCoffee.MOD_ID, "textures/gui/container/coffee_workstation.png");

    public CoffeeWorkstationScreen(CoffeeWorkstationScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title, TEXTURE);
        this.titleX = 60;
        this.titleY = 18;
    }

    protected void drawInvalidRecipeArrow(DrawContext context, int x, int y) {
        if (this.hasInvalidRecipe()) {
            context.drawTexture(TEXTURE, x + 65, y + 46, this.backgroundWidth, 0, 28, 21);
        }
    }

    private boolean hasInvalidRecipe() {
        return (this.handler).getSlot(0).hasStack() && (this.handler).getSlot(1).hasStack() && !(this.handler).getSlot((this.handler).getResultSlotIndex()).hasStack();
    }

}
