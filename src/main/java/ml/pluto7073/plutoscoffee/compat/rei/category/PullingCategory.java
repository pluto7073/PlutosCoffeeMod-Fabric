package ml.pluto7073.plutoscoffee.compat.rei.category;

import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import ml.pluto7073.plutoscoffee.compat.rei.ItemIcon;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class PullingCategory implements DisplayCategory {
    @Override
    public CategoryIdentifier getCategoryIdentifier() {
        return null;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("title.category.pulling");
    }

    @Override
    public Renderer getIcon() {
        return new ItemIcon(() -> new ItemStack(ModItems.ESPRESSO_MACHINE));
    }

}