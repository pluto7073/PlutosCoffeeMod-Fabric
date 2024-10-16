package ml.pluto7073.plutoscoffee.coffee;

import com.google.common.collect.ImmutableMap;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.world.item.Item;

import java.util.HashMap;

public class CoffeeGrounds {

    private static final HashMap<Item, Item> BEANS_TO_GROUNDS_REGISTRY = new HashMap<>();
    private static final HashMap<Item, Item> GROUNDS_TO_BEANS_REGISTRY = new HashMap<>();

    public static void register(Item beansItem, Item groundsItem) {
        BEANS_TO_GROUNDS_REGISTRY.put(beansItem, groundsItem);
        GROUNDS_TO_BEANS_REGISTRY.put(groundsItem, beansItem);
    }

    public static Item getGrounds(Item beansItem) {
        return BEANS_TO_GROUNDS_REGISTRY.get(beansItem);
    }

    public static Item getBeans(Item groundsItem) {
        return GROUNDS_TO_BEANS_REGISTRY.get(groundsItem);
    }

    public static ImmutableMap<Item, Item> getBeansToGroundsRegistry() {
        return ImmutableMap.copyOf(BEANS_TO_GROUNDS_REGISTRY);
    }

    static {
        register(ModItems.LIGHT_ROAST_BEAN, ModItems.GROUND_LIGHT_ROAST);
        register(ModItems.MEDIUM_ROAST_BEAN, ModItems.GROUND_MEDIUM_ROAST);
        register(ModItems.DARK_ROAST_BEAN, ModItems.GROUND_DARK_ROAST);
        register(ModItems.ESPRESSO_ROAST_BEAN, ModItems.GROUND_ESPRESSO_ROAST);
        register(ModItems.DECAF_ROAST_BEAN, ModItems.GROUND_DECAF_ROAST);
    }

    public static void init() {}

}
