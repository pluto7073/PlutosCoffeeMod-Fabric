package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.items.BrewedCoffee;
import ml.pluto7073.plutoscoffee.items.CoffeeBean;
import ml.pluto7073.plutoscoffee.items.EspressoShotItem;
import ml.pluto7073.plutoscoffee.items.MilkBottle;
import net.minecraft.block.Block;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item COFFEE_BERRY = new AliasedBlockItem(ModBlocks.COFFEE_CROP, new Item.Settings());
    public static final Item COFFEE_WORKSTATION = new BlockItem(ModBlocks.COFFEE_WORKSTATION, new Item.Settings());
    public static final Item COFFEE_BREWER = new BlockItem(ModBlocks.COFFEE_BREWER, new Item.Settings());
    public static final Item COFFEE_GRINDR = new BlockItem(ModBlocks.COFFEE_GRINDR, new Item.Settings());
    public static final Item ESPRESSO_MACHINE = new BlockItem(ModBlocks.ESPRESSO_MACHINE, new Item.Settings());
    public static final Item COFFEE_BEAN = new CoffeeBean();
    public static final Item LIGHT_ROAST_BEAN = new Item(new Item.Settings().food(CoffeeBean.COFFEE_BEAN_FOOD_COMPONENT.build()));
    public static final Item MEDIUM_ROAST_BEAN = new Item(new Item.Settings().food(CoffeeBean.COFFEE_BEAN_FOOD_COMPONENT.build()));
    public static final Item DARK_ROAST_BEAN = new Item(new Item.Settings().food(CoffeeBean.COFFEE_BEAN_FOOD_COMPONENT.build()));
    public static final Item ESPRESSO_ROAST_BEAN = new Item(new Item.Settings().food(CoffeeBean.COFFEE_BEAN_FOOD_COMPONENT.build()));
    public static final Item GROUND_LIGHT_ROAST = new Item(new Item.Settings());
    public static final Item GROUND_MEDIUM_ROAST = new Item(new Item.Settings());
    public static final Item GROUND_DARK_ROAST = new Item(new Item.Settings());
    public static final Item GROUND_ESPRESSO_ROAST = new Item(new Item.Settings());
    public static final Item USED_COFFEE_GROUNDS = new Item(new Item.Settings());
    public static final Item CARAMEL = new Item(new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE).maxCount(16));
    public static final Item MOCHA_SAUCE = new Item(new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE).maxCount(16));
    public static final Item MILK_BOTTLE = new MilkBottle();

    public static final Item BREWED_COFFEE = new BrewedCoffee(new Item.Settings().maxCount(1).recipeRemainder(Items.GLASS_BOTTLE));
    public static final Item ESPRESSO_SHOT = new EspressoShotItem(new Item.Settings().maxCount(16).recipeRemainder(Items.GLASS_BOTTLE));

    private static void register(String id, Item item) {
        Registry.register(Registries.ITEM, new Identifier(PlutosCoffee.MOD_ID, id), item);
    }

    public static void init() {
        register("coffee_bean", COFFEE_BEAN);
        register("coffee_berry", COFFEE_BERRY);
        register("ground_coffee", GROUND_LIGHT_ROAST);
        register("roasted_coffee_bean", LIGHT_ROAST_BEAN);
        register("caramel", CARAMEL);
        register("mocha_syrup", MOCHA_SAUCE);
        register("milk_bottle", MILK_BOTTLE);
        register("medium_roast_bean", MEDIUM_ROAST_BEAN);
        register("ground_medium_roast", GROUND_MEDIUM_ROAST);
        register("dark_roast_bean", DARK_ROAST_BEAN);
        register("ground_dark_roast", GROUND_DARK_ROAST);
        register("espresso_roast_bean", ESPRESSO_ROAST_BEAN);
        register("espresso_grounds", GROUND_ESPRESSO_ROAST);
        register("brewed_coffee", BREWED_COFFEE);
        register("espresso_shot", ESPRESSO_SHOT);
        register("used_coffee_grounds", USED_COFFEE_GROUNDS);

        register("coffee_workstation", COFFEE_WORKSTATION);
        register("coffee_brewer", COFFEE_BREWER);
        register("coffee_grinder", COFFEE_GRINDR);
        register("espresso_machine", ESPRESSO_MACHINE);
    }

}
