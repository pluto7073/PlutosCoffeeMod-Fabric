package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.recipe.PullingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {

    public static final RecipeType<PullingRecipe> PULLING_RECIPE_TYPE = Registry.register(BuiltInRegistries.RECIPE_TYPE, PlutosCoffee.asId("pulling"), new RecipeType<PullingRecipe>() {
        public String toString() {
            return "plutoscoffee:pulling";
        }
    });
    public static final RecipeSerializer<PullingRecipe> PULLING_RECIPE_SERIALIZER = registerRecipeSerializer("pulling", new PullingRecipe.Serializer());

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerRecipeSerializer(String id, S serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, PlutosCoffee.asId(id), serializer);
    }

    public static void init() {}

}
