package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.recipe.PullingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModRecipes {

    public static final RecipeType<PullingRecipe> PULLING_RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE, PlutosCoffee.asId("pulling"), new RecipeType<PullingRecipe>() {
        public String toString() {
            return "plutoscoffee:pulling";
        }
    });
    public static final RecipeSerializer<PullingRecipe> PULLING_RECIPE_SERIALIZER = registerRecipeSerializer("pulling", new PullingRecipe.Serializer());

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerRecipeSerializer(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, PlutosCoffee.asId(id), serializer);
    }

    public static void init() {}

}
