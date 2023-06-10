package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.recipes.CoffeeWorkstationRecipe;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModMisc {

    public static final ItemGroup PC_GROUP;

    public static final RecipeSerializer<CoffeeWorkstationRecipe> COFFEE_WORK_RECIPE_SERIALIZER;

    public static final RecipeType<CoffeeWorkstationRecipe> COFFEE_WORK_RECIPE_TYPE;

    public static void init() {}

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerRecipeSerializer(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(PlutosCoffee.MOD_ID, id), serializer);
    }

    static {
        PC_GROUP = FabricItemGroup.builder(new Identifier(PlutosCoffee.MOD_ID, "pc_group"))
                .icon(() -> new ItemStack(ModItems.MEDIUM_ROAST_BEAN)).build();
        ItemGroupEvents.modifyEntriesEvent(PC_GROUP).register(stacks -> {
            stacks.add(new ItemStack(ModItems.COFFEE_BREWER, 1));
            stacks.add(new ItemStack(ModItems.COFFEE_WORKSTATION, 1));
            stacks.add(new ItemStack(ModItems.COFFEE_BERRY, 1));
            stacks.add(new ItemStack(ModItems.COFFEE_BEAN, 1));
            stacks.add(new ItemStack(ModItems.LIGHT_ROAST_BEAN, 1));
            stacks.add(new ItemStack(ModItems.MEDIUM_ROAST_BEAN, 1));
            stacks.add(new ItemStack(ModItems.DARK_ROAST_BEAN, 1));
            stacks.add(new ItemStack(ModItems.GROUND_LIGHT_ROAST, 1));
            stacks.add(new ItemStack(ModItems.GROUND_MEDIUM_ROAST, 1));
            stacks.add(new ItemStack(ModItems.GROUND_DARK_ROAST, 1));
            stacks.add(new ItemStack(ModItems.GROUND_ESPRESSO_ROAST, 1));
            ItemStack lightRoast = new ItemStack(ModItems.BREWED_COFFEE, 1);
            lightRoast.getOrCreateSubNbt("Coffee").putString("CoffeeType", "light_roast");
            ItemStack mediumRoast = lightRoast.copy();
            mediumRoast.getOrCreateSubNbt("Coffee").putString("CoffeeType", "medium_roast");
            ItemStack darkRoast = mediumRoast.copy();
            darkRoast.getOrCreateSubNbt("Coffee").putString("CoffeeType", "dark_roast");
            stacks.add(lightRoast);
            stacks.add(mediumRoast);
            stacks.add(darkRoast);
            stacks.add(new ItemStack(ModItems.CARAMEL));
            stacks.add(new ItemStack(ModItems.MILK_BOTTLE));
            stacks.add(new ItemStack(ModItems.MOCHA_SAUCE));
        });
        COFFEE_WORK_RECIPE_SERIALIZER = registerRecipeSerializer("coffee_workstation", new CoffeeWorkstationRecipe.Serializer());
        COFFEE_WORK_RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE, new Identifier(PlutosCoffee.MOD_ID, "coffee_workstation"), new RecipeType<CoffeeWorkstationRecipe>() {
            public String toString() {
                return "plutoscoffee:coffee_workstation";
            }
        });
    }

}
