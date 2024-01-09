package ml.pluto7073.plutoscoffee.gui;

import ml.pluto7073.plutoscoffee.Utils;
import ml.pluto7073.plutoscoffee.registry.ModScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class CoffeeGrindrScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final Slot ingredientSlot;

    public CoffeeGrindrScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new SimpleInventory(2), new ArrayPropertyDelegate(1));
    }

    public CoffeeGrindrScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreens.GRINDR_SCREEN_HANDLER_TYPE, syncId);
        checkSize(inventory, 2);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        // Input Slot
        this.addSlot(new CoffeeGrindrScreenHandler.GroundsSlot(inventory, 1, 79, 58));
        this.ingredientSlot = this.addSlot(new CoffeeGrindrScreenHandler.IngredientSlot(inventory, 0, 79, 17));
        this.addProperties(propertyDelegate);

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

    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index != 1 && index != 0) { // Source is from Player Inventory or Hotbar
                if (ingredientSlot.canInsert(slotStack)) { // To Ingredient Slot
                    if (!this.insertItem(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (CoffeeGrindrScreenHandler.GroundsSlot.matches(stack) && stack.getCount() == 1) { // To Coffee Grounds Slot
                    if (!this.insertItem(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 2 && index < 29) { // Player Inventory to Hotbar
                    if (!insertItem(slotStack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 29 && index < 38) { // Hotbar to Player Inventory
                    if (!insertItem(slotStack, 2, 29, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!insertItem(slotStack, 2, 38, false)) { // To Next Available Slot In Player Inventory or Hotbar
                    return ItemStack.EMPTY;
                }
            } else { // Source is from Coffee Brewer
                if (!insertItem(slotStack, 2, 38, true)) { // To Anywhere in Player Inventory or Hotbar
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(slotStack, stack);
            }

            if (slotStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, slotStack);
        }

        return stack;
    }

    public int getGrindTime() {
        return this.propertyDelegate.get(0);
    }

    static class GroundsSlot extends Slot {
        public GroundsSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return false;
        }

        public int getMaxItemCount() {
            return 64;
        }

        public static boolean matches(ItemStack stack) {
            return Utils.isItemACoffeeGround(stack.getItem());
        }
    }

    private static class IngredientSlot extends Slot {

        public IngredientSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return Utils.isItemACoffeeBean(stack.getItem());
        }

        public int getMaxItemCount() {
            return 64;
        }
    }

}
