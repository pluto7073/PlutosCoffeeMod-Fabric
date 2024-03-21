package ml.pluto7073.plutoscoffee.coffee;

import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CoffeeTypes {

    public static final Map<Identifier, CoffeeType> REGISTRY = new HashMap<>();

    public static final CoffeeType EMPTY;
    public static final CoffeeType LIGHT_ROAST;
    public static final CoffeeType MEDIUM_ROAST;
    public static final CoffeeType DARK_ROAST;
    public static final CoffeeType ESPRESSO;

    public static CoffeeType register(String id, CoffeeType type) {
        REGISTRY.put(new Identifier("plutoscoffee", id), type);
        return type;
    }

    public static String getId(CoffeeType type) {
        return getIdentifier(type).getPath();
    }

    public static Identifier getIdentifier(CoffeeType type) {
        Set<Identifier> keySet = REGISTRY.keySet();
        for (Identifier i : keySet) {
            if (REGISTRY.get(i).equals(type)) {
                return i;
            }
        }
        return new Identifier("plutoscoffee:empty");
    }

    public static CoffeeType getFromGrounds(Item grounds) {
        for (Identifier i : REGISTRY.keySet()) {
            if (REGISTRY.get(i).getGrounds() == grounds) {
                return REGISTRY.get(i);
            }
        }
        throw new IllegalArgumentException("No Such CoffeeType with Grounds: " + Registries.ITEM.getId(grounds));
    }

    static {
        EMPTY = register("empty", new CoffeeType("empty", Items.AIR, (stack, world, user) -> {}, 0));
        LIGHT_ROAST = register("light_roast", new CoffeeType("light_roast", ModItems.GROUND_LIGHT_ROAST, (stack, world, user) -> {
            //TODO: Light roast exclusives
        }, 125));
        MEDIUM_ROAST = register("medium_roast", new CoffeeType("medium_roast", ModItems.GROUND_MEDIUM_ROAST, (stack, world, user) -> {
        }, 120));
        DARK_ROAST = register("dark_roast", new CoffeeType("dark_roast", ModItems.GROUND_DARK_ROAST, (stack, world, user) -> {
            //TODO: Dark roast exclusives
        }, 115));
        ESPRESSO = register("espresso", new CoffeeType("espresso", ModItems.GROUND_ESPRESSO_ROAST, (stack, world, user) -> {
        }, 100));
    }

}
