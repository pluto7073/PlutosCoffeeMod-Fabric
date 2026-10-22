package ml.pluto7073.plutoscoffee.registry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import ml.pluto7073.pdapi.PDRegistries;
import ml.pluto7073.pdapi.addition.DrinkAddition;
import ml.pluto7073.pdapi.component.DrinkAdditions;
import ml.pluto7073.pdapi.component.PDComponents;
import ml.pluto7073.pdapi.item.PDItems;
import ml.pluto7073.plutoscoffee.CoffeeUtil;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import ml.pluto7073.plutoscoffee.items.LatteItem;
import ml.pluto7073.plutoscoffee.tags.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CoffeeVillagerTrades {

    public static final VillagerProfession BARISTA;

    private static final Predicate<Holder<PoiType>> WORKSTATION_PREDICATE = (entry) -> entry.is(CoffeePOIs.DRINK_WORKSTATION);

    static {
        BARISTA = register("barista",
                new VillagerProfession(
                        PlutosCoffee.asId("barista").toString(),
                        WORKSTATION_PREDICATE,
                        WORKSTATION_PREDICATE,
                        ImmutableSet.of(),
                        ImmutableSet.of(),
                        SoundEvents.VILLAGER_WORK_CLERIC
                )
        );
    }

    public static void init() {}

    public static void postInit() {
        VillagerTrades.TRADES.put(BARISTA, new Int2ObjectOpenHashMap<>(ImmutableMap.of(
                1, new VillagerTrades.ItemListing[]{
                        new VillagerTrades.EmeraldForItems(CoffeeItems.COFFEE_CHERRY, 24, 16, 2),
                        new VillagerTrades.ItemsForEmeralds(CoffeeItems.MOCHA_SAUCE, 1, 3, 16, 2),
                        new VillagerTrades.EmeraldForItems(CoffeeItems.COFFEE_BEAN, 22, 16, 2),
                        new VillagerTrades.EmeraldForItems(CoffeeItems.USED_COFFEE_GROUNDS, 26, 16, 2)
                },
                2, new VillagerTrades.ItemListing[]{
                        new VillagerTrades.ItemsAndEmeraldsToItems(new ItemCost(CoffeeItems.LIGHT_ROAST_BEAN, 3), 2, new ItemStack(CoffeeItems.GROUND_LIGHT_ROAST, 12), 12, 5, 5, Optional.empty()),
                        new VillagerTrades.ItemsAndEmeraldsToItems(new ItemCost(CoffeeItems.MEDIUM_ROAST_BEAN, 3), 2, new ItemStack(CoffeeItems.GROUND_MEDIUM_ROAST, 12), 12, 5, 5, Optional.empty()),
                        new VillagerTrades.ItemsAndEmeraldsToItems(new ItemCost(CoffeeItems.DARK_ROAST_BEAN, 3), 2, new ItemStack(CoffeeItems.GROUND_DARK_ROAST, 12), 12, 5, 5, Optional.empty()),
                        new VillagerTrades.ItemsAndEmeraldsToItems(new ItemCost(CoffeeItems.ESPRESSO_ROAST_BEAN, 3), 2, new ItemStack(CoffeeItems.GROUND_ESPRESSO_ROAST, 12), 12, 5, 5, Optional.empty())
                },
                3, new VillagerTrades.ItemListing[]{
                        new VillagerTrades.ItemsForEmeralds(CoffeeUtil.getBaseCoffee(CoffeeTypes.LIGHT_ROAST), 3, 1, 12, 10),
                        new VillagerTrades.ItemsForEmeralds(CoffeeUtil.getBaseCoffee(CoffeeTypes.MEDIUM_ROAST), 3, 1, 12, 10),
                        new VillagerTrades.ItemsForEmeralds(CoffeeUtil.getBaseCoffee(CoffeeTypes.DARK_ROAST), 3, 1, 12, 10),
                        new LatteWithRandomAdditions(5, 12, 15),
                        new VillagerTrades.EmeraldForItems(PDItems.MILK_BOTTLE, 16, 12, 5)
                },
                4, new VillagerTrades.ItemListing[] {
                        new LatteWithRandomAdditions(5, 12, 20),
                        new LatteWithRandomAdditions(5, 12, 20),
                        new LatteWithRandomAdditions(5, 12, 20),
                        new LatteWithRandomAdditions(5, 12, 20),
                        new VillagerTrades.ItemsAndEmeraldsToItems(new ItemCost(CoffeeItems.DARK_ROAST_BEAN, 16), 2, new ItemStack(CoffeeItems.DECAF_ROAST_BEAN, 16), 12, 20, 5, Optional.empty())
                },
                5, new VillagerTrades.ItemListing[]{
                        new VillagerTrades.ItemsForEmeralds(CoffeeItems.COFFEE_BREWER, 24, 1, 12, 25),
                        new VillagerTrades.ItemsForEmeralds(CoffeeItems.COFFEE_GRINDR, 20, 1, 12, 25),
                        new VillagerTrades.ItemsForEmeralds(CoffeeItems.ESPRESSO_MACHINE, 30, 1, 12, 25)
                }
        )));
    }

    private static VillagerProfession register(String id, VillagerProfession profession ) {
        return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, PlutosCoffee.asId(id), profession);
    }

    public static class LatteForEmeralds implements VillagerTrades.ItemListing {

        private final int price;
        private final int maxUses;
        private final int experience;

        public LatteForEmeralds(int price, int maxUses, int experience) {
            this.price = price;
            this.maxUses = maxUses;
            this.experience = experience;
        }

        @Override
        public @Nullable MerchantOffer getOffer(Entity entity, RandomSource random) {
            Registry<DrinkAddition> registry = entity.level().registryAccess().registryOrThrow(PDRegistries.DRINK_ADDITION_KEY);
            ItemStack latte = getLatte(registry);
            return new MerchantOffer(new ItemCost(Items.EMERALD, price), Optional.empty(), latte, maxUses, experience, 0.05f);
        }
    }

    public static class LatteWithRandomAdditions implements VillagerTrades.ItemListing {

        private final int price;
        private final int maxUses;
        private final int experience;

        public LatteWithRandomAdditions(int price, int maxUses, int experience) {
            this.price = price;
            this.maxUses = maxUses;
            this.experience = experience;
        }

        @Override
        public @Nullable MerchantOffer getOffer(Entity entity, RandomSource random) {
            Registry<DrinkAddition> registry = entity.level().registryAccess().registryOrThrow(PDRegistries.DRINK_ADDITION_KEY);
            ItemStack latte = getLatte(registry);
            List<Holder<DrinkAddition>> toAdd = Lists.newArrayList();
            int price = this.price;
            int commonCount = random.nextInt(1, 4);
            boolean uncommon = random.nextInt(4) < 3;
            boolean rare = random.nextInt(4) == 0;
            for (int i = 0; i < commonCount; i++) {
                registry.getRandomElementOf(ModTags.COMMON_TRADEABLE_ADDITIONS, random)
                        .ifPresent(toAdd::add);
            }
            if (uncommon) {
                registry.getRandomElementOf(ModTags.UNCOMMON_TRADEABLE_ADDITIONS, random)
                        .ifPresent(toAdd::add);
                price += 1;
            }
            if (rare) {
                registry.getRandomElementOf(ModTags.RARE_TRADEABLE_ADDITIONS, random)
                        .ifPresent(toAdd::add);
                price += 4;
            }
            //noinspection DataFlowIssue
            latte.set(PDComponents.ADDITIONS,
                    DrinkAdditions.or(latte.get(PDComponents.ADDITIONS), DrinkAdditions.of(toAdd)));
            return new MerchantOffer(new ItemCost(Items.EMERALD, price), Optional.empty(), latte, maxUses, experience, 0.05f);
        }

    }

    private static ItemStack getLatte(Registry<DrinkAddition> registry) {
        ItemStack stack = new ItemStack(CoffeeItems.LATTE);
        Holder<DrinkAddition> espressoShot = registry.getHolder(PlutosCoffee.asId("espresso_shot")).orElseThrow();
        stack.set(PDComponents.ADDITIONS, DrinkAdditions.of(List.of(espressoShot, espressoShot)));
        return stack;
    }

}
