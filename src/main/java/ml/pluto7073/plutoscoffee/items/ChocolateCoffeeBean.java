package ml.pluto7073.plutoscoffee.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ChocolateCoffeeBean extends Item {
    public ChocolateCoffeeBean() {
        super(new Item.Settings().group(ItemGroup.FOOD).food(CoffeeBean.COFFEE_BEAN_FOOD_COMPONENT.hunger(3).saturationModifier(2.5f).build()));
    }
}
