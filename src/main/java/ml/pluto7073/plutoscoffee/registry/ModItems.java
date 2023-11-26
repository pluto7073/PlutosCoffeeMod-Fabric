package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.items.*;
import net.fabricmc.fabric.api.registry.VillagerInteractionRegistries;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static Item COFFEE_BERRY;
    public static Item COFFEE_WORKSTATION;
    public static Item COFFEE_BREWER;
    public static Item COFFEE_BEAN;
    public static Item LIGHT_ROAST_BEAN;
    public static Item MEDIUM_ROAST_BEAN;
    public static Item DARK_ROAST_BEAN;
    public static Item GROUND_LIGHT_ROAST;
    public static Item GROUND_MEDIUM_ROAST;
    public static Item GROUND_DARK_ROAST;
    public static Item GROUND_ESPRESSO_ROAST;
    public static Item CARAMEL;
    public static Item MOCHA_SAUCE;
    public static Item MILK_BOTTLE;

    public static Item BREWED_COFFEE;

    private static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(PlutosCoffee.MOD_ID, id), item);
    }

    private static Item register(Block block) {
        return Registry.register(Registries.ITEM, Registries.BLOCK.getId(block), new BlockItem(block, new Item.Settings()));
    }

    public static void init() {
        COFFEE_BEAN = register("coffee_bean", new CoffeeBean());
        COFFEE_BERRY = register("coffee_berry", new AliasedBlockItem(ModBlocks.COFFEE_CROP, new Item.Settings()));
        GROUND_LIGHT_ROAST = register("ground_coffee", new Item(new Item.Settings()));
        LIGHT_ROAST_BEAN = register("roasted_coffee_bean", new Item(new Item.Settings().food(CoffeeBean.COFFEE_BEAN_FOOD_COMPONENT.build())));
        CARAMEL = register("caramel", new Item(new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE).maxCount(16)));
        MOCHA_SAUCE = register("mocha_syrup", new Item(new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE).maxCount(16)));
        MILK_BOTTLE = register("milk_bottle", new MilkBottle());
        MEDIUM_ROAST_BEAN = register("medium_roast_bean", new Item(new Item.Settings().food(CoffeeBean.COFFEE_BEAN_FOOD_COMPONENT.build())));
        GROUND_MEDIUM_ROAST = register("ground_medium_roast", new Item(new Item.Settings()));
        DARK_ROAST_BEAN = register("dark_roast_bean", new Item(new Item.Settings().food(CoffeeBean.COFFEE_BEAN_FOOD_COMPONENT.build())));
        GROUND_DARK_ROAST = register("ground_dark_roast", new Item(new Item.Settings()));
        GROUND_ESPRESSO_ROAST = register("espresso_grounds", new Item(new Item.Settings()));
        BREWED_COFFEE = register("brewed_coffee", new BrewedCoffee(new Item.Settings().maxCount(1)));
        COFFEE_WORKSTATION = register(ModBlocks.COFFEE_WORKSTATION);
        COFFEE_BREWER = register(ModBlocks.COFFEE_BREWER);
    }

}
