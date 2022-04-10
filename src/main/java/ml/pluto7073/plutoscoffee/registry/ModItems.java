package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.items.*;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final Item COFFEE_BEAN = new CoffeeBean();
    public static final Item COFFEE_BERRY = new AliasedBlockItem(ModBlocks.COFFEE_CROP, new Item.Settings().group(ItemGroup.FOOD));
    public static final Item GROUND_COFFEE = new GroundCoffee();
    public static final Item COFFEE_BEAN_ROASTED = new CoffeeBeanRoasted();
    public static final Item CHOCOLATE_COFFEE_BEAN = new ChocolateCoffeeBean();
    public static final Item CHOCOLATE_GROUND_COFFEE = new ChocolateGroundCoffee();
    public static final Item CARAMEL = new Caramel();
    public static final Item MILK_BOTTLE = new MilkBottle();

    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(PlutosCoffee.MOD_ID, "ground_coffee"), GROUND_COFFEE);
        Registry.register(Registry.ITEM, new Identifier(PlutosCoffee.MOD_ID, "coffee_berry"), COFFEE_BERRY);
        Registry.register(Registry.ITEM, new Identifier(PlutosCoffee.MOD_ID, "coffee_bean"), COFFEE_BEAN);
        Registry.register(Registry.ITEM, new Identifier(PlutosCoffee.MOD_ID, "roasted_coffee_bean"), COFFEE_BEAN_ROASTED);
        Registry.register(Registry.ITEM, new Identifier(PlutosCoffee.MOD_ID, "chocolate_coffee_bean"), CHOCOLATE_COFFEE_BEAN);
        Registry.register(Registry.ITEM, new Identifier(PlutosCoffee.MOD_ID, "chocolate_ground_coffee"), CHOCOLATE_GROUND_COFFEE);
        Registry.register(Registry.ITEM, new Identifier(PlutosCoffee.MOD_ID, "caramel"), CARAMEL);
        Registry.register(Registry.ITEM, new Identifier(PlutosCoffee.MOD_ID, "milk_bottle"), MILK_BOTTLE);
    }

}
