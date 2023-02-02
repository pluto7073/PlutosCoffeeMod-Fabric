package ml.pluto7073.plutoscoffee.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CoffeeWorkstationScreen extends ForgingScreen<CoffeeWorkstationScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(PlutosCoffee.MOD_ID, "textures/gui/container/coffee_workstation.png");

    public CoffeeWorkstationScreen(CoffeeWorkstationScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title, TEXTURE);
        this.titleX = 60;
        this.titleY = 18;
    }

    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        RenderSystem.disableBlend();
        super.drawForeground(matrices, mouseX, mouseY);
    }

}
