package ml.pluto7073.plutoscoffee.items;

import ml.pluto7073.pdapi.component.DrinkAdditions;
import ml.pluto7073.pdapi.component.PDComponents;
import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.registry.ModStats;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;

@MethodsReturnNonnullByDefault
public class LatteItem extends AbstractCustomizableDrinkItem {

    public LatteItem(Properties settings) {
        super(Items.GLASS_BOTTLE, Temperature.HOT, settings);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        Player playerEntity = user instanceof Player ? (Player) user : null;

        if (playerEntity != null) {
            playerEntity.awardStat(ModStats.DRINK_COFFEE);
        }

        return super.finishUsingItem(stack, level, user);
    }

    public static ItemStack getStandardLatte() {
        ItemStack stack = new ItemStack(ModItems.LATTE);
        ResourceLocation shot = new ResourceLocation("plutoscoffee:espresso_shot");
        stack.set(PDComponents.ADDITIONS, DrinkAdditions.of(List.of(shot, shot)));
        return stack;
    }

}
