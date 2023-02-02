package ml.pluto7073.plutoscoffee.blocks;

import ml.pluto7073.plutoscoffee.Utils;
import ml.pluto7073.plutoscoffee.coffee.CoffeeType;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import ml.pluto7073.plutoscoffee.gui.CoffeeBrewerScreenHandler;
import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.block.BlockState;
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

public class CoffeeBrewerBlockEntity extends LockableContainerBlockEntity implements SidedInventory {

    private static final int INPUT_SLOT_INDEX = 0; //Coffee Grounds Go Here
    private static final int FUEL_SLOT_INDEX = 1; //Water Bucket Goes Here
    private static final int[] TOP_SLOTS = {0};
    private static final int[] BOTTOM_SLOTS = {0, 2};
    private static final int[] SIDE_SLOTS = {1, 2};
    public static final int MAX_FUEL_USES = 3;
    public static final int BREW_TIME_PROPERTY_INDEX = 0;
    public static final int FUEL_PROPERTY_INDEX = 1;
    public static final int PROPERTY_COUNT = 2;
    private DefaultedList<ItemStack> inventory;
    int brewTime;
    private int lastTickSlot;
    private Item itemBrewing;
    int fuel;
    protected final PropertyDelegate propertyDelegate;

    public CoffeeBrewerBlockEntity(BlockPos pos, BlockState state) {
        super (ModBlocks.COFFEE_BREWER_BLOCK_ENTITY_TYPE, pos, state);
        inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
        propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> CoffeeBrewerBlockEntity.this.brewTime;
                    case 1 -> CoffeeBrewerBlockEntity.this.fuel;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CoffeeBrewerBlockEntity.this.brewTime = value;
                    case 1 -> CoffeeBrewerBlockEntity.this.fuel = value;
                }
            }

            public int size() {
                return 2;
            }
        };
    }

    protected Text getContainerName() {
        return Text.translatable("container.coffee_brewer");
    }

    public int size() {
        return inventory.size();
    }

    public boolean isEmpty() {
        Iterator<ItemStack> i = inventory.iterator();

        ItemStack itemStack;
        do {
            if (!i.hasNext()) {
                return true;
            }
            itemStack = i.next();
        } while (itemStack.isEmpty());
        return false;
    }

    public static void tick(World world, BlockPos pos, BlockState state, CoffeeBrewerBlockEntity blockEntity) {
        ItemStack fuelStack = blockEntity.inventory.get(FUEL_SLOT_INDEX);
        if (blockEntity.fuel <= 0 && fuelStack.isOf(Items.WATER_BUCKET)) {
            blockEntity.fuel = MAX_FUEL_USES;
            fuelStack = new ItemStack(Items.WATER_BUCKET.getRecipeRemainder(), 1);
            blockEntity.inventory.set(FUEL_SLOT_INDEX, fuelStack);
            markDirty(world, pos, state);
        }

        boolean recipe = canCraft(blockEntity.inventory);
        boolean brewing = blockEntity.brewTime > 0;

        ItemStack inputStack = blockEntity.inventory.get(INPUT_SLOT_INDEX);
        if (brewing) {
            --blockEntity.brewTime;
            boolean done = blockEntity.brewTime == 0;
            if (done && recipe) {
                craft(world, pos, blockEntity.inventory);
                markDirty(world, pos, state);
            } else if (!recipe || !inputStack.isOf(blockEntity.itemBrewing)) {
                blockEntity.brewTime = 0;
                markDirty(world, pos, state);
            }
        } else if (recipe && blockEntity.fuel > 0) {
            --blockEntity.fuel;
            blockEntity.brewTime = 600;
            blockEntity.itemBrewing = inputStack.getItem();
            markDirty(world, pos, state);
        }

        int slotEmpty = blockEntity.getEmptySlot();
        if (slotEmpty != blockEntity.lastTickSlot) {
            blockEntity.lastTickSlot = slotEmpty;
            BlockState blockState = state;
            if (!(state.getBlock() instanceof CoffeeBrewerBlock)) {
                return;
            }

            blockState = blockState.with(CoffeeBrewerBlock.BOTTLE_PROPERTY, slotEmpty);

            world.setBlockState(pos, blockState, 2);
        }
    }

    private int getEmptySlot() {
        if (inventory.get(2).isEmpty()) {
            return 0;
        } else if (inventory.get(2).isOf(Items.GLASS_BOTTLE)) {
            return 1;
        } else if (inventory.get(2).isOf(ModItems.BREWED_COFFEE)) {
            return 2;
        } else {
            return 0;
        }
    }

    private static boolean canCraft(DefaultedList<ItemStack> slots) {
        ItemStack input = slots.get(INPUT_SLOT_INDEX);
        if (input.isEmpty()) {
            return false;
        } else if (!Utils.isItemACoffeeGround(input.getItem())) {
            return false;
        } else {
            ItemStack base = slots.get(2);
            return base.isOf(Items.GLASS_BOTTLE);
        }
    }

    private static void craft(World world, BlockPos pos, DefaultedList<ItemStack> slots) {
        ItemStack input = slots.get(INPUT_SLOT_INDEX);

        CoffeeType type = CoffeeTypes.getFromGrounds(input.getItem());
        ItemStack result = new ItemStack(ModItems.BREWED_COFFEE, 1);
        result.getOrCreateSubNbt("Coffee").putString("CoffeeType", CoffeeTypes.getId(type));
        slots.set(2, result);

        input.decrement(1);

        slots.set(INPUT_SLOT_INDEX, input);
        world.syncWorldEvent(10001, pos, 0);
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, inventory);
        brewTime = nbt.getShort("BrewTime");
        fuel = nbt.getByte("Fuel");
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putShort("BrewTime", (short) brewTime);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putByte("Fuel", (byte) fuel);
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
        if (world.getBlockEntity(pos) != this) {
            return false;
        } else {
            return !(player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 64.0);
        }
    }

    public boolean isValid(int slot, ItemStack stack) {
        if (slot == INPUT_SLOT_INDEX) {
            return Utils.isItemACoffeeGround(stack.getItem());
        } else if (slot == FUEL_SLOT_INDEX) {
            return stack.isOf(Items.WATER_BUCKET);
        } else {
            return stack.isOf(Items.GLASS_BOTTLE) && getStack(slot).isEmpty();
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
        return new CoffeeBrewerScreenHandler(syncId, inventory, this, propertyDelegate);
    }

}
