package ml.pluto7073.plutoscoffee.coffee;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CoffeeAddition {

    private final String baseName;
    private final Item ingredient;
    private final OnDrink action;
    private final boolean changesColour;
    private final int colour;

    public static CoffeeAddition byId(String id) {
        return CoffeeAdditions.REGISTRY.get(new Identifier(id.replace("\"", "")));
    }

    public CoffeeAddition(String baseName, Item ingredient, OnDrink action, boolean changesColour, int colour) {
        this.baseName = baseName;
        this.ingredient = ingredient;
        this.action = action;
        this.changesColour = changesColour;
        this.colour = colour;
    }

    public String getTranslationKey() {
        return "coffee_addition." + CoffeeAdditions.getId(this).getNamespace() + "." + baseName;
    }

    public Item getIngredient() {
        return ingredient;
    }

    public void onDrink(ItemStack stack, World world, LivingEntity user) {
        action.onDrink(stack, world, user);
    }

    public boolean changesColour() {
        return changesColour;
    }

    public int getColour() {
        return colour;
    }

}
