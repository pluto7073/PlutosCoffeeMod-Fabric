package ml.pluto7073.plutoscoffee.blocks;

import ml.pluto7073.plutoscoffee.gui.CoffeeBrewerScreenHandler;
import ml.pluto7073.plutoscoffee.gui.EspressoMachineScreenHandler;
import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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

public class EspressoMachineBlockEntity extends LockableContainerBlockEntity implements SidedInventory {

    public static final int GROUNDS_SLOT_INDEX = 0;
    public static final int WATER_SLOT_INDEX = 1;
    public static final int ESPRESSO_SLOT_INDEX = 2;
    public static final int ESPRESSO_SLOT_2_INDEX = 3;
    public static final int TRASH_SLOT_INDEX = 4;
    public static final int MILK_SLOT_INDEX = 5;
    public static final int INVENTORY_SIZE = 6;
    private static final int[] TOP_SLOTS = { GROUNDS_SLOT_INDEX, MILK_SLOT_INDEX };
    private static final int[] BOTTOM_SLOTS = { GROUNDS_SLOT_INDEX, MILK_SLOT_INDEX, ESPRESSO_SLOT_INDEX, TRASH_SLOT_INDEX };
    private static final int[] SIDE_SLOTS = { WATER_SLOT_INDEX, MILK_SLOT_INDEX, ESPRESSO_SLOT_INDEX };
    public static final int MAX_WATER_USES = 24;
    public static final int PULL_TIME_PROPERTY_INDEX = 0;
    public static final int STEAM_TIME_PROPERTY_INDEX = 1;
    public static final int WATER_PROPERTY_INDEX = 2;
    public static final int PROPERTY_COUNT = 3;

    private DefaultedList<ItemStack> inventory;
    private int pullTime;
    private int steamTime;
    private boolean lastTickEspresso, lastTickMilk;
    int water;

    protected final PropertyDelegate propertyDelegate;


