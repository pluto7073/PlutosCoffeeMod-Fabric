package ml.pluto7073.plutoscoffee.potions;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.effects.ModStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModPotions {

    public static final Potion COFFEE;
    public static final Potion MILK_COFFEE;
    public static final Potion MOCHA;
    public static final Potion CARAMEL_LATTE;

    static {
        COFFEE = register("coffee", new Potion(new StatusEffectInstance(ModStatusEffects.CAFFEINE_HIGH, 20 * 60)));
        MILK_COFFEE = register("milk_coffee", new Potion(new StatusEffectInstance(ModStatusEffects.CLEAR_ALL_EFFECTS, 20 * 10),
                new StatusEffectInstance(ModStatusEffects.CAFFEINE_HIGH, 20 * 60)));
        MOCHA = register("mocha", new Potion(new StatusEffectInstance(ModStatusEffects.CHOCOLATE_FOOD, 20 * 10),
                new StatusEffectInstance(ModStatusEffects.CAFFEINE_HIGH, 20 * 60)));
        CARAMEL_LATTE = register("caramel_latte", new Potion(new StatusEffectInstance(ModStatusEffects.CLEAR_ALL_EFFECTS, 20 * 10),
                new StatusEffectInstance(ModStatusEffects.CARAMEL_FOOD, 20 * 60),
                new StatusEffectInstance(ModStatusEffects.CAFFEINE_HIGH, 20 * 60)));
    }

    public static void registerPotions() {}

    private static Potion register(String name, Potion potion) {
        return Registry.register(Registry.POTION, new Identifier(PlutosCoffee.MOD_ID, name), potion);
    }

}
