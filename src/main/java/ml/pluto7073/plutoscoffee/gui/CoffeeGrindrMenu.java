package ml.pluto7073.plutoscoffee.gui;

import ml.pluto7073.plutoscoffee.CoffeeUtil;
import ml.pluto7073.plutoscoffee.registry.ModScreens;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@MethodsReturnNonnullByDefault
public class CoffeeGrindrMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData containerData;
    private final Slot ingredientSlot;

    public CoffeeGrindrMenu(int syncId, Inventory inventory) {
        this(syncId, inventory, new SimpleContainer(2), new SimpleContainerData(1));
    }

    public CoffeeGrindrMenu(int syncId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(ModScreens.GRINDR_SCREEN_HANDLER_TYPE, syncId);
        checkContainerSize(container, 2);
        this.container = container;
        this.containerData = containerData;
        // Input Slot
        this.addSlot(new CoffeeGrindrMenu.GroundsSlot(container, 1, 79, 58));
        this.ingredientSlot = this.addSlot(new CoffeeGrindrMenu.IngredientSlot(container, 0, 79, 17));
        this.addDataSlots(containerData);

        // Player Inventory & Hotbar Slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index != 1 && index != 0) { // Source is from Player Inventory or Hotbar
                if (ingredientSlot.mayPlace(slotStack)) { // To Ingredient Slot
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (CoffeeGrindrMenu.GroundsSlot.matches(stack) && stack.getCount() == 1) { // To Coffee Grounds Slot
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 2 && index < 29) { // Player Inventory to Hotbar
                    if (!moveItemStackTo(slotStack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 29 && index < 38) { // Hotbar to Player Inventory
                    if (!moveItemStackTo(slotStack, 2, 29, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!moveItemStackTo(slotStack, 2, 38, false)) { // To Next Available Slot In Player Inventory or Hotbar
                    return ItemStack.EMPTY;
                }
            } else { // Source is from Coffee Brewer
                if (!moveItemStackTo(slotStack, 2, 38, true)) { // To Anywhere in Player Inventory or Hotbar
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, stack);
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return stack;
    }

    public int getGrindTime() {
        return this.containerData.get(0);
    }

    static class GroundsSlot extends Slot {
        public GroundsSlot(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        public int getMaxStackSize() {
            return 64;
        }

        public static boolean matches(ItemStack stack) {
            return CoffeeUtil.isItemACoffeeGround(stack.getItem());
        }
    }

    private static class IngredientSlot extends Slot {

        public IngredientSlot(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return CoffeeUtil.isItemACoffeeBean(stack.getItem());
        }

        public int getMaxStackSize() {
            return 64;
        }
    }

}

