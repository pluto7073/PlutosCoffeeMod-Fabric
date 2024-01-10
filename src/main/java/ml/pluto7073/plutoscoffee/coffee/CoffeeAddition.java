package ml.pluto7073.plutoscoffee.coffee;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CoffeeAddition {

    private final Item ingredient;
    private final OnDrink action;
    private final boolean changesColour;
    private final int colour;
    private final int caffeine;
    private final int maxAmount;

    public static CoffeeAddition byId(String id) {
        return CoffeeAdditions.REGISTRY.get(new Identifier(id.replace("\"", "")));
    }

    public CoffeeAddition(Item ingredient, OnDrink action, boolean changesColour, int colour, int caffeine) {
        this(ingredient, action, changesColour, colour, caffeine, 0);
    }

    public CoffeeAddition(Item ingredient, OnDrink action, boolean changesColour, int colour, int caffeine, int maxAmount) {
        this.ingredient = ingredient;
        this.action = action;
        this.changesColour = changesColour;
        this.colour = colour;
        this.caffeine = caffeine;
        this.maxAmount = maxAmount;
    }

    public String getTranslationKey() {
        Identifier id = CoffeeAdditions.getId(this);
        return "coffee_addition." + id.getNamespace() + "." + id.getPath();
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

    public int getCaffeine() {
        return caffeine;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

}
