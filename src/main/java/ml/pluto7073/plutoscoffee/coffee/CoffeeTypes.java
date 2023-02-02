package ml.pluto7073.plutoscoffee.coffee;

import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CoffeeTypes {

    public static final Map<Identifier, CoffeeType> REGISTRY = new HashMap<>();

    public static final CoffeeType EMPTY;
    public static final CoffeeType LIGHT_ROAST;
    public static final CoffeeType MEDIUM_ROAST;
    public static final CoffeeType DARK_ROAST;

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
        throw new IllegalArgumentException("No Such CoffeeType with Grounds: " + Registry.ITEM.getId(grounds));
    }

    static {
        EMPTY = register("empty", new CoffeeType("empty", Items.AIR, ((stack, world, user) -> {})));
        LIGHT_ROAST = register("light_roast", new CoffeeType("light_roast", ModItems.GROUND_LIGHT_ROAST, ((stack, world, user) -> {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1200));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 1200));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200));
            //TODO: Light roast exclusives
        })));
        MEDIUM_ROAST = register("medium_roast", new CoffeeType("medium_roast", ModItems.GROUND_MEDIUM_ROAST, ((stack, world, user) -> {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1200));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 1200));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200));
        })));
        DARK_ROAST = register("dark_roast", new CoffeeType("dark_roast", ModItems.GROUND_DARK_ROAST, (stack, world, user) -> {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1200));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 1200));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200));
            //TODO: Dark roast exclusives
        }));
    }

}
