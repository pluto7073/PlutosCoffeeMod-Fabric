package ml.pluto7073.plutoscoffee.tags;

import ml.pluto7073.pdapi.PDRegistries;
import ml.pluto7073.pdapi.addition.DrinkAddition;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static final TagKey<Item> COFFEE_BEANS = TagKey.create(Registries.ITEM, PlutosCoffee.asId("roasted_coffee_beans"));
    public static final TagKey<Item> COFFEE_GROUNDS = TagKey.create(Registries.ITEM, PlutosCoffee.asId("ground_coffee_beans"));
    public static final TagKey<Item> MACHINE_WATER_SOURCES = TagKey.create(Registries.ITEM, PlutosCoffee.asId("machine_water_sources"));

    public static final TagKey<DrinkAddition>
            COMMON_TRADEABLE_ADDITIONS = TagKey.create(PDRegistries.DRINK_ADDITION_KEY, PlutosCoffee.asId("common_tradeable_additions")),
            UNCOMMON_TRADEABLE_ADDITIONS = TagKey.create(PDRegistries.DRINK_ADDITION_KEY, PlutosCoffee.asId("uncommon_tradeable_additions")),
            RARE_TRADEABLE_ADDITIONS = TagKey.create(PDRegistries.DRINK_ADDITION_KEY, PlutosCoffee.asId("rare_tradeable_additions"));

}
