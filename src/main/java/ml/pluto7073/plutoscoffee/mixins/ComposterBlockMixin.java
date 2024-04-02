package ml.pluto7073.plutoscoffee.mixins;

import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {

    @Shadow
    private static void add(float levelIncreaseChance, ItemLike item) {
    }

    @Inject(at = @At("TAIL"), method = "bootStrap")
    private static void teatime_registerModCompostItems(CallbackInfo ci) {
        add(0.3F, ModItems.COFFEE_BERRY);
        add(0.75F, ModItems.USED_COFFEE_GROUNDS);
        add(0.3f, ModItems.GROUND_LIGHT_ROAST);
        add(0.3f, ModItems.GROUND_MEDIUM_ROAST);
        add(0.3f, ModItems.GROUND_DARK_ROAST);
    }

}
