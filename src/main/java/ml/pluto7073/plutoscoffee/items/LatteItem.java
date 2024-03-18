package ml.pluto7073.plutoscoffee.items;

import ml.pluto7073.plutoscoffee.CoffeeUtil;
import ml.pluto7073.plutoscoffee.coffee.CoffeeAddition;
import ml.pluto7073.plutoscoffee.coffee.CoffeeAdditions;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.registry.ModStats;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class LatteItem extends Item {

    private static final int MAX_USE_TIME = 32;

    public LatteItem(Settings settings) {
        super(settings);
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!world.isClient) {
            Collection<StatusEffectInstance> userStats = user.getStatusEffects();
            for (StatusEffectInstance effect : userStats) {
                if (!effect.getEffectType().isBeneficial())
                    user.removeStatusEffect(effect.getEffectType());
            }
            CoffeeAddition[] additions = CoffeeUtil.getCoffeeAddIns(stack);
            float caffeine = 0;
            if (additions != null) {
                for (CoffeeAddition addition : additions) {
                    addition.onDrink(stack, world, user);
                    caffeine += addition.getCaffeine();
                }
            }
            if (playerEntity != null) {
                float currentCaffeine = CoffeeUtil.getPlayerCaffeine(playerEntity);
                currentCaffeine += caffeine;
                CoffeeUtil.setPlayerCaffeine(playerEntity, currentCaffeine);
            }
        }

        if (playerEntity != null) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            playerEntity.incrementStat(ModStats.DRINK_COFFEE);
            if (!playerEntity.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }

        if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (playerEntity != null) {
                playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        user.emitGameEvent(GameEvent.DRINK);
        return stack;
    }

    public int getMaxUseTime(ItemStack stack) {
        return MAX_USE_TIME;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        float caffeine = 0;
        CoffeeAddition[] addIns = CoffeeUtil.getCoffeeAddIns(stack);
        HashMap<Identifier, Integer> additionCounts = new HashMap<>();
        if (addIns != null) {
            for (CoffeeAddition addIn : addIns) {
                if (addIn == CoffeeAdditions.EMPTY) continue;
                caffeine += addIn.getCaffeine();
                Identifier id = CoffeeAdditions.getId(addIn);
                if (additionCounts.containsKey(id)) {
                    int count = additionCounts.get(id);
                    additionCounts.put(id, ++count);
                } else additionCounts.put(id, 1);
            }
            tooltip.add(Text.translatable("tooltip.plutoscoffee.caffeine_content", caffeine).formatted(Formatting.AQUA));
            additionCounts.forEach((id, count) -> tooltip.add(Text.translatable(CoffeeAdditions.REGISTRY.get(id).getTranslationKey(), count).formatted(Formatting.GRAY)));
        }
    }

    public static ItemStack getStandardLatte() {
        ItemStack stack = new ItemStack(ModItems.LATTE);
        NbtList adds = new NbtList();
        adds.add(CoffeeUtil.stringAsNbt("plutoscoffee:espresso_shot"));
        adds.add(CoffeeUtil.stringAsNbt("plutoscoffee:espresso_shot"));
        stack.getOrCreateSubNbt("Coffee").put("Additions", adds);
        return stack;
    }

}
