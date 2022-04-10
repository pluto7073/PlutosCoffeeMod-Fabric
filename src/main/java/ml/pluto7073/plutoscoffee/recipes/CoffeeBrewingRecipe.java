package ml.pluto7073.plutoscoffee.recipes;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CoffeeBrewingRecipe implements Recipe<CraftingInventory> {

    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;
    private final Identifier id;

    public CoffeeBrewingRecipe(Identifier id, ItemStack result, Ingredient base, Ingredient addition) {
        this.id = id;
        this.result = result;
        this.base = base;
        this.addition = addition;
    }

    public Ingredient getBase() {
        return this.base;
    }

    public Ingredient getAddition() {
        return this.addition;
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        if (inventory.size() < 2) return false;
        return base.test(inventory.getStack(0)) && addition.test(inventory.getStack(1));
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        return this.getOutput().copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    public ItemStack getOutput() {
        return this.result;
    }

    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CoffeeBrewingRecipe> {
        private Type(){}
        public static final Type INSTANCE = new Type();
        public static final String ID = "coffee_brewing";
    }

}
