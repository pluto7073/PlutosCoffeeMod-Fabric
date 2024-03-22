package ml.pluto7073.plutoscoffee.registry;

import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import ml.pluto7073.pdapi.item.PDItems;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModMisc {

    // Item Groups
    public static final RegistryKey<ItemGroup> PC_GROUP;

    public static void init() {}

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerRecipeSerializer(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(PlutosCoffee.MOD_ID, id), serializer);
    }

    static {
        // Item Groups
        PC_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(PlutosCoffee.MOD_ID, "pc_group"));
        Registry.register(Registries.ITEM_GROUP, PC_GROUP, FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.MEDIUM_ROAST_BEAN))
                .displayName(Text.translatable("itemGroup.plutoscoffee.pc_group"))
                .build());
        ItemGroupEvents.modifyEntriesEvent(PC_GROUP).register(stacks -> {
            stacks.add(new ItemStack(ModItems.COFFEE_BREWER, 1));
            stacks.add(new ItemStack(ModItems.COFFEE_GRINDR, 1));
            stacks.add(new ItemStack(ModItems.ESPRESSO_MACHINE, 1));
            stacks.add(new ItemStack(PDItems.DRINK_WORKSTATION, 1));
            stacks.add(new ItemStack(ModItems.COFFEE_BERRY, 1));
            stacks.add(new ItemStack(ModItems.COFFEE_BEAN, 1));
            stacks.add(new ItemStack(ModItems.LIGHT_ROAST_BEAN, 1));
            stacks.add(new ItemStack(ModItems.MEDIUM_ROAST_BEAN, 1));
            stacks.add(new ItemStack(ModItems.DARK_ROAST_BEAN, 1));
            stacks.add(new ItemStack(ModItems.ESPRESSO_ROAST_BEAN, 1));
            stacks.add(new ItemStack(ModItems.GROUND_LIGHT_ROAST, 1));
            stacks.add(new ItemStack(ModItems.GROUND_MEDIUM_ROAST, 1));
            stacks.add(new ItemStack(ModItems.GROUND_DARK_ROAST, 1));
            stacks.add(new ItemStack(ModItems.GROUND_ESPRESSO_ROAST, 1));
            ItemStack lightRoast = new ItemStack(ModItems.BREWED_COFFEE, 1);
            lightRoast.getOrCreateSubNbt(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).putString("CoffeeType", "light_roast");
            ItemStack mediumRoast = lightRoast.copy();
            mediumRoast.getOrCreateSubNbt(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).putString("CoffeeType", "medium_roast");
            ItemStack darkRoast = mediumRoast.copy();
            darkRoast.getOrCreateSubNbt(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).putString("CoffeeType", "dark_roast");
            stacks.add(lightRoast);
            stacks.add(mediumRoast);
            stacks.add(darkRoast);
            stacks.add(new ItemStack(ModItems.ESPRESSO_SHOT));
            stacks.add(new ItemStack(ModItems.CARAMEL));
            stacks.add(new ItemStack(PDItems.MILK_BOTTLE));
            stacks.add(new ItemStack(ModItems.MOCHA_SAUCE));
        });
    }

}
