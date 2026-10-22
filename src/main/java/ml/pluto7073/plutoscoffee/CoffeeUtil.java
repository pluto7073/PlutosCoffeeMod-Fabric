package ml.pluto7073.plutoscoffee;

import com.mojang.datafixers.util.Pair;
import ml.pluto7073.pdapi.component.DrinkAdditions;
import ml.pluto7073.pdapi.component.PDComponents;
import ml.pluto7073.pdapi.util.DrinkUtil;
import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import ml.pluto7073.plutoscoffee.coffee.CoffeeType;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSources;
import ml.pluto7073.plutoscoffee.mixins.StructurePoolAccessor;
import ml.pluto7073.plutoscoffee.registry.ModComponents;
import ml.pluto7073.plutoscoffee.registry.CoffeeItems;
import ml.pluto7073.plutoscoffee.tags.ModItemTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil;
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

import java.util.*;

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
        return stack.get(ModComponents.COFFEE_TYPE);
    }

    public static ItemStack setCoffeeType(ItemStack stack, CoffeeType type) {
        stack.set(ModComponents.COFFEE_TYPE, type);
        return stack;
    }

    public static ItemStack getBaseCoffee(CoffeeType type) {
        ItemStack stack = new ItemStack(CoffeeItems.BREWED_COFFEE);
        if (type == CoffeeTypes.EMPTY) return stack;
        stack.set(ModComponents.COFFEE_TYPE, type);
        return stack;
    }

    public static boolean isItemACoffeeGround(Item item) {
        return TagUtil.isIn(ModItemTags.COFFEE_GROUNDS, item);
    }

    public static boolean isItemACoffeeBean(Item item) {
        return TagUtil.isIn(ModItemTags.COFFEE_BEANS, item);
    }

    public static ItemStack getWithAdditions(ItemStack stack, String... additions) {
        DrinkAdditions adds = stack.getOrDefault(PDComponents.ADDITIONS, DrinkAdditions.EMPTY);
        for (String s : additions) {
            adds = adds.withAddition(ResourceLocation.parse(s));
        }
        stack.set(PDComponents.ADDITIONS, adds);
        return stack;
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
