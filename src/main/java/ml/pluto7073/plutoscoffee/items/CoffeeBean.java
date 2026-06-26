package ml.pluto7073.plutoscoffee.items;

import ml.pluto7073.pdapi.PDAPI;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class CoffeeBean extends Item {

    public static FoodProperties.Builder COFFEE_BEAN_FOOD_COMPONENT = new FoodProperties.Builder()
            .fast()
            .alwaysEat()
            .nutrition(1)
            .saturationMod(0.5f);

    public static FoodProperties.Builder DECAF_BEAN_FOOD_COMPONENT = new FoodProperties.Builder()
            .fast()
            .alwaysEat()
            .nutrition(1)
            .saturationMod(0)
            .addChemical(PDAPI.asId("caffeine"), 0.1f);

    public CoffeeBean() {
        super(new Item.Properties());
    }

}
