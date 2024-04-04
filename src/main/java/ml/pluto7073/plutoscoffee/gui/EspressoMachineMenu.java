package ml.pluto7073.plutoscoffee.gui;

import ml.pluto7073.pdapi.tag.PDTags;
import ml.pluto7073.plutoscoffee.blocks.EspressoMachineBlockEntity;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSources;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.registry.ModScreens;
import ml.pluto7073.plutoscoffee.tags.ModItemTags;
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
public class EspressoMachineMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData containerData;
    private final Slot groundsSlot;

    public EspressoMachineMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(EspressoMachineBlockEntity.INVENTORY_SIZE), new SimpleContainerData(EspressoMachineBlockEntity.PROPERTY_COUNT));
    }

    public EspressoMachineMenu(int syncId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(ModScreens.ESPRESSO_SCREEN_HANDLER_TYPE, syncId);
        checkContainerSize(container, EspressoMachineBlockEntity.INVENTORY_SIZE);
        this.container = container;
        this.containerData = containerData;
        //Input Slots
        this.addSlot(new EspressoSlot(container, EspressoMachineBlockEntity.ESPRESSO_SLOT_INDEX, 67, 58));
        this.addSlot(new EspressoSlot(container, EspressoMachineBlockEntity.ESPRESSO_SLOT_2_INDEX, 91, 58));
        this.groundsSlot = this.addSlot(new GroundsSlot(container, EspressoMachineBlockEntity.GROUNDS_SLOT_INDEX, 79, 17));
        this.addSlot(new WaterSlot(container, EspressoMachineBlockEntity.WATER_SLOT_INDEX, 17, 17));
        this.addSlot(new TrashSlot(container, EspressoMachineBlockEntity.TRASH_SLOT_INDEX, 17, 46));
        this.addSlot(new MilkSlot(container, EspressoMachineBlockEntity.MILK_SLOT_INDEX, 136, 26));
        this.addDataSlots(containerData);

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

    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY; // TODO
    }

    public int getWater() {
        return this.containerData.get(EspressoMachineBlockEntity.WATER_PROPERTY_INDEX);
    }

    public int getPullTime() {
        return this.containerData.get(EspressoMachineBlockEntity.PULL_TIME_PROPERTY_INDEX);
    }

    public int getSteamTime() {
        return this.containerData.get(EspressoMachineBlockEntity.STEAM_TIME_PROPERTY_INDEX);
    }

    private static class EspressoSlot extends Slot {
        public EspressoSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return true;
        }

        public int getMaxStackSize() {
            return 1;
        }

    }

    private static class GroundsSlot extends Slot {
        public GroundsSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return stack.is(ModItemTags.COFFEE_GROUNDS);
        }

        public int getMaxStackSize() {
            return 64;
        }
    }

    private static class WaterSlot extends Slot {
        public WaterSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
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

    private static class TrashSlot extends Slot {
        public TrashSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        public int getMaxStackSize() { return 64; }
    }

    private static class MilkSlot extends Slot {
        public MilkSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return stack.is(PDTags.MILK_BOTTLES) || stack.is(ModItems.LATTE);
        }

        public int getMaxStackSize() { return 1; }
    }

}
