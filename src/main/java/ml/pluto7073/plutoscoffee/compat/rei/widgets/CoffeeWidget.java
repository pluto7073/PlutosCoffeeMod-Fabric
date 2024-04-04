package ml.pluto7073.plutoscoffee.compat.rei.widgets;

import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@MethodsReturnNonnullByDefault
public class CoffeeWidget extends Widget {

    private Point pos;

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Point getPos() {
        return pos;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        draw(guiGraphics, pos.getX(), pos.getY());
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return Lists.newArrayList();
    }

    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {}



}
