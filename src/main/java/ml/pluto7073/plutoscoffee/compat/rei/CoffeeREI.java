package ml.pluto7073.plutoscoffee.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.ButtonArea;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.compat.rei.category.PullingCategory;
import ml.pluto7073.plutoscoffee.compat.rei.display.PullingDisplay;
import ml.pluto7073.plutoscoffee.recipe.PullingRecipe;
import ml.pluto7073.plutoscoffee.registry.ModItems;

public class CoffeeREI implements REIClientPlugin {

    public static final CategoryIdentifier<PullingDisplay> PULLING_DISPLAY = CategoryIdentifier.of(PlutosCoffee.asId("pulling"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new PullingCategory());
        registry.configure(PULLING_DISPLAY, configuration -> {
            configuration.setPlusButtonArea(ButtonArea.defaultArea());
        });
        registry.addWorkstations(PULLING_DISPLAY, EntryStacks.of(ModItems.ESPRESSO_MACHINE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(PullingRecipe.class, PullingDisplay::new);
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(PULLING_DISPLAY, BasicDisplay.Serializer.ofRecipeLess(PullingDisplay::new, (display, tag) -> {
            tag.putInt("pullTime", display.getPullTime());
            tag.putInt("groundsRequired", display.getGroundsRequired());
        }));
    }
}
