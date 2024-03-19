package ml.pluto7073.plutoscoffee.items;

import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import ml.pluto7073.plutoscoffee.CoffeeUtil;
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

public class BrewedCoffee extends AbstractCustomizableDrinkItem {

    public static final int DEFAULT_COLOUR = 0x160A02;
    public static final int COLOUR_WITH_MILK = 0x885737;

    public BrewedCoffee(Settings settings) {
        super(Items.GLASS_BOTTLE, Temperature.HOT, settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        return CoffeeUtil.setCoffeeType(super.getDefaultStack(), CoffeeTypes.MEDIUM_ROAST);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;

        if (!world.isClient) {
            CoffeeType type = CoffeeUtil.getCoffeeType(stack);
            type.onDrink(stack, world, user);
        }

        if (playerEntity != null) {
            playerEntity.incrementStat(ModStats.DRINK_COFFEE);
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (CoffeeUtil.getCoffeeType(stack) != CoffeeTypes.EMPTY
                && CoffeeUtil.getCoffeeType(stack) != null)
            tooltip.add(Text.translatable(CoffeeUtil.getCoffeeType(stack).getTranslationKey()).formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }

}
