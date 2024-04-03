package ml.pluto7073.plutoscoffee.compat.rei.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

public class EspressoMachineWidget extends CoffeeWidget {

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrices = graphics.pose();
        matrices.pushPose();
        matrices.translate(xOffset, yOffset, 0);
    }
}
