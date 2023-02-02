package ml.pluto7073.plutoscoffee.items;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class CoffeeBean extends Item {

    public static FoodComponent.Builder COFFEE_BEAN_FOOD_COMPONENT = new FoodComponent.Builder()
            .snack()
            .alwaysEdible()
            .hunger(1)
            .saturationModifier(0.5f)
            .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 10), 1)
            .statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 20 * 10), 1)
            .statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 20 * 10), 1);

    public CoffeeBean() {
        super(new Item.Settings());
    }

}
