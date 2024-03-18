package ml.pluto7073.plutoscoffee.items;

import ml.pluto7073.plutoscoffee.CoffeeUtil;
import ml.pluto7073.plutoscoffee.coffee.CoffeeAddition;
import ml.pluto7073.plutoscoffee.coffee.CoffeeAdditions;
import ml.pluto7073.plutoscoffee.coffee.CoffeeType;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import ml.pluto7073.plutoscoffee.registry.ModStats;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class BrewedCoffee extends Item {

    private static final int MAX_USE_TIME = 32;

    public static final int DEFAULT_COLOUR = 0x160A02;
    public static final int COLOUR_WITH_MILK = 0x885737;

    public BrewedCoffee(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        return CoffeeUtil.setCoffeeType(super.getDefaultStack(), CoffeeTypes.MEDIUM_ROAST);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!world.isClient) {
            CoffeeType type = CoffeeUtil.getCoffeeType(stack);
            type.onDrink(stack, world, user);
            CoffeeAddition[] additions = CoffeeUtil.getCoffeeAddIns(stack);
            float caffeine = type.getCaffeineContent();
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

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return MAX_USE_TIME;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        float caffeine = 0;
        if (CoffeeUtil.getCoffeeType(stack) != CoffeeTypes.EMPTY && CoffeeUtil.getCoffeeType(stack) != null) {
            caffeine += CoffeeUtil.getCoffeeType(stack).getCaffeineContent();
        }
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
            if (CoffeeUtil.getCoffeeType(stack) != CoffeeTypes.EMPTY && CoffeeUtil.getCoffeeType(stack) != null) tooltip.add(Text.translatable(CoffeeUtil.getCoffeeType(stack).getTranslationKey()).formatted(Formatting.GRAY));
            additionCounts.forEach((id, count) -> tooltip.add(Text.translatable(CoffeeAdditions.REGISTRY.get(id).getTranslationKey(), count).formatted(Formatting.GRAY)));
        }
        tooltip.add(Text.translatable("tooltip.plutoscoffee.caffeine_content", caffeine).formatted(Formatting.AQUA));
    }

}
