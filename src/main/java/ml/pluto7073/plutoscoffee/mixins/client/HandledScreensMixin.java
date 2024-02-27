package ml.pluto7073.plutoscoffee.mixins.client;

import ml.pluto7073.plutoscoffee.gui.CoffeeBrewerScreen;
import ml.pluto7073.plutoscoffee.gui.CoffeeGrindrScreen;
import ml.pluto7073.plutoscoffee.gui.CoffeeWorkstationScreen;
import ml.pluto7073.plutoscoffee.gui.EspressoMachineScreen;
import ml.pluto7073.plutoscoffee.registry.ModScreens;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreens.class)
public class HandledScreensMixin {

    @Shadow
    public static <M extends ScreenHandler, U extends Screen & ScreenHandlerProvider<M>> void register(ScreenHandlerType<? extends M> type, HandledScreens.Provider<M, U> provider) {
    }

    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void plutoscoffee_clInitInject(CallbackInfo ci) {
        register(ModScreens.BREWER_SCREEN_HANDLER_TYPE, CoffeeBrewerScreen::new);
        register(ModScreens.WORKSTATION_HANDLER_TYPE, CoffeeWorkstationScreen::new);
        register(ModScreens.GRINDR_SCREEN_HANDLER_TYPE, CoffeeGrindrScreen::new);
        register(ModScreens.ESPRESSO_SCREEN_HANDLER_TYPE, EspressoMachineScreen::new);
    }

}
