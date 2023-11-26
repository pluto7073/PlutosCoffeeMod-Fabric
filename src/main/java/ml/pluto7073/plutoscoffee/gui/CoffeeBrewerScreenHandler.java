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

public class CoffeeBrewerScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final Slot ingredientSlot;

    public CoffeeBrewerScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new SimpleInventory(3), new ArrayPropertyDelegate(2));
    }

    public CoffeeBrewerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreens.BREWER_SCREEN_HANDLER_TYPE, syncId);
        checkSize(inventory, 3);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        //Input Slots
        this.addSlot(new CoffeeSlot(inventory, 2, 79, 58));
        this.ingredientSlot = this.addSlot(new IngredientSlot(inventory, 0, 79, 17));
        this.addSlot(new FuelSlot(inventory, 1, 17, 17));
        this.addProperties(propertyDelegate);

        //Player Inventory & Hotbar Slots
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
            if (index != 2 && index != 1 && index != 0) { // Source is from Player Inventory or Hotbar
                if (CoffeeBrewerScreenHandler.FuelSlot.matches(stack)) { // To Water Slot
                    if (this.insertItem(slotStack, 1, 2, false) || this.ingredientSlot.canInsert(slotStack) && !this.insertItem(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (ingredientSlot.canInsert(slotStack)) { // To Ingredient Slot
                    if (!this.insertItem(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (CoffeeBrewerScreenHandler.CoffeeSlot.matches(stack) && stack.getCount() == 1) { // To Coffee Grounds Slot
                    if (!this.insertItem(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) { // Player Inventory to Hotbar
                    if (!insertItem(slotStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39) { // Hotbar to Player Inventory
                    if (!insertItem(slotStack, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!insertItem(slotStack, 3, 39, false)) { // To Next Available Slot In Player Inventory or Hotbar
                    return ItemStack.EMPTY;
                }
            } else { // Source is from Coffee Brewer
                if (!insertItem(slotStack, 3, 39, true)) { // To Anywhere in Player Inventory or Hotbar
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

    public int getFuel() {
        return this.propertyDelegate.get(1);
    }

    public int getBrewTime() {
        return this.propertyDelegate.get(0);
    }

    static class CoffeeSlot extends Slot {

        public CoffeeSlot(Inventory inventory, int i, int j, int k) {
            super (inventory, i, j, k);
        }

        public boolean canInsert(ItemStack stack) {
            return matches(stack);
        }

        public int getMaxItemCount() {
            return 1;
        }

        public static boolean matches(ItemStack stack) {
            return stack.isOf(Items.GLASS_BOTTLE);
        }

    }

    private static class IngredientSlot extends Slot {

        public IngredientSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        public boolean canInsert(ItemStack stack) {
            return Utils.isItemACoffeeGround(stack.getItem());
        }

        public int getMaxItemCount() {
            return 64;
        }

    }

    private static class FuelSlot extends Slot {

        public FuelSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        public boolean canInsert(ItemStack stack) {
            return matches(stack);
        }

        public static boolean matches(ItemStack stack) {
            return stack.isOf(Items.WATER_BUCKET);
        }

        public int getMaxItemCount() {
            return 1;
        }

    }

}
