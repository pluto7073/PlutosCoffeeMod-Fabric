package ml.pluto7073.plutoscoffee.blocks;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import ml.pluto7073.pdapi.tag.PDTags;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSources;
import ml.pluto7073.plutoscoffee.gui.EspressoMachineMenu;
import ml.pluto7073.plutoscoffee.recipe.PullingRecipe;
import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.registry.ModRecipes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@MethodsReturnNonnullByDefault
public class EspressoMachineBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder {

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
    public static final int WATER_TO_ESPRESSO = 50;
    public static final int WATER_TO_STEAM = 25;
    public static final int PULL_TIME_PROPERTY_INDEX = 0;
    public static final int STEAM_TIME_PROPERTY_INDEX = 1;
    public static final int WATER_PROPERTY_INDEX = 2;
    public static final int TOTAL_PULL_TIME_PROPERTY_INDEX = 3;
    public static final int PROPERTY_COUNT = 4;

    private NonNullList<ItemStack> inventory;
    private int pullTime;
    private int steamTime;
    private boolean lastTickEspresso, lastTickMilk;
    int water;

    protected final ContainerData containerData;
    private final RecipeManager.CachedCheck<Container, PullingRecipe> matchGetter;
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed;


    public EspressoMachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.ESPRESSO_MACHINE_BLOCK_ENTITY_TYPE, pos, state);
        this.inventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
        this.containerData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case PULL_TIME_PROPERTY_INDEX -> pullTime;
                    case STEAM_TIME_PROPERTY_INDEX -> steamTime;
                    case WATER_PROPERTY_INDEX -> water;
                    case TOTAL_PULL_TIME_PROPERTY_INDEX -> {
                        if (getItem(GROUNDS_SLOT_INDEX).isEmpty()) yield 400;
                        Optional<PullingRecipe> recipe = matchGetter.getRecipeFor(EspressoMachineBlockEntity.this, level);
                        if (recipe.isEmpty()) yield 400;
                        yield recipe.get().pullTime;
                    }
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
            public int getCount() {
                return PROPERTY_COUNT;
            }
        };
        this.matchGetter = RecipeManager.createCheck(ModRecipes.PULLING_RECIPE_TYPE);
        this.recipesUsed = new Object2IntOpenHashMap<>();
    }

    protected Component getDefaultName() {
        return Component.translatable("container.espresso_machine");
    }

    public int getContainerSize() { return inventory.size(); }

    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EspressoMachineBlockEntity blockEntity) {
        ItemStack fuelStack = blockEntity.inventory.get(WATER_SLOT_INDEX);
        int waterAmount = MachineWaterSources.getWaterAmount(fuelStack);
        if ((blockEntity.water <= 1000 - waterAmount || blockEntity.water < 25) && waterAmount > 0) {
            blockEntity.water += waterAmount;
            if (blockEntity.water > 1000) blockEntity.water = 1000;
            if (fuelStack.getItem().hasCraftingRemainingItem()) {
                //noinspection DataFlowIssue
                fuelStack = new ItemStack(fuelStack.getItem().getCraftingRemainingItem(), 1);
            } else {
                fuelStack = ItemStack.EMPTY;
            }
            blockEntity.inventory.set(WATER_SLOT_INDEX, fuelStack);
            setChanged(level, pos, state);
        }

        // Espresso Section

        PullingRecipe recipe;

        if (blockEntity.inventory.get(GROUNDS_SLOT_INDEX).isEmpty()) {
            recipe = null;
        } else {
            recipe = blockEntity.matchGetter.getRecipeFor(blockEntity, level).orElse(null);
        }

        boolean canPull = canPull(blockEntity.inventory, recipe);
        boolean pulling = blockEntity.pullTime > 0 && recipe != null;

        if (pulling) {
            --blockEntity.pullTime;
            boolean done = blockEntity.pullTime == 0;
            state = state.setValue(EspressoMachineBlock.PULLING, true);
            if (done && canPull) {
                blockEntity.water -= WATER_TO_ESPRESSO;
                pull(level, pos, blockEntity, recipe);
                state = state.setValue(EspressoMachineBlock.PULLING, false);
                setChanged(level, pos, state);
            } else if (!canPull) {
                blockEntity.pullTime = 0;
                state = state.setValue(EspressoMachineBlock.PULLING, false);
                setChanged(level, pos, state);
            }
        } else if (canPull && blockEntity.water >= WATER_TO_ESPRESSO) {
            blockEntity.pullTime = recipe.pullTime;
            setChanged(level, pos, state);
        }
        boolean hasEspresso = !blockEntity.inventory.get(ESPRESSO_SLOT_INDEX).isEmpty() || !blockEntity.inventory.get(ESPRESSO_SLOT_2_INDEX).isEmpty();
        if (hasEspresso != blockEntity.lastTickEspresso) {
            blockEntity.lastTickEspresso = hasEspresso;
            if (!(state.getBlock() instanceof EspressoMachineBlock)) {
                return;
            }

            state = state.setValue(EspressoMachineBlock.HAS_ESPRESSO, hasEspresso);

            level.setBlock(pos, state, 2);
        }

        boolean hasMilk = !blockEntity.inventory.get(MILK_SLOT_INDEX).isEmpty();
        if (hasMilk != blockEntity.lastTickMilk) {
            blockEntity.lastTickMilk = hasMilk;
            if (!(state.getBlock() instanceof EspressoMachineBlock)) {
                return;
            }

            state = state.setValue(EspressoMachineBlock.HAS_MILK, hasMilk);

            level.setBlock(pos, state, 2);
        }

        ItemStack milkStack = blockEntity.inventory.get(MILK_SLOT_INDEX);

        boolean steaming = (milkStack.is(PDTags.MILK_BOTTLES) || milkStack.is(ModItems.LATTE)) && blockEntity.water >= WATER_TO_STEAM;
        if (steaming) {
            blockEntity.steamTime++;
            if (blockEntity.steamTime > 600) blockEntity.steamTime = 600;

            if (milkStack.is(PDTags.MILK_BOTTLES) && blockEntity.steamTime >= 400) {
                milkStack = new ItemStack(ModItems.LATTE);
            } else if (milkStack.is(ModItems.LATTE) && blockEntity.steamTime >= 500) {
                ListTag resAdds = new ListTag();
                if (!resAdds.contains(StringTag.valueOf("pdapi:burnt"))) resAdds.add(StringTag.valueOf("pdapi:burnt"));
                milkStack.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).put("Additions", resAdds);
            }
            blockEntity.inventory.set(MILK_SLOT_INDEX, milkStack);
        } else {
            if (blockEntity.steamLastTick) {
                if (blockEntity.steamTime >= 400) blockEntity.water -= WATER_TO_STEAM;
            }
            blockEntity.steamTime = 0;
        }

        blockEntity.steamLastTick = steaming;
    }

    private boolean steamLastTick = false;

    private static boolean canPull(NonNullList<ItemStack> slots, PullingRecipe recipe) {
        if (recipe == null) return false;
        ItemStack grounds = slots.get(GROUNDS_SLOT_INDEX);
        if (grounds.getCount() < recipe.groundsRequired) return false;
        ItemStack trash = slots.get(TRASH_SLOT_INDEX);
        if (trash.getCount() > trash.getItem().getMaxStackSize() - recipe.groundsRequired) return false;
        return trash.is(ModItems.USED_COFFEE_GROUNDS) || trash.isEmpty();
    }

    private static void pull(Level level, BlockPos pos, Container slots, PullingRecipe recipe) {
        ItemStack input = slots.getItem(GROUNDS_SLOT_INDEX);
        ItemStack trash = slots.getItem(TRASH_SLOT_INDEX);
        input.shrink(recipe.groundsRequired);

        if (trash.isEmpty()) {
            trash = new ItemStack(ModItems.USED_COFFEE_GROUNDS, recipe.groundsRequired);
        } else {
            trash.grow(recipe.groundsRequired);
        }

        ItemStack out1 = slots.getItem(ESPRESSO_SLOT_INDEX),
                out2 = slots.getItem(ESPRESSO_SLOT_2_INDEX);

        if (recipe.base.test(out1)) {
            out1 = recipe.assemble(slots, level.registryAccess());
            slots.setItem(ESPRESSO_SLOT_INDEX, out1);
        }
        if (recipe.base.test(out2)) {
            out2 = recipe.assemble(slots, level.registryAccess());
            slots.setItem(ESPRESSO_SLOT_2_INDEX, out2);
        }

        slots.setItem(GROUNDS_SLOT_INDEX, input);
        slots.setItem(TRASH_SLOT_INDEX, trash);

        level.levelEvent(10003, pos, 0);
    }

    public void load(CompoundTag nbt) {
        super.load(nbt);
        inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, inventory);
        pullTime = nbt.getShort("PullTime");
        water = nbt.getShort("Water");
        steamTime = nbt.getShort("SteamTime");
        CompoundTag used = nbt.getCompound("RecipesUsed");
        for (String s : used.getAllKeys()) {
            recipesUsed.put(new ResourceLocation(s), used.getInt(s));
        }
    }

    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putShort("PullTime", (short) pullTime);
        nbt.putShort("Water", (short) water);
        nbt.putShort("SteamTime", (short) steamTime);
        CompoundTag used = new CompoundTag();
        recipesUsed.forEach((id, i) -> used.putInt(id.toString(), i));
        nbt.put("RecipesUsed", used);
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
        return switch (slot) {
            case GROUNDS_SLOT_INDEX -> stack.is(ModItems.GROUND_ESPRESSO_ROAST);
            case ESPRESSO_SLOT_INDEX,ESPRESSO_SLOT_2_INDEX -> getItem(slot).isEmpty() && stack.is(Items.GLASS_BOTTLE);
            case MILK_SLOT_INDEX -> getItem(slot).isEmpty() && (stack.is(PDTags.MILK_BOTTLES) || stack.is(ModItems.LATTE));
            case WATER_SLOT_INDEX -> stack.is(Items.WATER_BUCKET);
            default -> false;
        };
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
        return slot != GROUNDS_SLOT_INDEX;
    }

    public void clearContent() {
        inventory.clear();
    }

    protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        return new EspressoMachineMenu(syncId, inventory, this, containerData);
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourceLocation = recipe.getId();
            this.recipesUsed.addTo(resourceLocation, 1);
        }

    }

    @Nullable
    public Recipe<?> getRecipeUsed() {
        return null;
    }

}
