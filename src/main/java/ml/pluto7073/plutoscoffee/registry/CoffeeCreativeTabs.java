package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import ml.pluto7073.pdapi.item.PDItems;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CoffeeCreativeTabs {

    // Item Groups
    public static final ResourceKey<CreativeModeTab> PC_GROUP;

    public static void init() {}

    static {
        // Item Groups
        PC_GROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB, PlutosCoffee.asId("pc_group"));
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, PC_GROUP, FabricItemGroup.builder().icon(() -> new ItemStack(CoffeeItems.MEDIUM_ROAST_BEAN))
                .title(Component.translatable("itemGroup.plutoscoffee.pc_group"))
                .build());
        ItemGroupEvents.modifyEntriesEvent(PC_GROUP).register(stacks -> {
            stacks.accept(new ItemStack(CoffeeItems.COFFEE_BREWER, 1));
            stacks.accept(new ItemStack(CoffeeItems.COFFEE_GRINDR, 1));
            stacks.accept(new ItemStack(CoffeeItems.ESPRESSO_MACHINE, 1));
            stacks.accept(new ItemStack(PDItems.DRINK_WORKSTATION, 1));
            stacks.accept(new ItemStack(CoffeeItems.COFFEE_CHERRY, 1));
            stacks.accept(new ItemStack(CoffeeItems.COFFEE_BEAN, 1));
            stacks.accept(new ItemStack(CoffeeItems.LIGHT_ROAST_BEAN, 1));
            stacks.accept(new ItemStack(CoffeeItems.MEDIUM_ROAST_BEAN, 1));
            stacks.accept(new ItemStack(CoffeeItems.DARK_ROAST_BEAN, 1));
            stacks.accept(new ItemStack(CoffeeItems.ESPRESSO_ROAST_BEAN, 1));
            stacks.accept(new ItemStack(CoffeeItems.DECAF_ROAST_BEAN));
            stacks.accept(new ItemStack(CoffeeItems.GROUND_LIGHT_ROAST, 1));
            stacks.accept(new ItemStack(CoffeeItems.GROUND_MEDIUM_ROAST, 1));
            stacks.accept(new ItemStack(CoffeeItems.GROUND_DARK_ROAST, 1));
            stacks.accept(new ItemStack(CoffeeItems.GROUND_ESPRESSO_ROAST, 1));
            stacks.accept(new ItemStack(CoffeeItems.GROUND_DECAF_ROAST));
            ItemStack lightRoast = new ItemStack(CoffeeItems.BREWED_COFFEE, 1);
            lightRoast.set(ModComponents.COFFEE_TYPE, CoffeeTypes.LIGHT_ROAST);
            ItemStack mediumRoast = lightRoast.copy();
            mediumRoast.set(ModComponents.COFFEE_TYPE, CoffeeTypes.MEDIUM_ROAST);
            ItemStack darkRoast = mediumRoast.copy();
            darkRoast.set(ModComponents.COFFEE_TYPE, CoffeeTypes.DARK_ROAST);
            stacks.accept(lightRoast);
            stacks.accept(mediumRoast);
            stacks.accept(darkRoast);
            stacks.accept(new ItemStack(CoffeeItems.BLONDE_ESPRESSO_SHOT));
            stacks.accept(new ItemStack(CoffeeItems.ESPRESSO_SHOT));
            stacks.accept(new ItemStack(CoffeeItems.DECAF_ESPRESSO_SHOT));
            stacks.accept(new ItemStack(CoffeeItems.CARAMEL));
            stacks.accept(new ItemStack(PDItems.MILK_BOTTLE));
            stacks.accept(new ItemStack(CoffeeItems.MOCHA_SAUCE));
        });
    }

}