    public EspressoMachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.ESPRESSO_MACHINE_BLOCK_ENTITY_TYPE, pos, state);
        this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case PULL_TIME_PROPERTY_INDEX -> pullTime;
                    case STEAM_TIME_PROPERTY_INDEX -> steamTime;
                    case WATER_PROPERTY_INDEX -> water;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case PULL_TIME_PROPERTY_INDEX -> pullTime = value;
                    case STEAM_TIME_PROPERTY_INDEX -> steamTime = value;
                    case WATER_PROPERTY_INDEX -> water = value;
                }
            }

            @Override
            public int size() {
                return PROPERTY_COUNT;
            }
        };
    }

    protected Text getContainerName() {
        return Text.translatable("container.espresso_machine");
    }

    public int size() { return inventory.size(); }

    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    public static void tick(World world, BlockPos pos, BlockState state, EspressoMachineBlockEntity blockEntity) {
        ItemStack fuelStack = blockEntity.inventory.get(WATER_SLOT_INDEX);
        if (blockEntity.water <= 0 && fuelStack.isOf(Items.WATER_BUCKET)) {
            blockEntity.water = MAX_WATER_USES;
            //noinspection DataFlowIssue
            fuelStack = new ItemStack(Items.WATER_BUCKET.getRecipeRemainder(), 1);
            blockEntity.inventory.set(WATER_SLOT_INDEX, fuelStack);
            markDirty(world, pos, state);
        }

        // Espresso Section

        boolean recipe = canPull(blockEntity.inventory);
        boolean pulling = blockEntity.pullTime > 0;

        if (pulling) {
            --blockEntity.pullTime;
            boolean done = blockEntity.pullTime == 0;
            if (done && recipe) {
                blockEntity.water -= 2;
                pull(world, pos, blockEntity.inventory);
                markDirty(world, pos, state);
            } else if (!recipe) {
                blockEntity.pullTime = 0;
                markDirty(world, pos, state);
            }
        } else if (recipe && blockEntity.water > 1) {
            blockEntity.pullTime = 21 * 20;
            markDirty(world, pos, state);
        }
        BlockState blockState = state;
        boolean hasEspresso = !blockEntity.inventory.get(ESPRESSO_SLOT_INDEX).isEmpty() || !blockEntity.inventory.get(ESPRESSO_SLOT_2_INDEX).isEmpty();
        if (hasEspresso != blockEntity.lastTickEspresso) {
            blockEntity.lastTickEspresso = hasEspresso;
            if (!(state.getBlock() instanceof EspressoMachineBlock)) {
                return;
            }

            blockState = blockState.with(EspressoMachineBlock.HAS_ESPRESSO, hasEspresso);

            world.setBlockState(pos, blockState, 2);
        }

        boolean hasMilk = !blockEntity.inventory.get(MILK_SLOT_INDEX).isEmpty();
        if (hasMilk != blockEntity.lastTickMilk) {
            blockEntity.lastTickMilk = hasMilk;
            if (!(state.getBlock() instanceof EspressoMachineBlock)) {
                return;
            }

            blockState = blockState.with(EspressoMachineBlock.HAS_MILK, hasMilk);

            world.setBlockState(pos, blockState, 2);
        }

        // TODO milksteaming
    }

    private static boolean canPull(DefaultedList<ItemStack> slots) {
        ItemStack input = slots.get(GROUNDS_SLOT_INDEX);
        ItemStack trash = slots.get(TRASH_SLOT_INDEX);
        if (input.isEmpty()) return false;
        if (input.getCount() < 2) return false;
        if (!input.isOf(ModItems.GROUND_ESPRESSO_ROAST)) return false;
        if (trash.getCount() > trash.getItem().getMaxCount() - 2) return false;
        if (!(trash.isOf(ModItems.USED_COFFEE_GROUNDS) || trash.isEmpty())) return false;
        return slots.get(ESPRESSO_SLOT_INDEX).isOf(Items.GLASS_BOTTLE) || slots.get(ESPRESSO_SLOT_2_INDEX).isOf(Items.GLASS_BOTTLE);
    }

    private static void pull(World world, BlockPos pos, DefaultedList<ItemStack> slots) {
        ItemStack input = slots.get(GROUNDS_SLOT_INDEX);
        ItemStack trash = slots.get(TRASH_SLOT_INDEX);
        input.decrement(2);

        if (trash.isEmpty()) {
            trash = new ItemStack(ModItems.USED_COFFEE_GROUNDS, 2);
        } else {
            trash.increment(2);
        }

        ItemStack out1 = slots.get(ESPRESSO_SLOT_INDEX),
                out2 = slots.get(ESPRESSO_SLOT_2_INDEX);

        if (out1.isOf(Items.GLASS_BOTTLE)) {
            out1 = new ItemStack(ModItems.ESPRESSO_SHOT, 1);
            slots.set(ESPRESSO_SLOT_INDEX, out1);
        }
        if (out2.isOf(Items.GLASS_BOTTLE)) {
            out2 = new ItemStack(ModItems.ESPRESSO_SHOT, 1);
            slots.set(ESPRESSO_SLOT_2_INDEX, out2);
        }

        slots.set(GROUNDS_SLOT_INDEX, input);
        slots.set(TRASH_SLOT_INDEX, trash);

        world.syncWorldEvent(10003, pos, 0);
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, inventory);
        pullTime = nbt.getShort("PullTime");
        water = nbt.getShort("Water");
        steamTime = nbt.getShort("SteamTime");
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putShort("PullTime", (short) pullTime);
        nbt.putShort("Water", (short) water);
        nbt.putShort("SteamTime", (short) steamTime);
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
        return switch (slot) {
            case GROUNDS_SLOT_INDEX -> stack.isOf(ModItems.GROUND_ESPRESSO_ROAST);
            case ESPRESSO_SLOT_INDEX,ESPRESSO_SLOT_2_INDEX -> getStack(slot).isEmpty() && stack.isOf(Items.GLASS_BOTTLE);
            case MILK_SLOT_INDEX -> getStack(slot).isEmpty() && (stack.isOf(ModItems.MILK_BOTTLE) /*TODO || stack.isOf(ModItems.LATTE)*/);
            case WATER_SLOT_INDEX -> stack.isOf(Items.WATER_BUCKET);
            default -> false;
        };
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
        return slot != GROUNDS_SLOT_INDEX;
    }

    public void clear() {
        inventory.clear();
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory inventory) {
        return new EspressoMachineScreenHandler(syncId, inventory, this, propertyDelegate);
    }

}
