package ml.pluto7073.plutoscoffee.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class CoffeeBean extends Item {

    public static FoodProperties.Builder COFFEE_BEAN_FOOD_COMPONENT = new FoodProperties.Builder()
            .fast()
            .alwaysEat()
            .nutrition(1)
            .saturationMod(0.5f)
            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 10), 1)
            .effect(new MobEffectInstance(MobEffects.JUMP, 20 * 10), 1)
            .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 10), 1);

    public static FoodProperties.Builder DECAF_BEAN_FOOD_COMPONENT = new FoodProperties.Builder()
            .fast()
            .alwaysEat()
            .nutrition(1)
            .saturationMod(0);

    public CoffeeBean() {
        super(new Item.Properties());
    }

}
