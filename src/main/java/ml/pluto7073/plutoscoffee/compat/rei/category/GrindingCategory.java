package ml.pluto7073.plutoscoffee.compat.rei.category;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import ml.pluto7073.plutoscoffee.compat.rei.CoffeeREI;
import ml.pluto7073.plutoscoffee.compat.rei.display.GrindingDisplay;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.network.chat.Component;

import java.util.List;

public class GrindingCategory implements DisplayCategory<GrindingDisplay> {
    @Override
    public CategoryIdentifier<? extends GrindingDisplay> getCategoryIdentifier() {
        return CoffeeREI.GRINDING_DISPLAY;
    }

    @Override
    public List<Widget> setupDisplay(GrindingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5))
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5))
                .entries(display.getInputEntries().get(0)).markInput());
        return widgets;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("title.category.grinding");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModItems.COFFEE_GRINDR);
    }
}
