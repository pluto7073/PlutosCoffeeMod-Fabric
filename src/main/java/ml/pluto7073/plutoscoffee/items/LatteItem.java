package ml.pluto7073.plutoscoffee.items;

import ml.pluto7073.pdapi.DrinkUtil;
import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import ml.pluto7073.plutoscoffee.CoffeeUtil;
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

public class LatteItem extends AbstractCustomizableDrinkItem {

    public LatteItem(Settings settings) {
        super(Items.GLASS_BOTTLE, Temperature.HOT, settings);
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;

        if (playerEntity != null) {
            playerEntity.incrementStat(ModStats.DRINK_COFFEE);
        }

        return super.finishUsing(stack, world, user);
    }

    public static ItemStack getStandardLatte() {
        ItemStack stack = new ItemStack(ModItems.LATTE);
        NbtList adds = new NbtList();
        adds.add(DrinkUtil.stringAsNbt("plutoscoffee:espresso_shot"));
        adds.add(DrinkUtil.stringAsNbt("plutoscoffee:espresso_shot"));
        stack.getOrCreateSubNbt(DRINK_DATA_NBT_KEY).put("Additions", adds);
        return stack;
    }

}
