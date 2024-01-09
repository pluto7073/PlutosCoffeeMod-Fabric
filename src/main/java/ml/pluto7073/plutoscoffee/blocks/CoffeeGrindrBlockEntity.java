package ml.pluto7073.plutoscoffee.blocks;

import ml.pluto7073.plutoscoffee.Utils;
import ml.pluto7073.plutoscoffee.coffee.CoffeeGrounds;
import ml.pluto7073.plutoscoffee.gui.CoffeeBrewerScreenHandler;
import ml.pluto7073.plutoscoffee.gui.CoffeeGrindrScreenHandler;
import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class CoffeeGrindrBlockEntity extends LockableContainerBlockEntity implements SidedInventory {

    private static final int INPUT_SLOT_INDEX = 0; // Beans go here
    private static final int OUTPUT_SLOT_INDEX = 1; // Grounds come out here
    private static final int[] TOP_SLOTS = {0};
    private static final int[] BOTTOM_SLOTS = {0,1};
    private static final int[] SIDE_SLOTS = {1};
    public static final int GRIND_TIME_PROPERTY_INDEX = 0;

    private DefaultedList<ItemStack> inventory;
    int grindTime;
    private boolean lastTickSlot;
    private Item itemGrinding;

    protected final PropertyDelegate propertyDelegate;


    public CoffeeGrindrBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.COFFEE_GRINDR_BLOCK_ENTITY_TYPE, blockPos, blockState);
        inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
        propertyDelegate = new PropertyDelegate() {
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
            public int size() {
                return 1;
            }
        };
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.coffee_grinder");
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    public static void tick(World world, BlockPos pos, BlockState state, CoffeeGrindrBlockEntity blockEntity) {
        boolean recipe = canCraft(blockEntity.inventory);
        boolean grinding = blockEntity.grindTime > 0;

        ItemStack inputStack = blockEntity.inventory.get(INPUT_SLOT_INDEX);
        if (grinding) {
            --blockEntity.grindTime;
            boolean done = blockEntity.grindTime == 0;
            if (done && recipe) {
                craft(world, pos, blockEntity.inventory);
                markDirty(world, pos, state);
            } else if (!recipe || !inputStack.isOf(blockEntity.itemGrinding)) {
                blockEntity.grindTime = 0;
                markDirty(world, pos, state);
            }
        } else if (recipe) {
            blockEntity.grindTime = 20;
            blockEntity.itemGrinding = inputStack.getItem();
            markDirty(world, pos, state);
        }

        boolean slotEmpty = blockEntity.getEmptySlot();
        if (slotEmpty != blockEntity.lastTickSlot) {
            blockEntity.lastTickSlot = slotEmpty;
            BlockState blockState = state;
            if (!(state.getBlock() instanceof CoffeeGrindrBlock)) {
                return;
            }

            blockState = blockState.with(CoffeeGrindrBlock.FULL_PROPERTY, slotEmpty);

            world.setBlockState(pos, blockState, 2);
        }
    }

    private boolean getEmptySlot() {
        return !inventory.get(1).isEmpty();
    }

    private static boolean canCraft(DefaultedList<ItemStack> slots) {
        ItemStack input = slots.get(INPUT_SLOT_INDEX);
        if (input.isEmpty()) return false;
        else if (!Utils.isItemACoffeeBean(input.getItem())) return false;
        else {
            ItemStack output = slots.get(OUTPUT_SLOT_INDEX);
            if (output.isEmpty()) return true;

            return output.isOf(CoffeeGrounds.getGrounds(input.getItem())) && output.getCount() <= output.getItem().getMaxCount() - 4;
        }
    }

    private static void craft(World world, BlockPos pos, DefaultedList<ItemStack> slots) {
        ItemStack input = slots.get(INPUT_SLOT_INDEX);

        Item groundsResult = CoffeeGrounds.getGrounds(input.getItem());
        ItemStack result = slots.get(OUTPUT_SLOT_INDEX);
        if (result.isOf(groundsResult)) {
            result.increment(4);
        }
        result = result.isEmpty() ? new ItemStack(groundsResult, 4) : result;
        slots.set(OUTPUT_SLOT_INDEX, result);

        input.decrement(1);

        slots.set(INPUT_SLOT_INDEX, input);
        world.syncWorldEvent(10002, pos, 0);
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, inventory);
        grindTime = nbt.getShort("GrindTime");
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putShort("GrindTime", (short) grindTime);
        Inventories.writeNbt(nbt, this.inventory);
    }

    public ItemStack getStack(int slot) {
        return slot >= 0 && slot < inventory.size() ? inventory.get(slot) : ItemStack.EMPTY;
    }

    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(inventory, slot, amount);
    }

    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    public void setStack(int slot, ItemStack stack) {
        if (slot >= 0 && slot < inventory.size()) {
            inventory.set(slot, stack);
        }
    }

    public boolean canPlayerUse(PlayerEntity player) {
        //noinspection DataFlowIssue
        if (world.getBlockEntity(pos) != this) {
            return false;
        } else {
            return !(player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 64.0);
        }
    }

    public boolean isValid(int slot, ItemStack stack) {
        if (slot == INPUT_SLOT_INDEX) {
            return Utils.isItemACoffeeBean(stack.getItem());
        } else {
            return false;
        }
    }

    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.UP) {
            return TOP_SLOTS;
        } else {
            return side == Direction.DOWN ? BOTTOM_SLOTS : SIDE_SLOTS;
        }
    }

    public boolean canInsert(int slot, ItemStack  stack, @Nullable Direction dir) {
        return isValid(slot, stack);
    }

    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot != INPUT_SLOT_INDEX;
    }

    public void clear() {
        inventory.clear();
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory inventory) {
        return new CoffeeGrindrScreenHandler(syncId, inventory, this, propertyDelegate);
    }

}
