package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.gui.CoffeeBrewerMenu;
import ml.pluto7073.plutoscoffee.gui.CoffeeGrindrMenu;
import ml.pluto7073.plutoscoffee.gui.EspressoMachineScreenHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ModScreens {

    public static final MenuType<CoffeeBrewerMenu> BREWER_MENU_TYPE;
    public static final MenuType<CoffeeGrindrMenu> GRINDR_SCREEN_HANDLER_TYPE;
    public static final MenuType<EspressoMachineScreenHandler> ESPRESSO_SCREEN_HANDLER_TYPE;

    public static void init() {}

    private static <T extends AbstractContainerMenu> MenuType<T> register(String id, MenuType.MenuSupplier<T> factory) {
        return Registry.register(BuiltInRegistries.MENU, PlutosCoffee.asId(id), new MenuType<>(factory, FeatureFlags.VANILLA_SET));
    }

    static {
        BREWER_MENU_TYPE = register("coffee_brewer", CoffeeBrewerMenu::new);
        GRINDR_SCREEN_HANDLER_TYPE = register("coffee_grinder", CoffeeGrindrMenu::new);
        ESPRESSO_SCREEN_HANDLER_TYPE = register("espresso_machine", EspressoMachineScreenHandler::new);
    }

}
