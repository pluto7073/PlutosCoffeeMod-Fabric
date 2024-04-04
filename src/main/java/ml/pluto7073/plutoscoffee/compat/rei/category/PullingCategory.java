package ml.pluto7073.plutoscoffee.compat.rei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSources;
import ml.pluto7073.plutoscoffee.compat.rei.CoffeeREI;
import ml.pluto7073.plutoscoffee.compat.rei.ItemIcon;
import ml.pluto7073.plutoscoffee.compat.rei.display.PullingDisplay;
import ml.pluto7073.plutoscoffee.registry.ModGuiTextures;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class PullingCategory implements DisplayCategory<PullingDisplay> {

    @Override
    public List<Widget> setupDisplay(PullingDisplay display, Rectangle bounds) {
        ArrayList<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        Point origin = new Point(bounds.getCenterX() - 41, bounds.getMinY());

        widgets.add(Widgets.createArrow(new Point(origin.x + 27, origin.y + 91)));

        widgets.add(Widgets.createSlot(
                new Point(origin.x - 6, origin.y + 50))
                .markInput().entries(EntryIngredients.ofIngredient(MachineWaterSources.asIngredient()))
        );
        widgets.add(Widgets.createSlot(
                new Point(origin.x + 14, origin.y + 50))
                .markInput().entries(display.getInputEntries().get(0))
        );
        widgets.add(Widgets.createSlot(
                new Point(origin.x + 4, origin.y + 91))
                .markInput().entries(display.getInputEntries().get(1))
        );
        widgets.add(Widgets.createResultSlotBackground(new Point(origin.x + 61, origin.y + 91)));
        widgets.add(Widgets.createSlot(
                new Point(origin.x + 61, origin.y + 91)).disableBackground()
                .markOutput().entries(display.getOutputEntries().get(0))
        );
        widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            ModGuiTextures.PROGRESS_ARROW.render(graphics, origin.x + 9, origin.y + 68);
        }));
        return widgets;
        // TODO finish
    }

    @Override
    public int getDisplayHeight() {
        return DisplayCategory.super.getDisplayHeight() + 54;
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