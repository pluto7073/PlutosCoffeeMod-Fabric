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

    public static CoffeeType byId(String id) {
        return CoffeeTypes.REGISTRY.get(new Identifier(PlutosCoffee.MOD_ID, id.replace("\"", "")));
    }

    public CoffeeType(String baseName, Item grounds, OnDrink action) {
        this.baseName = baseName;
        this.grounds = grounds;
        this.action = action;
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

}
