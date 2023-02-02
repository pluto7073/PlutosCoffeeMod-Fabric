package ml.pluto7073.plutoscoffee.coffee;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface OnDrink {

    void onDrink(ItemStack stack, World world, LivingEntity user);

}
