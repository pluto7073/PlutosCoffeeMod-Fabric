package ml.pluto7073.plutoscoffee.coffee;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CoffeeType {

    private final String baseName;
    private final Item grounds;
    private final OnDrink action;
    private final float caffeineContent;

    @SuppressWarnings("unused")
    public static final String ZELDA = null;

    public static CoffeeType byId(String id) {
        if (id.contains(":")) {
            return CoffeeTypes.REGISTRY.get(new Identifier(id.replace("\"", "")));
        }
        return CoffeeTypes.REGISTRY.get(new Identifier(PlutosCoffee.MOD_ID, id.replace("\"", "")));
    }

    /**
     *
     * @param baseName - the id used in registering the coffee type
     * @param grounds - the item to be used to brew the coffee
     * @param action - do this when this coffee type is drank
     * @param caffeineContent - amount in mg of caffeine to add to the drinker.
     */
    public CoffeeType(String baseName, Item grounds, OnDrink action, float caffeineContent) {
        this.baseName = baseName;
        this.grounds = grounds;
        this.action = action;
        this.caffeineContent = caffeineContent;
    }

    public String getTranslationKey() {
        return "coffee_type.plutoscoffee." + baseName;
    }

    public Item getGrounds() {
        return grounds;
    }

    public void onDrink(ItemStack stack, World world, LivingEntity user) {
        action.onDrink(stack, world, user);
    }

    public float getCaffeineContent() {
        return caffeineContent;
    }

}
