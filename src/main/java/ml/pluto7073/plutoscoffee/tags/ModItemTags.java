package ml.pluto7073.plutoscoffee.tags;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {

    public static final TagKey<Item> COFFEE_BEANS = TagKey.create(Registries.ITEM, PlutosCoffee.asId("roasted_coffee_beans"));
    public static final TagKey<Item> COFFEE_GROUNDS = TagKey.create(Registries.ITEM, PlutosCoffee.asId("ground_coffee_beans"));

}
