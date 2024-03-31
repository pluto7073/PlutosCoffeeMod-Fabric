package ml.pluto7073.plutoscoffee.gui;

import ml.pluto7073.pdapi.item.PDItems;
import ml.pluto7073.pdapi.tag.PDTags;
import ml.pluto7073.plutoscoffee.blocks.EspressoMachineBlockEntity;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.registry.ModScreens;
import ml.pluto7073.plutoscoffee.tags.ModItemTags;
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

public class EspressoMachineScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final Slot groundsSlot;

    public EspressoMachineScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(EspressoMachineBlockEntity.INVENTORY_SIZE), new ArrayPropertyDelegate(EspressoMachineBlockEntity.PROPERTY_COUNT));
    }

    public EspressoMachineScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreens.ESPRESSO_SCREEN_HANDLER_TYPE, syncId);
        checkSize(inventory, EspressoMachineBlockEntity.INVENTORY_SIZE);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        //Input Slots
        this.addSlot(new EspressoSlot(inventory, EspressoMachineBlockEntity.ESPRESSO_SLOT_INDEX, 67, 58));
        this.addSlot(new EspressoSlot(inventory, EspressoMachineBlockEntity.ESPRESSO_SLOT_2_INDEX, 91, 58));
        this.groundsSlot = this.addSlot(new GroundsSlot(inventory, EspressoMachineBlockEntity.GROUNDS_SLOT_INDEX, 79, 17));
        this.addSlot(new WaterSlot(inventory, EspressoMachineBlockEntity.WATER_SLOT_INDEX, 17, 17));
        this.addSlot(new TrashSlot(inventory, EspressoMachineBlockEntity.TRASH_SLOT_INDEX, 17, 46));
        this.addSlot(new MilkSlot(inventory, EspressoMachineBlockEntity.MILK_SLOT_INDEX, 136, 26));
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
        return ItemStack.EMPTY; // TODO
    }

    public int getWater() {
        return this.propertyDelegate.get(EspressoMachineBlockEntity.WATER_PROPERTY_INDEX);
    }

    public int getPullTime() {
        return this.propertyDelegate.get(EspressoMachineBlockEntity.PULL_TIME_PROPERTY_INDEX);
    }

    public int getSteamTime() {
        return this.propertyDelegate.get(EspressoMachineBlockEntity.STEAM_TIME_PROPERTY_INDEX);
    }

    private static class EspressoSlot extends Slot {
        public EspressoSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return true;
        }

        public int getMaxItemCount() {
            return 1;
        }

    }

    private static class GroundsSlot extends Slot {
        public GroundsSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return stack.isIn(ModItemTags.COFFEE_GROUNDS);
        }

        public int getMaxItemCount() {
            return 64;
        }
    }

    private static class WaterSlot extends Slot {
        public WaterSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
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

    private static class TrashSlot extends Slot {
        public TrashSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return false;
        }

        public int getMaxItemCount() { return 64; }
    }

    private static class MilkSlot extends Slot {
        public MilkSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return stack.isIn(PDTags.MILK_BOTTLES) || stack.isOf(ModItems.LATTE);
        }

        public int getMaxItemCount() { return 1; }
    }

}
