package ml.pluto7073.plutoscoffee.coffee;

import ml.pluto7073.pdapi.addition.OnDrink;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CoffeeType {

    private final String baseName;
    private final Item grounds;
    private final OnDrink action;
    private final int caffeineContent;

    @SuppressWarnings("unused")
    public static final String ZELDA = null;

    public static CoffeeType byId(String id) {
        CoffeeType type;
        if (id.contains(":")) {
            type =  CoffeeTypes.REGISTRY.get(new ResourceLocation(id.replace("\"", "")));
        } else {
            type = CoffeeTypes.REGISTRY.get(new ResourceLocation(PlutosCoffee.MOD_ID, id.replace("\"", "")));
        }
        if (type == null) {
            type = CoffeeTypes.EMPTY;
        }
        return type;
    }

    /**
     *
     * @param baseName the id used in registering the coffee type
     * @param grounds the item to be used to brew the coffee
     * @param action do this when this coffee type is drank
     * @param caffeineContent amount in mg of caffeine to add to the drinker.
     */
    public CoffeeType(String baseName, Item grounds, OnDrink action, int caffeineContent) {
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

    public void onDrink(ItemStack stack, Level level, LivingEntity user) {
        action.onDrink(stack, level, user);
    }

    public int getCaffeineContent() {
        return caffeineContent;
    }

}
