package ml.pluto7073.plutoscoffee.compat.rei.category;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.DrawableConsumer;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSources;
import ml.pluto7073.plutoscoffee.compat.rei.CoffeeREI;
import ml.pluto7073.plutoscoffee.compat.rei.display.PullingDisplay;
import ml.pluto7073.plutoscoffee.registry.ModGuiTextures;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class PullingCategory implements DisplayCategory<PullingDisplay> {

    @Override
    public List<Widget> setupDisplay(PullingDisplay display, Rectangle bounds) {
        ArrayList<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        Point origin = new Point(bounds.getCenterX() - 41, bounds.getMinY() - 16);

        widgets.add(Widgets.createArrow(new Point(origin.x + 27, origin.y + 91)));

        widgets.add(Widgets.createSlot(
                new Point(origin.x - 6, origin.y + 40))
                .markInput().entries(EntryIngredients.ofIngredient(MachineWaterSources.asIngredient()))
        );
        widgets.add(Widgets.createSlot(
                new Point(origin.x + 14, origin.y + 40))
                .markInput().entries(display.getInputEntries().get(0))
        );
        widgets.add(Widgets.createSlot(
                new Point(origin.x + 2, origin.y + 91))
                .markInput().entries(display.getInputEntries().get(1))
        );
        widgets.add(Widgets.createResultSlotBackground(new Point(origin.x + 61, origin.y + 91)));
        widgets.add(Widgets.createSlot(
                new Point(origin.x + 61, origin.y + 91)).disableBackground()
                .markOutput().entries(display.getOutputEntries().get(0))
        );
        widgets.add(Widgets.createTooltip(new Rectangle(origin.x + 7, origin.y + 58, 9, 28), Component.translatable("category.pulling.pullTime.tooltip", display.getPullTime() / 20)));
        widgets.add(Widgets.createDrawableWidget(new DrawableConsumer() {
            int tick = 0;

            @Override
            public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
                int n = (int) (28.0F * (1.0F - (float) tick / display.getPullTime()));
                ModGuiTextures.PROGRESS_OUTLINE.render(graphics, origin.x + 7, origin.y + 58);
                ModGuiTextures.PROGRESS_ARROW.renderOnMenu(graphics, origin.x + 7, origin.y + 58, 9, n);
                tick--;
                if (tick <= 0) tick = display.getPullTime();
            }
        }));
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return DisplayCategory.super.getDisplayHeight() + 48;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModItems.ESPRESSO_MACHINE);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("title.category.pulling");
    }

    @Override
    public CategoryIdentifier<? extends PullingDisplay> getCategoryIdentifier() {
        return CoffeeREI.PULLING_DISPLAY;
    }
}