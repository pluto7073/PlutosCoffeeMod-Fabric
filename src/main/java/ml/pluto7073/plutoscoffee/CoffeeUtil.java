package ml.pluto7073.plutoscoffee;

import com.mojang.datafixers.util.Pair;
import ml.pluto7073.pdapi.component.DrinkAdditions;
import ml.pluto7073.pdapi.component.PDComponents;
import ml.pluto7073.pdapi.util.DrinkUtil;
import ml.pluto7073.pdapi.addition.DrinkAddition;
import ml.pluto7073.pdapi.addition.DrinkAdditionManager;
import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import ml.pluto7073.plutoscoffee.coffee.CoffeeType;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import ml.pluto7073.plutoscoffee.items.BrewedCoffee;
import ml.pluto7073.plutoscoffee.mixins.StructurePoolAccessor;
import ml.pluto7073.plutoscoffee.registry.ModComponents;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.tags.ModItemTags;
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

    public static CoffeeType getCoffeeType(ItemStack stack) {
        return stack.getOrDefault(ModComponents.COFFEE_TYPE, CoffeeTypes.EMPTY);
    }

    public static ItemStack setCoffeeType(ItemStack stack, CoffeeType type) {
        stack.set(ModComponents.COFFEE_TYPE, type);
        return stack;
    }

    public static ItemStack getBaseCoffee(CoffeeType type) {
        return setCoffeeType(new ItemStack(ModItems.BREWED_COFFEE), type);
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
        DrinkAddition[] addIns = DrinkUtil.getAdditionsFromStack(stack);
        if (addIns == null) {
            return BrewedCoffee.DEFAULT_COLOUR;
        }
        return getCoffeeColour(addIns);
    }

    public static int getCoffeeColour(DrinkAddition[] addIns) {
        int colour = BrewedCoffee.DEFAULT_COLOUR;
        if (Arrays.stream(addIns).map(DrinkAdditionManager::getId).anyMatch(identifier -> identifier.toString().equals("pdapi:milk"))) {
            colour = BrewedCoffee.COLOUR_WITH_MILK;
        }
        float r = (colour >> 16 & 255) / 255.0F;
        float g = (colour >> 8 & 255) / 255.0F;
        float b = (colour & 255) / 255.0F;
        int colourCount = 1;
        int allowedMilk = 3;
        for (DrinkAddition addition : addIns) {
            if (!addition.changesColor()) continue;
            if (DrinkAdditionManager.getId(addition).toString().equals("pdapi:milk") && allowedMilk > 0) {
                allowedMilk--;
                continue;
            }
            int additionColour = addition.color();
            r += (additionColour >> 16 & 255) / 255.0F;
            g += (additionColour >> 8 & 255) / 255.0F;
            b += (additionColour & 255) / 255.0F;
            colourCount += 1;
        }
        r = r / (float) colourCount * 255.0F;
        g = g / (float) colourCount * 255.0F;
        b = b / (float) colourCount * 255.0F;
        return 255 << 24 | (int) r << 16 | (int) g << 8 | (int) b;
    }

    public static ItemStack getWithAdditions(ItemStack stack, String... additions) {
        DrinkAdditions base = stack.getOrDefault(PDComponents.ADDITIONS, DrinkAdditions.EMPTY);
        DrinkAdditions addIns = DrinkAdditions.or(base, DrinkAdditions.of(Arrays.stream(additions).map(ResourceLocation::new).toList()));
        stack.set(PDComponents.ADDITIONS, addIns);
        return stack;
    }

    public static int getLatteColour(ItemStack stack) {
        DrinkAddition[] addIns = DrinkUtil.getAdditionsFromStack(stack);
        if (addIns == null) {
            return 0xFFFFFF;
        }
        return getLatteColour(addIns);
    }

    public static int getLatteColour(DrinkAddition[] addIns) {
        int color = 0xFFFFFF;
        if (Arrays.stream(addIns).map(DrinkAdditionManager::getId).anyMatch(id -> id.toString().equals("plutoscoffee:espresso_shot")
                || id.toString().equals("plutoscoffee:blonde_espresso_shot")
                || id.toString().equals("plutoscoffee:decaf_espresso_shot"))) {
            color = BrewedCoffee.COLOUR_WITH_MILK;
        }
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        int colourCount = 1;
        int allowedShots = 2;
        for (DrinkAddition addition : addIns) {
            if (!addition.changesColor()) continue;
            if ((DrinkAdditionManager.getId(addition).toString().equals("plutoscoffee:espresso_shot") ||
                    DrinkAdditionManager.getId(addition).toString().equals("plutoscoffee:blonde_espresso_shot") ||
                    DrinkAdditionManager.getId(addition).toString().equals("plutoscoffee:decaf_espresso_shot")) && allowedShots > 0) {
                allowedShots--;
                continue;
            }
            int additionColour = addition.color();
            r += (additionColour >> 16 & 255) / 255.0F;
            g += (additionColour >> 8 & 255) / 255.0F;
            b += (additionColour & 255) / 255.0F;
            colourCount += 1;
        }
        r = r / (float) colourCount * 255.0F;
        g = g / (float) colourCount * 255.0F;
        b = b / (float) colourCount * 255.0F;
        return 255 << 24 | (int) r << 16 | (int) g << 8 | (int) b;
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
