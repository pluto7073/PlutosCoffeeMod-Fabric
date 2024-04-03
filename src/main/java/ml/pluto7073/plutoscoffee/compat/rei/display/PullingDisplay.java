package ml.pluto7073.plutoscoffee.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import ml.pluto7073.plutoscoffee.compat.rei.CoffeeREI;
import ml.pluto7073.plutoscoffee.recipe.PullingRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PullingDisplay extends BasicDisplay {

    private int groundsRequired, pullTime;

    public PullingDisplay(PullingRecipe recipe) {
        this(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getResultItem())),
                recipe, recipe.groundsRequired, recipe.pullTime);
    }

    public PullingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, CompoundTag tag) {
        this(inputs, outputs, RecipeManagerContext.getInstance().byId(tag, "location"), tag.getInt("groundsRequired"), tag.getInt("pullTime"));
    }

    public PullingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Recipe<?> recipe, int groundsRequired, int pullTime) {
        super(inputs, outputs, Optional.ofNullable(recipe).map(Recipe::getId));
        this.groundsRequired = groundsRequired;
        this.pullTime = pullTime;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CoffeeREI.PULLING_DISPLAY;
    }

    public int getGroundsRequired() {
        return groundsRequired;
    }

    public int getPullTime() {
        return pullTime;
    }

}
