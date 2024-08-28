package ml.pluto7073.plutoscoffee.items;

import ml.pluto7073.pdapi.DrinkUtil;
import ml.pluto7073.pdapi.addition.chemicals.ConsumableChemicalRegistry;
import ml.pluto7073.plutoscoffee.CoffeeUtil;
import ml.pluto7073.plutoscoffee.registry.ModStats;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@MethodsReturnNonnullByDefault
public class EspressoShotItem extends Item {

    private static final int MAX_USE_TIME = 32;

    protected final int caffeine;

    public EspressoShotItem(int caffeine, Item.Properties settings) {
        super(settings);
        this.caffeine = caffeine;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        Player player = user instanceof Player ? (Player) user : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
        }

        if (!level.isClientSide) {
            if (player != null) {
                ConsumableChemicalRegistry.CAFFEINE.add(player, caffeine);
            }
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            player.awardStat(ModStats.DRINK_COFFEE);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        if (player == null || !player.getAbilities().instabuild) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (player != null) {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        user.gameEvent(GameEvent.DRINK);
        return stack;
    }

    public int getUseDuration(ItemStack stack) {
        return MAX_USE_TIME;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, user, hand);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context) {
        if (context.isCreative() || context.isAdvanced()) tooltip.add(Component.translatable("tooltip.pdapi.caffeine_content", caffeine).withStyle(ChatFormatting.AQUA));
    }

}
