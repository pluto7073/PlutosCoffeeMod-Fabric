package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.gui.CoffeeBrewerScreenHandler;
import ml.pluto7073.plutoscoffee.gui.CoffeeGrindrScreenHandler;
import ml.pluto7073.plutoscoffee.gui.EspressoMachineScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreens {

    public static final ScreenHandlerType<CoffeeBrewerScreenHandler> BREWER_SCREEN_HANDLER_TYPE;
    public static final ScreenHandlerType<CoffeeGrindrScreenHandler> GRINDR_SCREEN_HANDLER_TYPE;
    public static final ScreenHandlerType<EspressoMachineScreenHandler> ESPRESSO_SCREEN_HANDLER_TYPE;

    public static void init() {}

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(PlutosCoffee.MOD_ID, id), new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    static {
        BREWER_SCREEN_HANDLER_TYPE = register("coffee_brewer", CoffeeBrewerScreenHandler::new);
        GRINDR_SCREEN_HANDLER_TYPE = register("coffee_grinder", CoffeeGrindrScreenHandler::new);
        ESPRESSO_SCREEN_HANDLER_TYPE = register("espresso_machine", EspressoMachineScreenHandler::new);
    }

}
