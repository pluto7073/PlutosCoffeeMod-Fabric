package ml.pluto7073.plutoscoffee.coffee;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
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
        EMPTY = register("empty", new CoffeeAddition("empty", Items.AIR, (stack, world, user) -> {}, false, 0));
        MILK_BOTTLE = register("milk", new CoffeeAddition("milk", ModItems.MILK_BOTTLE, (stack, world, user) -> {
            Collection<StatusEffectInstance> statusEffects = user.getStatusEffects();
            for (StatusEffectInstance instance : statusEffects) {
                if (!instance.getEffectType().isBeneficial())
                    user.removeStatusEffect(instance.getEffectType());
            }
        }, true, 0xECECEC));
        CARAMEL = register("caramel", new CoffeeAddition("caramel", ModItems.CARAMEL, (stack, world, user) -> {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 2400, 2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 1200));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 600, 2));
        }, true, 0x57260D));
        SUGAR = register("sugar", new CoffeeAddition("sugar", Items.SUGAR, (stack, world, user) -> {
            if (user instanceof PlayerEntity) {
                ((PlayerEntity) user).getHungerManager().add(1, 0);
            }
        }, false, 0xFFFFFF));
        MOCHA_SYRUP = register("mocha_syrup", new CoffeeAddition("mocha_syrup", ModItems.MOCHA_SAUCE, (stack, world, user) -> {
            if (user instanceof PlayerEntity) {
                ((PlayerEntity) user).getHungerManager().add(6, 5.0f);
            }
        }, true, 0x301A0A));
    }

}
