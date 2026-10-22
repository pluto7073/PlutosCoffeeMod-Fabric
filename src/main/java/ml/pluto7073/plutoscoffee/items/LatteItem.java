package ml.pluto7073.plutoscoffee.items;

import ml.pluto7073.pdapi.component.DrinkAdditions;
import ml.pluto7073.pdapi.component.PDComponents;
import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.registry.CoffeeItems;
import ml.pluto7073.plutoscoffee.registry.CoffeeStats;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;

@MethodsReturnNonnullByDefault
public class LatteItem extends AbstractCustomizableDrinkItem {

    public LatteItem(Properties settings) {
        super(Items.GLASS_BOTTLE, 10, settings);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        Player playerEntity = user instanceof Player ? (Player) user : null;

        if (playerEntity != null) {
            playerEntity.awardStat(CoffeeStats.DRINK_COFFEE);
        }

        return super.finishUsingItem(stack, level, user);
    }

}
