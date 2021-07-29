package com.pluto7073.plutoscoffee.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;

public class Caramel extends Item {
    public Caramel() {
        super(new Item.Settings().group(ItemGroup.FOOD).recipeRemainder(Items.GLASS_BOTTLE).maxCount(16));
    }
}
