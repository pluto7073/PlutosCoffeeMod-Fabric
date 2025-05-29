package ml.pluto7073.plutoscoffee;

import com.mojang.datafixers.util.Pair;
import ml.pluto7073.pdapi.util.DrinkUtil;
import ml.pluto7073.pdapi.addition.DrinkAddition;
import ml.pluto7073.pdapi.addition.DrinkAdditionManager;
import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import ml.pluto7073.plutoscoffee.coffee.CoffeeType;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSources;
import ml.pluto7073.plutoscoffee.items.BrewedCoffee;
import ml.pluto7073.plutoscoffee.mixins.StructurePoolAccessor;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.tags.ModItemTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.fabricmc.fabric.api.tag.convention.v1.TagUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ml.pluto7073.plutoscoffee.blocks.EspressoMachineBlockEntity.WATER;

public final class CoffeeUtil {

    private CoffeeUtil(){}

    public static <T> boolean collectionContainsOnlyAll(Collection<T> c1, Collection<T> c2) {
        List<T> l1 = new ArrayList<>(c1);
        for (T t : c2) {
            l1.remove(t);
        }
        List<T> l2 = new ArrayList<>(c2);
        for (T t : c1) {
            l2.remove(t);
        }
        return (l1.isEmpty()) && (l2.isEmpty());
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void updateWaterMachine(BaseContainerBlockEntity blockEntity, int waterSlot, SingleFluidStorage fluid) {
        ItemStack fuelStack = blockEntity.getItem(waterSlot);
        int waterAmount = MachineWaterSources.getWaterAmount(fuelStack);
        trans: try (Transaction transaction = Transaction.openOuter()) {
            long waterInserted = fluid.insert(WATER, waterAmount, transaction);
            if (waterAmount - waterInserted > 2025 || waterAmount == 0) {
                transaction.abort();
                break trans;
            }
            Item source = fuelStack.getItem().getCraftingRemainingItem();
            if (fuelStack.is(ConventionalItemTags.POTIONS)) {
                source = Items.GLASS_BOTTLE;
            }
            blockEntity.setItem(waterSlot, source == null ? ItemStack.EMPTY : new ItemStack(source));
            transaction.commit();
        }
    }

    public static CoffeeType getCoffeeType(ItemStack stack) {
        DrinkUtil.convertStackFromPlutosCoffee(stack);
        return getCoffeeType(stack.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY));
    }

