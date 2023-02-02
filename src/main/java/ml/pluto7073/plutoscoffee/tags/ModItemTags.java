package ml.pluto7073.plutoscoffee.tags;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItemTags {

    public static final TagKey<Item> COFFEE_BEANS = TagKey.of(Registry.ITEM_KEY, new Identifier(PlutosCoffee.MOD_ID, "roasted_coffee_beans"));
    public static final TagKey<Item> COFFEE_GROUNDS = TagKey.of(Registry.ITEM_KEY, new Identifier(PlutosCoffee.MOD_ID, "ground_coffee_beans"));

}
