package ml.pluto7073.plutoscoffee.mixins;

import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.potions.ModPotions;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public class PotionRecipeMixin {

    @Shadow
    private static void registerPotionRecipe(Potion input, Item item, Potion output) {
    }

    @Inject(at = @At("HEAD"), method = "registerDefaults()V")
    private static void registerDefaultsMixin(CallbackInfo info) {
        registerPotionRecipe(Potions.WATER, ModItems.GROUND_COFFEE, ModPotions.COFFEE);
        registerPotionRecipe(ModPotions.COFFEE, ModItems.MILK_BOTTLE, ModPotions.MILK_COFFEE);
        registerPotionRecipe(Potions.WATER, ModItems.CHOCOLATE_GROUND_COFFEE, ModPotions.MOCHA);
        registerPotionRecipe(ModPotions.MILK_COFFEE, ModItems.CARAMEL, ModPotions.CARAMEL_LATTE);
    }

}
