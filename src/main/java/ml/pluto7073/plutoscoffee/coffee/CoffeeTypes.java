package ml.pluto7073.plutoscoffee.coffee;

import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CoffeeTypes {

    public static final Map<ResourceLocation, CoffeeType> REGISTRY = new HashMap<>();

    public static final CoffeeType EMPTY;
    public static final CoffeeType LIGHT_ROAST;
    public static final CoffeeType MEDIUM_ROAST;
    public static final CoffeeType DARK_ROAST;
    public static final CoffeeType ESPRESSO;
    public static final CoffeeType DECAF;

    public static CoffeeType register(String id, CoffeeType type) {
        REGISTRY.put(new ResourceLocation("plutoscoffee", id), type);
        return type;
    }

    public static String getId(CoffeeType type) {
        return getIdentifier(type).getPath();
    }

    public static ResourceLocation getIdentifier(CoffeeType type) {
        Set<ResourceLocation> keySet = REGISTRY.keySet();
        for (ResourceLocation i : keySet) {
            if (REGISTRY.get(i).equals(type)) {
                return i;
            }
        }
        return new ResourceLocation("plutoscoffee:empty");
    }

    public static CoffeeType getFromGrounds(Item grounds) {
        for (ResourceLocation i : REGISTRY.keySet()) {
            if (REGISTRY.get(i).getGrounds() == grounds) {
                return REGISTRY.get(i);
            }
        }
        throw new IllegalArgumentException("No Such CoffeeType with Grounds: " + BuiltInRegistries.ITEM.getId(grounds));
    }

    static {
        EMPTY = register("empty", new CoffeeType("empty", Items.AIR, 0));
        LIGHT_ROAST = register("light_roast", new CoffeeType("light_roast", ModItems.GROUND_LIGHT_ROAST, 125));
        MEDIUM_ROAST = register("medium_roast", new CoffeeType("medium_roast", ModItems.GROUND_MEDIUM_ROAST, 120));
        DARK_ROAST = register("dark_roast", new CoffeeType("dark_roast", ModItems.GROUND_DARK_ROAST, 115));
        ESPRESSO = register("espresso", new CoffeeType("espresso", ModItems.GROUND_ESPRESSO_ROAST, 100));
        DECAF = register("decaf", new CoffeeType("decaf", ModItems.GROUND_DECAF_ROAST, 7));
    }

}
