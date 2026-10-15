package ml.pluto7073.plutoscoffee.items;

import ml.pluto7073.chemicals.component.ChemicalMap;
import ml.pluto7073.pdapi.PDAPI;
import ml.pluto7073.plutoscoffee.registry.ModStats;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

@MethodsReturnNonnullByDefault
public class EspressoShotItem extends Item {

    private static final int MAX_USE_TIME = 32;

    public EspressoShotItem(int caffeine, Item.Properties settings) {
        super(settings.component(ChemicalMap.COMPONENT_TYPE,
                new ChemicalMap.Builder().add(PDAPI.asId("caffeine"), caffeine).build()));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        Player player = user instanceof Player ? (Player) user : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
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

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return MAX_USE_TIME;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, user, hand);
    }

}
