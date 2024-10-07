package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.items.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;

public class ModItems {

    public static final Item COFFEE_BERRY = new ItemNameBlockItem(ModBlocks.COFFEE_CROP, new Item.Properties());
    public static final Item COFFEE_BREWER = new BlockItem(ModBlocks.COFFEE_BREWER, new Item.Properties());
    public static final Item COFFEE_GRINDR = new BlockItem(ModBlocks.COFFEE_GRINDR, new Item.Properties());
    public static final Item ESPRESSO_MACHINE = new BlockItem(ModBlocks.ESPRESSO_MACHINE, new Item.Properties());
    public static final Item COFFEE_BEAN = new CoffeeBean();
    public static final Item LIGHT_ROAST_BEAN = new Item(new Item.Properties().food(CoffeeBean.COFFEE_BEAN_FOOD_COMPONENT.build()));
    public static final Item MEDIUM_ROAST_BEAN = new Item(new Item.Properties().food(CoffeeBean.COFFEE_BEAN_FOOD_COMPONENT.build()));
    public static final Item DARK_ROAST_BEAN = new Item(new Item.Properties().food(CoffeeBean.COFFEE_BEAN_FOOD_COMPONENT.build()));
    public static final Item ESPRESSO_ROAST_BEAN = new Item(new Item.Properties().food(CoffeeBean.COFFEE_BEAN_FOOD_COMPONENT.build()));
    public static final Item DECAF_ROAST_BEAN = new Item(new Item.Properties().food(CoffeeBean.DECAF_BEAN_FOOD_COMPONENT.build()));
    public static final Item GROUND_LIGHT_ROAST = new Item(new Item.Properties());
    public static final Item GROUND_MEDIUM_ROAST = new Item(new Item.Properties());
    public static final Item GROUND_DARK_ROAST = new Item(new Item.Properties());
    public static final Item GROUND_ESPRESSO_ROAST = new Item(new Item.Properties());
    public static final Item GROUND_DECAF_ROAST = new Item(new Item.Properties());
    public static final Item USED_COFFEE_GROUNDS = new Item(new Item.Properties());
    public static final Item CARAMEL = new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16));
    public static final Item MOCHA_SAUCE = new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16));

    public static final Item BREWED_COFFEE = new BrewedCoffee(new Item.Properties().stacksTo(1));
    public static final Item BLONDE_ESPRESSO_SHOT = new EspressoShotItem(85, new Item.Properties().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE));
    public static final Item ESPRESSO_SHOT = new EspressoShotItem(75, new Item.Properties().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE));
    public static final Item DECAF_ESPRESSO_SHOT = new EspressoShotItem(2, new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16));
    public static final Item LATTE = new LatteItem(new Item.Properties().stacksTo(1));

    private static void register(String id, Item item) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(PlutosCoffee.MOD_ID, id), item);
    }

    public static void init() {
        register("coffee_bean", COFFEE_BEAN);
        register("coffee_berry", COFFEE_BERRY);
        register("ground_coffee", GROUND_LIGHT_ROAST);
        register("roasted_coffee_bean", LIGHT_ROAST_BEAN);
        register("caramel", CARAMEL);
        register("mocha_syrup", MOCHA_SAUCE);
        register("medium_roast_bean", MEDIUM_ROAST_BEAN);
        register("ground_medium_roast", GROUND_MEDIUM_ROAST);
        register("dark_roast_bean", DARK_ROAST_BEAN);
        register("ground_dark_roast", GROUND_DARK_ROAST);
        register("espresso_roast_bean", ESPRESSO_ROAST_BEAN);
        register("espresso_grounds", GROUND_ESPRESSO_ROAST);
        register("decaf_roast_bean", DECAF_ROAST_BEAN);
        register("ground_decaf_roast", GROUND_DECAF_ROAST);
        register("brewed_coffee", BREWED_COFFEE);
        register("blonde_espresso_shot", BLONDE_ESPRESSO_SHOT);
        register("espresso_shot", ESPRESSO_SHOT);
        register("decaf_espresso_shot", DECAF_ESPRESSO_SHOT);
        register("latte", LATTE);
        register("used_coffee_grounds", USED_COFFEE_GROUNDS);

        register("coffee_brewer", COFFEE_BREWER);
        register("coffee_grinder", COFFEE_GRINDR);
        register("espresso_machine", ESPRESSO_MACHINE);
    }

}