    public static ItemStack setCoffeeType(ItemStack stack, CoffeeType type) {
        DrinkUtil.convertStackFromPlutosCoffee(stack);
        ResourceLocation id = CoffeeTypes.getIdentifier(type);
        if (type == CoffeeTypes.EMPTY) {
            stack.removeTagKey(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY);
        } else {
            stack.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).putString("CoffeeType", id.toString());
        }
        return stack;
    }

    public static ItemStack getBaseCoffee(CoffeeType type) {
        ItemStack stack = new ItemStack(ModItems.BREWED_COFFEE);
        if (type == CoffeeTypes.EMPTY) return stack;
        stack.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).putString("CoffeeType", CoffeeTypes.getIdentifier(type).toString());
        return stack;
    }

    public static CoffeeType getCoffeeType(@Nullable CompoundTag nbt) {
        return nbt == null ? CoffeeTypes.EMPTY : CoffeeType.byId(nbt.getString("CoffeeType"));
    }

    public static boolean isItemACoffeeGround(Item item) {
        return TagUtil.isIn(ModItemTags.COFFEE_GROUNDS, item);
    }

    public static boolean isItemACoffeeBean(Item item) {
        return TagUtil.isIn(ModItemTags.COFFEE_BEANS, item);
    }

    public static int getCoffeeColour(ItemStack stack) {
        DrinkUtil.convertStackFromPlutosCoffee(stack);
        DrinkAddition[] addIns = DrinkUtil.getAdditionsFromStack(stack);
        if (addIns == null) {
            return BrewedCoffee.DEFAULT_COLOUR;
        }
        return getCoffeeColour(addIns);
    }

    public static int getCoffeeColour(DrinkAddition[] addIns) {
        int color = BrewedCoffee.DEFAULT_COLOUR;
        if (Arrays.stream(addIns).map(DrinkAdditionManager::getId).anyMatch(identifier -> identifier.toString().equals("pdapi:milk"))) {
            color = BrewedCoffee.COLOUR_WITH_MILK;
        }
        final AtomicInteger allowedMilk = new AtomicInteger(2);
        List<Integer> colors = Arrays.stream(addIns).filter(addition -> {
            if (DrinkAdditionManager.getId(addition).toString().equals("pdapi:milk") && allowedMilk.get() > 0) {
                allowedMilk.decrementAndGet();
                return false;
            }
            return true;
        }).filter(DrinkAddition::changesColor).map(DrinkAddition::getColor).collect(Collectors.toCollection(ArrayList::new));
        colors.add(0, color);
        return DrinkUtil.averageColors(colors);
    }

    public static ItemStack getWithAdditions(ItemStack stack, String... additions) {
        ListTag adds = new ListTag();
        if (stack.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).contains("Additions")) {
            adds = stack.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).getList("Additions", ListTag.TAG_STRING);
        }
        for (String s : additions) {
            adds.add(DrinkUtil.stringAsNbt(s));
        }
        stack.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).put("Additions", adds);
        return stack;
    }

    public static int getLatteColour(ItemStack stack) {
        DrinkUtil.convertStackFromPlutosCoffee(stack);
        DrinkAddition[] addIns = DrinkUtil.getAdditionsFromStack(stack);
        if (addIns == null) {
            return 0xFFFFFF;
        }
        return getLatteColour(addIns);
    }

    public static int getLatteColour(DrinkAddition[] addIns) {
        int color = 0xFFFFFF;
        if (Arrays.stream(addIns).map(DrinkAdditionManager::getId).anyMatch(id -> id.getPath().contains("espresso_shot"))) {
            color = BrewedCoffee.COLOUR_WITH_MILK;
        }
        final AtomicInteger allowedShots = new AtomicInteger(2);
        List<Integer> colors = Arrays.stream(addIns).filter(addition -> {
            if (DrinkAdditionManager.getId(addition).getPath().contains("espresso_shot") && allowedShots.get() > 0) {
                allowedShots.decrementAndGet();
                return false;
            }
            return true;
        }).filter(DrinkAddition::changesColor).map(DrinkAddition::getColor).collect(Collectors.toCollection(ArrayList::new));
        colors.add(0, color);
        return DrinkUtil.averageColors(colors);
    }

    public static int calculateHealthBarHeightPixels(int health, int maxHeartsPerRow, int rowHeight) {
        double hearts = health / 2.0;
        int rows = (int) Math.floor(hearts / maxHeartsPerRow);
        return rows * rowHeight;
    }

    /**
     * Borrowed from the FriendsAndFoes mod by Faboslav
     */
    public static void addElementToStructurePool(Registry<StructureTemplatePool> templateRegistry, ResourceLocation poolLocation, String name, int weight) {
        StructureTemplatePool pool = templateRegistry.get(poolLocation);
        if (pool == null) return;

        SinglePoolElement piece = SinglePoolElement.single(PlutosCoffee.asId(name).toString()).apply(StructureTemplatePool.Projection.RIGID);

        for (int i = 0; i < weight; i++) {
            ((StructurePoolAccessor) pool).getTemplates().add(piece);
        }

        List<Pair<StructurePoolElement, Integer>> pieceCounts = new ArrayList<>(((StructurePoolAccessor) pool).getRawTemplates());
        pieceCounts.add(new Pair<>(piece, weight));
        ((StructurePoolAccessor) pool).setRawTemplates(pieceCounts);
    }

}
