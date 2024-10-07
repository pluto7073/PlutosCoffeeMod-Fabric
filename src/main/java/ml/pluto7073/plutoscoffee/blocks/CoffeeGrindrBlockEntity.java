package ml.pluto7073.plutoscoffee.blocks;

import ml.pluto7073.plutoscoffee.CoffeeUtil;
import ml.pluto7073.plutoscoffee.coffee.CoffeeGrounds;
import ml.pluto7073.plutoscoffee.gui.CoffeeGrindrMenu;
import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
public class CoffeeGrindrBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    private static final int INPUT_SLOT_INDEX = 0; // Beans go here
    private static final int OUTPUT_SLOT_INDEX = 1; // Grounds come out here
    private static final int[] TOP_SLOTS = {0};
    private static final int[] BOTTOM_SLOTS = {0,1};
    private static final int[] SIDE_SLOTS = {1};
    public static final int GRIND_TIME_PROPERTY_INDEX = 0;

    private NonNullList<ItemStack> inventory;
    int grindTime;
    private boolean lastTickSlot;
    private Item itemGrinding;

    protected final ContainerData containerData;


    public CoffeeGrindrBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.COFFEE_GRINDR_BLOCK_ENTITY_TYPE, blockPos, blockState);
        inventory = NonNullList.withSize(2, ItemStack.EMPTY);
        containerData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case GRIND_TIME_PROPERTY_INDEX -> CoffeeGrindrBlockEntity.this.grindTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case GRIND_TIME_PROPERTY_INDEX -> CoffeeGrindrBlockEntity.this.grindTime = value;
                }
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.coffee_grinder");
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CoffeeGrindrBlockEntity blockEntity) {
        boolean recipe = canCraft(blockEntity.inventory);
        boolean grinding = blockEntity.grindTime > 0;

        ItemStack inputStack = blockEntity.inventory.get(INPUT_SLOT_INDEX);
        if (grinding) {
            --blockEntity.grindTime;
            boolean done = blockEntity.grindTime == 0;
            if (done && recipe) {
                craft(level, pos, blockEntity.inventory);
                setChanged(level, pos, state);
            } else if (!recipe || !inputStack.is(blockEntity.itemGrinding)) {
                blockEntity.grindTime = 0;
                setChanged(level, pos, state);
            }
        } else if (recipe) {
            blockEntity.grindTime = 20;
            blockEntity.itemGrinding = inputStack.getItem();
            setChanged(level, pos, state);
        }

        boolean slotEmpty = blockEntity.getEmptySlot();
        if (slotEmpty != blockEntity.lastTickSlot) {
            blockEntity.lastTickSlot = slotEmpty;
            BlockState blockState = state;
            if (!(state.getBlock() instanceof CoffeeGrindrBlock)) {
                return;
            }

            blockState = blockState.setValue(CoffeeGrindrBlock.FULL_PROPERTY, slotEmpty);

            level.setBlock(pos, blockState, 2);
        }
    }

    private boolean getEmptySlot() {
        return !inventory.get(1).isEmpty();
    }

    private static boolean canCraft(NonNullList<ItemStack> slots) {
        ItemStack input = slots.get(INPUT_SLOT_INDEX);
        if (input.isEmpty()) return false;
        else if (!CoffeeUtil.isItemACoffeeBean(input.getItem())) return false;
        else {
            ItemStack output = slots.get(OUTPUT_SLOT_INDEX);
            if (output.isEmpty()) return true;

            return output.is(CoffeeGrounds.getGrounds(input.getItem())) && output.getCount() <= output.getItem().getMaxStackSize() - 4;
        }
    }

    private static void craft(Level level, BlockPos pos, NonNullList<ItemStack> slots) {
        ItemStack input = slots.get(INPUT_SLOT_INDEX);

        Item groundsResult = CoffeeGrounds.getGrounds(input.getItem());
        ItemStack result = slots.get(OUTPUT_SLOT_INDEX);
        if (result.is(groundsResult)) {
            result.grow(4);
        }
        result = result.isEmpty() ? new ItemStack(groundsResult, 4) : result;
        slots.set(OUTPUT_SLOT_INDEX, result);

        input.shrink(1);

        slots.set(INPUT_SLOT_INDEX, input);
        level.levelEvent(10002, pos, 0);
    }

    public void load(CompoundTag nbt) {
        super.load(nbt);
        inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, inventory);
        grindTime = nbt.getShort("GrindTime");
    }

    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putShort("GrindTime", (short) grindTime);
        ContainerHelper.saveAllItems(nbt, this.inventory);
    }

    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < inventory.size() ? inventory.get(slot) : ItemStack.EMPTY;
    }

    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(inventory, slot, amount);
    }

    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(inventory, slot);
    }

    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < inventory.size()) {
            inventory.set(slot, stack);
        }
    }

    public boolean stillValid(Player player) {
        //noinspection DataFlowIssue
        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        } else {
            return !(player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) > 64.0);
        }
    }

    public boolean isValid(int slot, ItemStack stack) {
        if (slot == INPUT_SLOT_INDEX) {
            return CoffeeUtil.isItemACoffeeBean(stack.getItem());
        } else {
            return false;
        }
    }

    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) {
            return TOP_SLOTS;
        } else {
            return side == Direction.DOWN ? BOTTOM_SLOTS : SIDE_SLOTS;
        }
    }

    public boolean canPlaceItemThroughFace(int slot, ItemStack  stack, @Nullable Direction dir) {
        return isValid(slot, stack);
    }

    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return slot != INPUT_SLOT_INDEX;
    }

    public void clearContent() {
        inventory.clear();
    }

    protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        return new CoffeeGrindrMenu(syncId, inventory, this, containerData);
    }

}
