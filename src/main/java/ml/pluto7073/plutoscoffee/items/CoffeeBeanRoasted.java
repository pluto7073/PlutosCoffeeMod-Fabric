package ml.pluto7073.plutoscoffee.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class CoffeeBeanRoasted extends Item {

    public CoffeeBeanRoasted() {
        super(new Item.Settings().group(ItemGroup.FOOD).food(CoffeeBean.COFFEE_BEAN_FOOD_COMPONENT.build()));
    }

}
