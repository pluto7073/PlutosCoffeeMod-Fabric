package ml.pluto7073.plutoscoffee.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import ml.pluto7073.plutoscoffee.compat.rei.CoffeeREI;
import ml.pluto7073.plutoscoffee.recipe.PullingRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PullingDisplay extends BasicDisplay {

    private final int groundsRequired;
    private final int pullTime;

    public PullingDisplay(RecipeHolder<PullingRecipe> recipe) {
        this(List.of(EntryIngredients.ofIngredient(recipe.value().grounds), EntryIngredients.ofIngredient(recipe.value().base)),
                Collections.singletonList(EntryIngredients.of(recipe.value().getResultItem())),
                recipe, recipe.value().groundsRequired, recipe.value().pullTime);
    }

    public PullingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, CompoundTag tag) {
        this(inputs, outputs, RecipeManagerContext.getInstance().byId(tag, "location"), tag.getInt("groundsRequired"), tag.getInt("pullTime"));
    }

    public PullingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, RecipeHolder<?> recipe, int groundsRequired, int pullTime) {
        super(inputs, outputs, Optional.ofNullable(recipe).map(RecipeHolder::id));
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
