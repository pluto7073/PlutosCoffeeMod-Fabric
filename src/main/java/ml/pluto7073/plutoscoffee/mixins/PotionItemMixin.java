package ml.pluto7073.plutoscoffee.mixins;

import ml.pluto7073.plutoscoffee.potions.ModPotions;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(PotionItem.class)
public class PotionItemMixin {

    private static final Potion[] COFFEE_POTIONS = {
            ModPotions.COFFEE,
            ModPotions.MILK_COFFEE,
            ModPotions.MOCHA,
            ModPotions.CARAMEL_LATTE
    };
    private static final List<Potion> COFFEE_POTIONS_LIST = List.of(COFFEE_POTIONS);

    @Inject(at = @At("RETURN"), method = "hasGlint", cancellable = true)
    public void hasGlintMixin(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (COFFEE_POTIONS_LIST.contains(PotionUtil.getPotion(stack))) {
            cir.setReturnValue(false);
        }
    }

}
