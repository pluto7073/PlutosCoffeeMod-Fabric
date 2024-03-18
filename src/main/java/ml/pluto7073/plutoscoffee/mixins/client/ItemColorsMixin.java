package ml.pluto7073.plutoscoffee.mixins.client;

import ml.pluto7073.plutoscoffee.CoffeeUtil;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemColors.class)
@Environment(EnvType.CLIENT)
public class ItemColorsMixin {

    @Inject(at = @At("TAIL"), method = "create", cancellable = true)
    private static void plutoscoffee_createInject(BlockColors blockColors, CallbackInfoReturnable<ItemColors> cir) {
        ItemColors itemColors = cir.getReturnValue();
        itemColors.register((stack, tintIndex) -> tintIndex > 0 ? -1 : CoffeeUtil.getCoffeeColour(stack), ModItems.BREWED_COFFEE);
        itemColors.register((stack, tintIndex) -> tintIndex > 0 ? -1 : CoffeeUtil.getLatteColour(stack), ModItems.LATTE);
        cir.setReturnValue(itemColors);
    }

}
