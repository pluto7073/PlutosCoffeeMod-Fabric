package ml.pluto7073.plutoscoffee.tags;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModItemTags {

    public static final TagKey<Item> COFFEE_BEANS = TagKey.of(RegistryKeys.ITEM, new Identifier(PlutosCoffee.MOD_ID, "roasted_coffee_beans"));
    public static final TagKey<Item> COFFEE_GROUNDS = TagKey.of(RegistryKeys.ITEM, new Identifier(PlutosCoffee.MOD_ID, "ground_coffee_beans"));

}
