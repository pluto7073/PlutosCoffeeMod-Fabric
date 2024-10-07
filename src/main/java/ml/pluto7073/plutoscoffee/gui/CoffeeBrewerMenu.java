package ml.pluto7073.plutoscoffee.gui;

import ml.pluto7073.plutoscoffee.CoffeeUtil;
import ml.pluto7073.plutoscoffee.blocks.CoffeeBrewerBlockEntity;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSources;
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
import net.minecraft.world.item.Items;

@MethodsReturnNonnullByDefault
public class CoffeeBrewerMenu extends AbstractContainerMenu {

    private final Container inventory;
    private final ContainerData propertyDelegate;
    private final Slot ingredientSlot;

    public CoffeeBrewerMenu(int syncId, Inventory inventory) {
        this(syncId, inventory, new SimpleContainer(3), new SimpleContainerData(2));
    }

    public CoffeeBrewerMenu(int syncId, Inventory inventory, Container container, ContainerData containerData) {
        super(ModScreens.BREWER_MENU_TYPE, syncId);
        checkContainerSize(container, 3);
        checkContainerDataCount(containerData, CoffeeBrewerBlockEntity.PROPERTY_COUNT);
        this.inventory = container;
        this.propertyDelegate = containerData;
        //Input Slots
        this.addSlot(new CoffeeSlot(container, 2, 79, 58));
        this.ingredientSlot = this.addSlot(new IngredientSlot(container, 0, 79, 17));
        this.addSlot(new FuelSlot(container, 1, 17, 17));
        this.addDataSlots(containerData);

        //Player Inventory & Hotbar Slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    public boolean stillValid(Player player) {
        return inventory.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index != 2 && index != 1 && index != 0) { // Source is from Player Inventory or Hotbar
                if (CoffeeBrewerMenu.FuelSlot.matches(stack)) { // To Water Slot
                    if (this.moveItemStackTo(slotStack, 1, 2, false) || this.ingredientSlot.mayPlace(slotStack) && !this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (ingredientSlot.mayPlace(slotStack)) { // To Ingredient Slot
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (CoffeeBrewerMenu.CoffeeSlot.matches(stack) && stack.getCount() == 1) { // To Coffee Grounds Slot
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) { // Player Inventory to Hotbar
                    if (!moveItemStackTo(slotStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39) { // Hotbar to Player Inventory
                    if (!moveItemStackTo(slotStack, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!moveItemStackTo(slotStack, 3, 39, false)) { // To Next Available Slot In Player Inventory or Hotbar
                    return ItemStack.EMPTY;
                }
            } else { // Source is from Coffee Brewer
                if (!moveItemStackTo(slotStack, 3, 39, true)) { // To Anywhere in Player Inventory or Hotbar
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

    public int getFuel() {
        return this.propertyDelegate.get(1);
    }

    public int getBrewTime() {
        return this.propertyDelegate.get(0);
    }

    static class CoffeeSlot extends Slot {

        public CoffeeSlot(Container container, int i, int j, int k) {
            super (container, i, j, k);
        }

        public boolean mayPlace(ItemStack stack) {
            return matches(stack);
        }

        public int getMaxStackSize() {
            return 1;
        }

        public static boolean matches(ItemStack stack) {
            return stack.is(Items.GLASS_BOTTLE);
        }

    }

    private static class IngredientSlot extends Slot {

        public IngredientSlot(Container container, int i, int j, int k) {
            super(container, i, j, k);
        }

        public boolean mayPlace(ItemStack stack) {
            return CoffeeUtil.isItemACoffeeGround(stack.getItem());
        }

        public int getMaxStackSize() {
            return 64;
        }

    }

    private static class FuelSlot extends Slot {

        public FuelSlot(Container container, int i, int j, int k) {
            super(container, i, j, k);
        }

        public boolean mayPlace(ItemStack stack) {
            return matches(stack);
        }

        public static boolean matches(ItemStack stack) {
            return MachineWaterSources.getWaterAmount(stack) > 0;
        }

        public int getMaxStackSize() {
            return 1;
        }

    }

}
