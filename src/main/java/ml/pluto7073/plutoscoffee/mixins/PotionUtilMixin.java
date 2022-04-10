package ml.pluto7073.plutoscoffee.mixins;

import ml.pluto7073.plutoscoffee.Utils;
import ml.pluto7073.plutoscoffee.potions.ModPotions;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.PotionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(PotionUtil.class)
public class PotionUtilMixin {

    @Inject(at = @At("RETURN"), method = "getColor(Ljava/util/Collection;)I", cancellable = true)
    private static void getColorMixin(Collection<StatusEffectInstance> effects, CallbackInfoReturnable<Integer> cir) {
        if (Utils.collectionContainsOnlyAll(effects, ModPotions.COFFEE.getEffects())) {
            cir.setReturnValue(0x291e14);
        } else if (Utils.collectionContainsOnlyAll(effects, ModPotions.MILK_COFFEE.getEffects())) {
            cir.setReturnValue(0x7a5c3e);
        } else if (Utils.collectionContainsOnlyAll(effects, ModPotions.MOCHA.getEffects())) {
            cir.setReturnValue(0x24170b);
        } else if (Utils.collectionContainsOnlyAll(effects, ModPotions.CARAMEL_LATTE.getEffects())) {
            cir.setReturnValue(0x94704e);
        }
    }

}
