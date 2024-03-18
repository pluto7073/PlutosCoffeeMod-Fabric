package ml.pluto7073.plutoscoffee;

import com.mojang.datafixers.util.Pair;
import ml.pluto7073.plutoscoffee.coffee.CoffeeAddition;
import ml.pluto7073.plutoscoffee.coffee.CoffeeAdditions;
import ml.pluto7073.plutoscoffee.coffee.CoffeeType;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import ml.pluto7073.plutoscoffee.items.BrewedCoffee;
import ml.pluto7073.plutoscoffee.mixins.StructurePoolAccessor;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.registry.ModMisc;
import ml.pluto7073.plutoscoffee.tags.ModItemTags;
import net.fabricmc.fabric.api.tag.convention.v1.TagUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registry;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class CoffeeUtil {

    private static final double CAFFEINE_HALF_LIFE_TICKS = 2500.0;

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
        return getCoffeeType(stack.getSubNbt("Coffee"));
    }

    public static ItemStack setCoffeeType(ItemStack stack, CoffeeType type) {
        Identifier id = CoffeeTypes.getIdentifier(type);
        if (type == CoffeeTypes.EMPTY) {
            stack.removeSubNbt("Coffee");
        } else {
            stack.getOrCreateSubNbt("Coffee").putString("CoffeeType", id.toString());
        }
        return stack;
    }

    public static ItemStack getBaseCoffee(CoffeeType type) {
        ItemStack stack = new ItemStack(ModItems.BREWED_COFFEE);
        if (type == CoffeeTypes.EMPTY) return stack;
        stack.getOrCreateSubNbt("Coffee").putString("CoffeeType", CoffeeTypes.getIdentifier(type).toString());
        return stack;
    }

    public static CoffeeType getCoffeeType(@Nullable NbtCompound nbt) {
        return nbt == null ? CoffeeTypes.EMPTY : CoffeeType.byId(nbt.getString("CoffeeType"));
    }

    public static NbtElement stringAsNbt(String s) {
        NbtCompound compound = new NbtCompound();
        compound.putString("string", s);
        return compound.get("string");
    }

    public static boolean isItemACoffeeGround(Item item) {
        return TagUtil.isIn(ModItemTags.COFFEE_GROUNDS, item);
    }

    public static boolean isItemACoffeeBean(Item item) {
        return TagUtil.isIn(ModItemTags.COFFEE_BEANS, item);
    }

    @Nullable
    public static CoffeeAddition[] getCoffeeAddIns(ItemStack stack) {
        return getCoffeeAddIns(stack.getOrCreateSubNbt("Coffee"));
    }

    @Nullable
    public static CoffeeAddition[] getCoffeeAddIns(@Nullable NbtCompound nbt) {
        if (nbt == null) return null;
        NbtList list = nbt.getList("Additions", NbtElement.STRING_TYPE);
        if (list == null) return null;
        if (list.isEmpty()) return null;
        CoffeeAddition[] addIns = new CoffeeAddition[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String addIn = list.getString(i);
            Identifier id = new Identifier(addIn);
            if (!CoffeeAdditions.REGISTRY.containsKey(id)) {
                addIns[i] = CoffeeAdditions.EMPTY;
                continue;
            }
            addIns[i] = CoffeeAdditions.REGISTRY.get(id);
        }
        return addIns;
    }

    public static int getCoffeeColour(ItemStack stack) {
        CoffeeAddition[] addIns = getCoffeeAddIns(stack);
        if (addIns == null) {
            return BrewedCoffee.DEFAULT_COLOUR;
        }
        return getCoffeeColour(addIns);
    }

    public static int getCoffeeColour(CoffeeAddition[] addIns) {
        int colour = BrewedCoffee.DEFAULT_COLOUR;
        if (Arrays.stream(addIns).anyMatch(coffeeAddition -> coffeeAddition == CoffeeAdditions.MILK_BOTTLE)) {
            colour = BrewedCoffee.COLOUR_WITH_MILK;
        }
        float r = (colour >> 16 & 255) / 255.0F;
        float g = (colour >> 8 & 255) / 255.0F;
        float b = (colour & 255) / 255.0F;
        int colourCount = 1;
        int allowedMilk = 3;
        for (CoffeeAddition addition : addIns) {
            if (!addition.changesColour()) continue;
            if (addition == CoffeeAdditions.MILK_BOTTLE && allowedMilk > 0) {
                allowedMilk--;
                continue;
            }
            int additionColour = addition.getColour();
            r += (additionColour >> 16 & 255) / 255.0F;
            g += (additionColour >> 8 & 255) / 255.0F;
            b += (additionColour & 255) / 255.0F;
            colourCount += 1;
        }
        r = r / (float) colourCount * 255.0F;
        g = g / (float) colourCount * 255.0F;
        b = b / (float) colourCount * 255.0F;
        return (int) r << 16 | (int) g << 8 | (int) b;
    }

    public static ItemStack getWithAdditions(ItemStack stack, CoffeeAddition... additions) {
        NbtList adds = new NbtList();
        if (stack.getOrCreateSubNbt("Coffee").contains("Additions")) {
            adds = stack.getOrCreateSubNbt("Coffee").getList("Additions", NbtElement.STRING_TYPE);
        }
        for (CoffeeAddition add : additions) {
            String id = CoffeeAdditions.getId(add).toString();
            adds.add(stringAsNbt(id));
        }
        stack.getOrCreateSubNbt("Coffee").put("Additions", adds);
        return stack;
    }

    public static int getLatteColour(ItemStack stack) {
        CoffeeAddition[] addIns = getCoffeeAddIns(stack);
        if (addIns == null) {
            return 0xFFFFFF;
        }
        return getLatteColour(addIns);
    }

    public static int getLatteColour(CoffeeAddition[] addIns) {
        int color = 0xFFFFFF;
        if (Arrays.stream(addIns).anyMatch(coffeeAddition -> coffeeAddition == CoffeeAdditions.ESPRESSO_SHOT)) {
            color = BrewedCoffee.COLOUR_WITH_MILK;
        }
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        int colourCount = 1;
        int allowedShots = 2;
        for (CoffeeAddition addition : addIns) {
            if (!addition.changesColour()) continue;
            if (addition == CoffeeAdditions.ESPRESSO_SHOT && allowedShots > 0) {
                allowedShots--;
                continue;
            }
            int additionColour = addition.getColour();
            r += (additionColour >> 16 & 255) / 255.0F;
            g += (additionColour >> 8 & 255) / 255.0F;
            b += (additionColour & 255) / 255.0F;
            colourCount += 1;
        }
        r = r / (float) colourCount * 255.0F;
        g = g / (float) colourCount * 255.0F;
        b = b / (float) colourCount * 255.0F;
        return (int) r << 16 | (int) g << 8 | (int) b;
    }

    public static int calculateHealthBarHeightPixels(int health, int maxHeartsPerRow, int rowHeight) {
        double hearts = health / 2.0;
        int rows = (int) Math.floor(hearts / maxHeartsPerRow);
        return rows * rowHeight;
    }

    public static void setPlayerCaffeine(PlayerEntity player, float caffeine) {
        player.getDataTracker().set(ModMisc.PLAYER_CAFFEINE_AMOUNT, caffeine);
        player.getDataTracker().set(ModMisc.PLAYER_ORIGINAL_CAFFEINE_AMOUNT, caffeine);
        player.getDataTracker().set(ModMisc.PLAYER_TICKS_SINCE_LAST_CAFFEINE, 0);
    }

    public static float getPlayerCaffeine(PlayerEntity player) {
        return player.getDataTracker().get(ModMisc.PLAYER_CAFFEINE_AMOUNT);
    }

    public static float getPlayerOriginalCaffeine(PlayerEntity player) {
        return player.getDataTracker().get(ModMisc.PLAYER_ORIGINAL_CAFFEINE_AMOUNT);
    }

    public static int getPlayerCaffeineTicks(PlayerEntity player) {
        return player.getDataTracker().get(ModMisc.PLAYER_TICKS_SINCE_LAST_CAFFEINE);
    }

    public static float calculateCaffeineDecay(int ticks, float originalCaffeine) {
        double exp = Math.pow(0.5, ticks / CAFFEINE_HALF_LIFE_TICKS);
        return (float) (exp * originalCaffeine);
    }

    /**
     * Borrowed from the FriendsAndFoes mod by Faboslav
     */
    public static void addElementToStructurePool(Registry<StructurePool> templateRegistry, Identifier poolLocation, String name, int weight) {
        StructurePool pool = templateRegistry.get(poolLocation);
        if (pool == null) return;

        SinglePoolElement piece = SinglePoolElement.ofLegacySingle(PlutosCoffee.asId(name).toString()).apply(StructurePool.Projection.RIGID);

        for (int i = 0; i < weight; i++) {
            ((StructurePoolAccessor) pool).getElements().add(piece);
        }

        List<Pair<StructurePoolElement, Integer>> pieceCounts = new ArrayList<>(((StructurePoolAccessor) pool).getElementCounts());
        pieceCounts.add(new Pair<>(piece, weight));
        ((StructurePoolAccessor) pool).setElementCounts(pieceCounts);
    }

}
