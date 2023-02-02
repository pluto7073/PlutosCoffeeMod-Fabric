package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.gui.CoffeeBrewerScreenHandler;
import ml.pluto7073.plutoscoffee.gui.CoffeeWorkstationScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModScreens {

    public static final ScreenHandlerType<CoffeeWorkstationScreenHandler> WORKSTATION_HANDLER_TYPE;
    public static final ScreenHandlerType<CoffeeBrewerScreenHandler> BREWER_SCREEN_HANDLER_TYPE;

    public static void init() {}

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registry.SCREEN_HANDLER, new Identifier(PlutosCoffee.MOD_ID, id), new ScreenHandlerType<>(factory));
    }

    static {
        WORKSTATION_HANDLER_TYPE = register("coffee_workstation", CoffeeWorkstationScreenHandler::new);
        BREWER_SCREEN_HANDLER_TYPE = register("coffee_brewer", CoffeeBrewerScreenHandler::new);
    }

}
