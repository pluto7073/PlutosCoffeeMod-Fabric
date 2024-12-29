package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.pdapi.PDRegistries;
import ml.pluto7073.pdapi.specialty.SpecialtyDrinkBase;
import ml.pluto7073.pdapi.specialty.SpecialtyDrinkBaseSerializer;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.coffee.CoffeeType;
import ml.pluto7073.plutoscoffee.specialty.CoffeeBaseSerializer;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;

public class ModDrinkBases {

    public static final CoffeeBaseSerializer COFFEE_BASE =
            new CoffeeBaseSerializer();

    public static void init() {
        Registry.register(PDRegistries.SPECIALTY_DRINK_BASE, PlutosCoffee.asId("brewed_coffee"),
                COFFEE_BASE);
    }

}
