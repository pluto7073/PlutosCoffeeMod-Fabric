package ml.pluto7073.plutoscoffee.effects;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModStatusEffects {

    public static final StatusEffect CLEAR_ALL_EFFECTS;
    public static final StatusEffect CAFFEINE_HIGH;
    public static final StatusEffect CHOCOLATE_FOOD;
    public static final StatusEffect CARAMEL_FOOD;

    static {
        CLEAR_ALL_EFFECTS = register("clear_all_effects", new ClearEffectsStatusEffect());
        CAFFEINE_HIGH = register("caffeine_high", new CoffeeCaffeineHigh());
        CHOCOLATE_FOOD = register("chocolate_food", new ChocolateFoodStatusEffect());
        CARAMEL_FOOD = register("caramel_food", new CaramelFoodStatusEffect());
    }

    private static StatusEffect register(String name, StatusEffect effect) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(PlutosCoffee.MOD_ID, name), effect);
    }

    public static void init() {}

}
