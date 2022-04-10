package ml.pluto7073.plutoscoffee.blocks;

import ml.pluto7073.plutoscoffee.recipes.CoffeeBrewingRecipe;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class CoffeeMachineBlockEntity extends AbstractFurnaceBlockEntity {

    public CoffeeMachineBlockEntity() {
        super(CoffeeBrewingRecipe.COFFEE_MACHINE_BLOCK_ENTITY, CoffeeBrewingRecipe.Type.INSTANCE);
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.plutoscoffee.coffee_machine");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new CoffeeMachineScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
