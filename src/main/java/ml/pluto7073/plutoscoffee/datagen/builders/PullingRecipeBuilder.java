package ml.pluto7073.plutoscoffee.datagen.builders;

import ml.pluto7073.plutoscoffee.recipe.PullingRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
public class PullingRecipeBuilder implements RecipeBuilder {

    public final Ingredient grounds, base;
    public final int groundsRequired, pullTime;
    final ItemStack result;

    public PullingRecipeBuilder(Ingredient grounds, Ingredient base, int groundsRequired, int pullTime, ItemStack result) {
        this.grounds = grounds;
        this.base = base;
        this.groundsRequired = groundsRequired;
        this.pullTime = pullTime;
        this.result = result;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(RecipeOutput exporter, ResourceLocation recipeId) {
        exporter.accept(recipeId, new PullingRecipe(grounds, base, groundsRequired, pullTime, result), null);
    }
}
