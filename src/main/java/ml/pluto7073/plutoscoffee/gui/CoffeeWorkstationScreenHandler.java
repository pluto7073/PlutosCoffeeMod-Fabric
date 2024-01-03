package ml.pluto7073.plutoscoffee.gui;

import ml.pluto7073.plutoscoffee.recipes.CoffeeWorkstationRecipe;
import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModMisc;
import ml.pluto7073.plutoscoffee.registry.ModScreens;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class CoffeeWorkstationScreenHandler extends ForgingScreenHandler {

    private final World world;
    private RecipeEntry<CoffeeWorkstationRecipe> currentRecipe;
    private final List<RecipeEntry<CoffeeWorkstationRecipe>> recipes;

    public CoffeeWorkstationScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public CoffeeWorkstationScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreens.WORKSTATION_HANDLER_TYPE, syncId, playerInventory, context);
        this.world = playerInventory.player.getWorld();
        this.recipes = this.world.getRecipeManager().listAllOfType(ModMisc.COFFEE_WORK_RECIPE_TYPE);
    }

    @Override
    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return currentRecipe != null && currentRecipe.value().matches(input, world);
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraft(player.getWorld(), player, stack.getCount());
        this.output.unlockLastRecipe(player, List.of(getSlot(0).getStack(), getSlot(1).getStack()));
        decrementStack(0, player);
        decrementStack(1, player);
        context.run((world, pos) -> world.syncWorldEvent(10000, pos, 0));
    }

    @SuppressWarnings("DataFlowIssue")
    private void decrementStack(int slot, PlayerEntity player) {
        ItemStack itemStack = input.getStack(slot);
        if (itemStack.getCount() == 1) {
            if (itemStack.getItem().hasRecipeRemainder()) {
                itemStack = new ItemStack(itemStack.getItem().getRecipeRemainder(), 1);
            } else {
                itemStack.decrement(1);
            }
        } else if (itemStack.getCount() > 1) {
            itemStack.decrement(1);
            if (itemStack.getItem().hasRecipeRemainder()) {
                player.giveItemStack(new ItemStack(itemStack.getItem().getRecipeRemainder(), 1));
            }
        }
        input.setStack(slot, itemStack);
    }

    @Override
    protected boolean canUse(BlockState state) {
        return state.isOf(ModBlocks.COFFEE_WORKSTATION);
    }

    @Override
    public void updateResult() {
        List<RecipeEntry<CoffeeWorkstationRecipe>> list = world.getRecipeManager().getAllMatches(ModMisc.COFFEE_WORK_RECIPE_TYPE, input, world);
        if (list.isEmpty()) {
            output.setStack(0, ItemStack.EMPTY);
        } else {
            currentRecipe = list.get(0);
            ItemStack stack = currentRecipe.value().craft(input);
            output.setLastRecipe(currentRecipe);
            output.setStack(0, stack);
        }
    }

    @Override
    protected ForgingSlotsManager getForgingSlotsManager() {
        return ForgingSlotsManager.create()
                .input(0, 27, 47,
                        stack -> this.recipes.stream().anyMatch(recipe -> recipe.value().testBase(stack)))
                .input(1, 76, 47,
                        stack -> this.recipes.stream().anyMatch(recipe -> recipe.value().testAddition(stack)))
                .output(2, 134, 47).build();
    }

    private static Optional<Integer> getQuickMoveSlot(CoffeeWorkstationRecipe recipe, ItemStack stack) {
        if (recipe.testBase(stack)) {
            return Optional.of(0);
        } else {
            return recipe.testAddition(stack) ? Optional.of(1) : Optional.empty();
        }
    }

    @Override
    protected boolean isValidIngredient(ItemStack stack) {
        return this.recipes.stream().map((recipe) -> getQuickMoveSlot(recipe.value(), stack)).anyMatch(Optional::isPresent);
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != output && super.canInsertIntoSlot(stack, slot);
    }

}
