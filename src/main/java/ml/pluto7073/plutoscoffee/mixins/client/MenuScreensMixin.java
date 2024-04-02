package ml.pluto7073.plutoscoffee.mixins.client;

import ml.pluto7073.plutoscoffee.gui.CoffeeBrewerScreen;
import ml.pluto7073.plutoscoffee.gui.CoffeeGrindrScreen;
import ml.pluto7073.plutoscoffee.gui.EspressoMachineScreen;
import ml.pluto7073.plutoscoffee.registry.ModScreens;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MenuScreens.class)
public class MenuScreensMixin {

    @Shadow
    public static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> type, MenuScreens.ScreenConstructor<M, U> provider) {
    }

    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void plutoscoffee_clInitInject(CallbackInfo ci) {
        register(ModScreens.BREWER_MENU_TYPE, CoffeeBrewerScreen::new);
        register(ModScreens.GRINDR_SCREEN_HANDLER_TYPE, CoffeeGrindrScreen::new);
        register(ModScreens.ESPRESSO_SCREEN_HANDLER_TYPE, EspressoMachineScreen::new);
    }

}
