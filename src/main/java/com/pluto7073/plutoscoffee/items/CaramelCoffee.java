package com.pluto7073.plutoscoffee.items;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class CaramelCoffee extends Item {
    public static final FoodComponent CaramelFood = (new FoodComponent.Builder()).alwaysEdible().hunger(6).saturationModifier(10.0f).build();

    public CaramelCoffee() {
        super(new Item.Settings().group(ItemGroup.FOOD).maxCount(16).recipeRemainder(Items.GLASS_BOTTLE).food(CaramelFood));
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        super.finishUsing(stack, world, user);
        if (user instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)user;
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        if (!world.isClient) {
            user.clearStatusEffects();
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 60));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 20 * 60));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 20 * 60));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 20 * 120, 5));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 60, 5));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 20 * 30, 5));
        }

        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (user instanceof PlayerEntity && !((PlayerEntity)user).isCreative()) {
                ItemStack itemStack = new ItemStack(Items.GLASS_BOTTLE);
                PlayerEntity playerEntity = (PlayerEntity)user;
                if (!playerEntity.getInventory().insertStack(itemStack)) {
                    playerEntity.dropItem(itemStack, false);
                }
            }

            return stack;
        }
    }

    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public SoundEvent getDrinkSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }

    public SoundEvent getEatSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
