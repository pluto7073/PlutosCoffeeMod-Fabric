package ml.pluto7073.plutoscoffee.recipe;

import ml.pluto7073.plutoscoffee.blocks.EspressoMachineBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;

import static ml.pluto7073.plutoscoffee.blocks.EspressoMachineBlockEntity.*;

@MethodsReturnNonnullByDefault
public record PullingRecipeInput(ItemStack grounds, ItemStack leftBase, ItemStack rightBase) implements RecipeInput {

    public PullingRecipeInput(EspressoMachineBlockEntity entity) {
        this(entity.getItem(GROUNDS_SLOT_INDEX),
                entity.getItem(ESPRESSO_SLOT_INDEX),
                entity.getItem(ESPRESSO_SLOT_2_INDEX));
    }

    @Override
    public ItemStack getItem(int i) {
        return switch (i) {
            case 0 -> grounds;
            case 1 -> leftBase;
            case 2 -> rightBase;
            default -> throw new IllegalStateException("No item of index " + i);
        };
    }

    @Override
    public int size() {
        return 3;
    }
}
