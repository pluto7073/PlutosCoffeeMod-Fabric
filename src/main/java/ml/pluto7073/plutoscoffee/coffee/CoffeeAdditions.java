package ml.pluto7073.plutoscoffee.coffee;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.items.EspressoShotItem;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

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
    public static final CoffeeAddition CHORUS_FRUIT;
    public static final CoffeeAddition GLOW_BERRIES;
    public static final CoffeeAddition HONEY;
    public static final CoffeeAddition DIAMOND;
    public static final CoffeeAddition NETHERITE;

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
        CHORUS_FRUIT = register("chorus_fruit", new CoffeeAddition(Items.CHORUS_FRUIT, (stack, world, user) -> {
            double d = user.getX();
            double e = user.getY();
            double f = user.getZ();

            for(int i = 0; i < 16; ++i) {
                double g = user.getX() + (user.getRandom().nextDouble() - 0.5) * 16.0;
                double h = MathHelper.clamp(user.getY() + (double)(user.getRandom().nextInt(16) - 8), (double)world.getBottomY(), (double)(world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1));
                double j = user.getZ() + (user.getRandom().nextDouble() - 0.5) * 16.0;
                if (user.hasVehicle()) {
                    user.stopRiding();
                }

                Vec3d vec3d = user.getPos();
                if (user.teleport(g, h, j, true)) {
                    world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(user));
                    SoundEvent soundEvent = user instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                    world.playSound(null, d, e, f, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    user.playSound(soundEvent, 1.0F, 1.0F);
                    user.onLanding();
                    break;
                }
            }
        }, true, 0x8B00FF, 0));
        GLOW_BERRIES = register("glow_berries", new CoffeeAddition(Items.GLOW_BERRIES, (stack, world, user) -> {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 1200));
        }, true, 0xFFD369, 0));
        HONEY = register("honey", new CoffeeAddition(Items.HONEY_BOTTLE, (stack, world, user) -> {
            if (user instanceof PlayerEntity) {
                ((PlayerEntity) user).getHungerManager().add(1, 0);
            }
        }, false, 0, 0));
        DIAMOND = register("diamond", new CoffeeAddition(Items.DIAMOND, (stack, world, user) -> {}, true, 0x29F6FF, 0));
        NETHERITE = register("netherite", new CoffeeAddition(Items.NETHERITE_INGOT, (stack, world, user) -> {}, true, 0x0B0B0B, 0));
    }

}
