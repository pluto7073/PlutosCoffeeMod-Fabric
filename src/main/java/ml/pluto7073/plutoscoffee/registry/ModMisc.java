package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import ml.pluto7073.pdapi.item.PDItems;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModMisc {

    // Item Groups
    public static final ResourceKey<CreativeModeTab> PC_GROUP;

    public static void init() {}

    static {
        // Item Groups
        PC_GROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB, PlutosCoffee.asId("pc_group"));
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, PC_GROUP, FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.MEDIUM_ROAST_BEAN))
                .title(Component.translatable("itemGroup.plutoscoffee.pc_group"))
                .build());
        ItemGroupEvents.modifyEntriesEvent(PC_GROUP).register(stacks -> {
            stacks.accept(new ItemStack(ModItems.COFFEE_BREWER, 1));
            stacks.accept(new ItemStack(ModItems.COFFEE_GRINDR, 1));
            stacks.accept(new ItemStack(ModItems.ESPRESSO_MACHINE, 1));
            stacks.accept(new ItemStack(PDItems.DRINK_WORKSTATION, 1));
            stacks.accept(new ItemStack(ModItems.COFFEE_BERRY, 1));
            stacks.accept(new ItemStack(ModItems.COFFEE_BEAN, 1));
            stacks.accept(new ItemStack(ModItems.LIGHT_ROAST_BEAN, 1));
            stacks.accept(new ItemStack(ModItems.MEDIUM_ROAST_BEAN, 1));
            stacks.accept(new ItemStack(ModItems.DARK_ROAST_BEAN, 1));
            stacks.accept(new ItemStack(ModItems.ESPRESSO_ROAST_BEAN, 1));
            stacks.accept(new ItemStack(ModItems.GROUND_LIGHT_ROAST, 1));
            stacks.accept(new ItemStack(ModItems.GROUND_MEDIUM_ROAST, 1));
            stacks.accept(new ItemStack(ModItems.GROUND_DARK_ROAST, 1));
            stacks.accept(new ItemStack(ModItems.GROUND_ESPRESSO_ROAST, 1));
            ItemStack lightRoast = new ItemStack(ModItems.BREWED_COFFEE, 1);
            lightRoast.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).putString("CoffeeType", "light_roast");
            ItemStack mediumRoast = lightRoast.copy();
            mediumRoast.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).putString("CoffeeType", "medium_roast");
            ItemStack darkRoast = mediumRoast.copy();
            darkRoast.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).putString("CoffeeType", "dark_roast");
            stacks.accept(lightRoast);
            stacks.accept(mediumRoast);
            stacks.accept(darkRoast);
            stacks.accept(new ItemStack(ModItems.ESPRESSO_SHOT));
            stacks.accept(new ItemStack(ModItems.CARAMEL));
            stacks.accept(new ItemStack(PDItems.MILK_BOTTLE));
            stacks.accept(new ItemStack(ModItems.MOCHA_SAUCE));
        });
    }

}
