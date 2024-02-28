package ml.pluto7073.plutoscoffee.coffee;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.items.EspressoShotItem;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CoffeeAdditions {

    public static final Map<Identifier, CoffeeAddition> REGISTRY = new HashMap<>();

    public static final CoffeeAddition EMPTY;
    public static final CoffeeAddition MILK_BOTTLE;
    public static final CoffeeAddition CARAMEL;
    public static final CoffeeAddition SUGAR;
    public static final CoffeeAddition MOCHA_SYRUP;
    public static final CoffeeAddition ESPRESSO_SHOT;
    public static final CoffeeAddition ICE;
    public static final CoffeeAddition BURNT;

    public static CoffeeAddition register(String id, CoffeeAddition addition) {
        REGISTRY.put(new Identifier(PlutosCoffee.MOD_ID, id), addition);
        return addition;
    }

    public static Identifier getId(CoffeeAddition addition) {
        for (Identifier i : REGISTRY.keySet()) {
            if (REGISTRY.get(i) == addition) {
                return i;
            }
        }
        return new Identifier(PlutosCoffee.MOD_ID, "empty");
    }

    public static CoffeeAddition getFromIngredient(ItemStack ingredient) {
        for (Identifier i : REGISTRY.keySet()) {
            if (ingredient.isOf(REGISTRY.get(i).getIngredient())) return REGISTRY.get(i);
        }
        throw new IllegalArgumentException("No Such Coffee Ingredient with Item: " + Registries.ITEM.getId(ingredient.getItem()));
    }

    static {
        EMPTY = register("empty", new CoffeeAddition(Items.AIR, (stack, world, user) -> {}, false, 0, 0));
        MILK_BOTTLE = register("milk", new CoffeeAddition(ModItems.MILK_BOTTLE, (stack, world, user) -> {
            Collection<StatusEffectInstance> statusEffects = user.getStatusEffects();
            for (StatusEffectInstance instance : statusEffects) {
                if (!instance.getEffectType().isBeneficial())
                    user.removeStatusEffect(instance.getEffectType());
            }
        }, true, 0xECECEC, 0));
        CARAMEL = register("caramel", new CoffeeAddition(ModItems.CARAMEL, (stack, world, user) -> {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 2400, 2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 200));
        }, true, 0x57260D, 0));
        SUGAR = register("sugar", new CoffeeAddition(Items.SUGAR, (stack, world, user) -> {
            if (user instanceof PlayerEntity) {
                ((PlayerEntity) user).getHungerManager().add(1, 0);
            }
        }, false, 0xFFFFFF,0));
        MOCHA_SYRUP = register("mocha_syrup", new CoffeeAddition(ModItems.MOCHA_SAUCE, (stack, world, user) -> {
            if (user instanceof PlayerEntity) {
                ((PlayerEntity) user).getHungerManager().add(6, 5.0f);
            }
        }, true, 0x301A0A, 0));
        ESPRESSO_SHOT = register("espresso_shot", new CoffeeAddition(ModItems.ESPRESSO_SHOT, (stack, world, user) -> {},
                true, 0x160A02, EspressoShotItem.CAFFEINE_CONTENT));
        ICE = register("ice", new CoffeeAddition(Items.ICE, (stack, world, user) -> {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 10 * 20, 1));
        }, false, 0, 0, 1));
        BURNT = register("burnt", new CoffeeAddition(Items.AIR, (stack, world, user) -> {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20 * 60));
            user.damage(user.getDamageSources().onFire(), 2);
        }, false, 0, 0, 1));
    }

}
