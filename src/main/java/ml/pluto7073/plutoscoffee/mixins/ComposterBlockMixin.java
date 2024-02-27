package ml.pluto7073.plutoscoffee.mixins;

import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemConvertible;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {

    @Shadow
    private static void registerCompostableItem(float levelIncreaseChance, ItemConvertible item) {
    }

    @Inject(at = @At("TAIL"), method = "registerDefaultCompostableItems")
    private static void teatime_registerModCompostItems(CallbackInfo ci) {
        registerCompostableItem(0.3F, ModItems.COFFEE_BERRY);
        registerCompostableItem(0.75F, ModItems.USED_COFFEE_GROUNDS);
        registerCompostableItem(0.3f, ModItems.GROUND_LIGHT_ROAST);
        registerCompostableItem(0.3f, ModItems.GROUND_MEDIUM_ROAST);
        registerCompostableItem(0.3f, ModItems.GROUND_DARK_ROAST);
    }

}
