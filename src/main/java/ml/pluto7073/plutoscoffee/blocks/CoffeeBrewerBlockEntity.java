package ml.pluto7073.plutoscoffee.blocks;

import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import ml.pluto7073.pdapi.util.BasicSingleStorage;
import ml.pluto7073.plutoscoffee.CoffeeUtil;
import ml.pluto7073.plutoscoffee.coffee.CoffeeType;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSources;
import ml.pluto7073.plutoscoffee.gui.CoffeeBrewerMenu;
import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

import static ml.pluto7073.plutoscoffee.blocks.EspressoMachineBlockEntity.WATER;

@SuppressWarnings("UnstableApiUsage")
@MethodsReturnNonnullByDefault
public class CoffeeBrewerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    private static final int INPUT_SLOT_INDEX = 0; //Coffee Grounds Go Here
    private static final int FUEL_SLOT_INDEX = 1; //Water Bucket Goes Here
    private static final int[] TOP_SLOTS = {0};
    private static final int[] BOTTOM_SLOTS = {0, 2};
    private static final int[] SIDE_SLOTS = {1, 2};
    private static final int WATER_FOR_COFFEE = 16200;
    public static final int BREW_TIME_PROPERTY_INDEX = 0;
    public static final int FUEL_PROPERTY_INDEX = 1;
    public static final int PROPERTY_COUNT = 2;
    private NonNullList<ItemStack> inventory;
    int brewTime;
    private int lastTickSlot;
    private Item itemBrewing;
    private final BasicSingleStorage water =
            new BasicSingleStorage(WATER, 81000, this::setChanged);
    public final Storage<FluidVariant> insert = FilteringStorage.insertOnlyOf(water);
    protected final ContainerData containerData;

    public CoffeeBrewerBlockEntity(BlockPos pos, BlockState state) {
        super (ModBlocks.COFFEE_BREWER_BLOCK_ENTITY_TYPE, pos, state);
        inventory = NonNullList.withSize(3, ItemStack.EMPTY);
        containerData = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case BREW_TIME_PROPERTY_INDEX -> CoffeeBrewerBlockEntity.this.brewTime;
                    case FUEL_PROPERTY_INDEX -> (int) CoffeeBrewerBlockEntity.this.water.amount;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case BREW_TIME_PROPERTY_INDEX -> CoffeeBrewerBlockEntity.this.brewTime = value;
                    case FUEL_PROPERTY_INDEX -> CoffeeBrewerBlockEntity.this.water.amount = value;
                }
            }

            public int getCount() {
                return PROPERTY_COUNT;
            }
        };
    }

    protected Component getDefaultName() {
        return Component.translatable("container.coffee_brewer");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        inventory = nonNullList;
    }

    public int getContainerSize() {
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

    public static void tick(Level level, BlockPos pos, BlockState state, CoffeeBrewerBlockEntity blockEntity) {
        ItemStack fuelStack = blockEntity.inventory.get(FUEL_SLOT_INDEX);
        int waterAmount = MachineWaterSources.getWaterAmount(fuelStack);
        trans: try (Transaction transaction = Transaction.openOuter()) {
            long waterInserted = blockEntity.water.insert(WATER, waterAmount, transaction);
            if (waterAmount - waterInserted > 2025) {
                transaction.abort();
                break trans;
            }
            Item source = fuelStack.getItem().getCraftingRemainingItem();
            if (fuelStack.is(ConventionalItemTags.POTIONS)) {
                source = Items.GLASS_BOTTLE;
            }
            blockEntity.setItem(FUEL_SLOT_INDEX, source == null ? ItemStack.EMPTY : new ItemStack(source));
            transaction.commit();
        }

        boolean recipe = canCraft(blockEntity.inventory);
        boolean brewing = blockEntity.brewTime > 0;

        ItemStack inputStack = blockEntity.inventory.get(INPUT_SLOT_INDEX);
        if (brewing) {
            --blockEntity.brewTime;
            boolean done = blockEntity.brewTime == 0;
            if (done && recipe) {
                blockEntity.water.amount -= WATER_FOR_COFFEE;
                craft(level, pos, blockEntity.inventory);
                setChanged(level, pos, state);
            } else if (!recipe || !inputStack.is(blockEntity.itemBrewing)) {
                blockEntity.brewTime = 0;
                setChanged(level, pos, state);
            }
        } else if (recipe && blockEntity.water.amount >= WATER_FOR_COFFEE) {
            blockEntity.brewTime = 600;
            blockEntity.itemBrewing = inputStack.getItem();
            setChanged(level, pos, state);
        }

        int slotEmpty = blockEntity.getEmptySlot();
        if (slotEmpty != blockEntity.lastTickSlot) {
            blockEntity.lastTickSlot = slotEmpty;
            BlockState blockState = state;
            if (!(state.getBlock() instanceof CoffeeBrewerBlock)) {
                return;
            }

            blockState = blockState.setValue(CoffeeBrewerBlock.BOTTLE_PROPERTY, slotEmpty);

            level.setBlock(pos, blockState, 2);
        }
    }

    private int getEmptySlot() {
        if (inventory.get(2).isEmpty()) {
            return 0;
        } else if (inventory.get(2).is(Items.GLASS_BOTTLE)) {
            return 1;
        } else if (inventory.get(2).is(ModItems.BREWED_COFFEE)) {
            return 2;
        } else {
            return 0;
        }
    }

    private static boolean canCraft(NonNullList<ItemStack> slots) {
        ItemStack input = slots.get(INPUT_SLOT_INDEX);
        if (input.isEmpty()) {
            return false;
        } else if (!CoffeeUtil.isItemACoffeeGround(input.getItem())) {
            return false;
        } else {
            ItemStack base = slots.get(2);
            return base.is(Items.GLASS_BOTTLE);
        }
    }

    private static void craft(Level level, BlockPos pos, NonNullList<ItemStack> slots) {
        ItemStack input = slots.get(INPUT_SLOT_INDEX);

        CoffeeType type = CoffeeTypes.getFromGrounds(input.getItem());
        ItemStack result = CoffeeUtil.getBaseCoffee(type);
        slots.set(2, result);

        input.shrink(1);

        slots.set(INPUT_SLOT_INDEX, input);
        level.levelEvent(10001, pos, 0);
    }

    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, inventory, provider);
        brewTime = nbt.getShort("BrewTime");
        water.amount = nbt.getInt("Fuel");
    }

    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.putShort("BrewTime", (short) brewTime);
        ContainerHelper.saveAllItems(nbt, this.inventory, provider);
        nbt.putInt("Fuel", (int) water.amount);
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
            return CoffeeUtil.isItemACoffeeGround(stack.getItem());
        } else if (slot == FUEL_SLOT_INDEX) {
            return stack.is(Items.WATER_BUCKET);
        } else {
            return stack.is(Items.GLASS_BOTTLE) && getItem(slot).isEmpty();
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
        return new CoffeeBrewerMenu(syncId, inventory, this, containerData);
    }

}
