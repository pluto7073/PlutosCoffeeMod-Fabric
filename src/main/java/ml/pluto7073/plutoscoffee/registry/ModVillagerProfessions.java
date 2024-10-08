package ml.pluto7073.plutoscoffee.registry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import ml.pluto7073.pdapi.item.PDItems;
import ml.pluto7073.plutoscoffee.CoffeeUtil;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import ml.pluto7073.plutoscoffee.items.LatteItem;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.function.Predicate;

public class ModVillagerProfessions {

    public static final VillagerProfession BARISTA;

    private static final Predicate<Holder<PoiType>> WORKSTATION_PREDICATE = (entry) -> entry.is(ModPointOfInterests.DRINK_WORKSTATION);

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
                        new VillagerTrades.EmeraldForItems(ModItems.COFFEE_BERRY, 24, 16, 2),
                        new VillagerTrades.ItemsForEmeralds(ModItems.MOCHA_SAUCE, 1, 3, 16, 2),
                        new VillagerTrades.EmeraldForItems(ModItems.COFFEE_BEAN, 22, 16, 2),
                        new VillagerTrades.EmeraldForItems(ModItems.USED_COFFEE_GROUNDS, 26, 16, 2)
                },
                2, new VillagerTrades.ItemListing[]{
                        new VillagerTrades.ItemsAndEmeraldsToItems(ModItems.LIGHT_ROAST_BEAN, 3, 2, ModItems.GROUND_LIGHT_ROAST, 12, 12, 5, 0.05F),
                        new VillagerTrades.ItemsAndEmeraldsToItems(ModItems.MEDIUM_ROAST_BEAN, 3, 2, ModItems.GROUND_MEDIUM_ROAST, 12, 12, 5, 0.05F),
                        new VillagerTrades.ItemsAndEmeraldsToItems(ModItems.DARK_ROAST_BEAN, 3, 2, ModItems.GROUND_DARK_ROAST, 12, 12, 5, 0.05F),
                        new VillagerTrades.ItemsAndEmeraldsToItems(ModItems.ESPRESSO_ROAST_BEAN, 3, 2, ModItems.GROUND_ESPRESSO_ROAST, 12, 12, 5, 0.05F)
                },
                3, new VillagerTrades.ItemListing[]{
                        new VillagerTrades.ItemsForEmeralds(CoffeeUtil.getBaseCoffee(CoffeeTypes.LIGHT_ROAST), 3, 1, 12, 10),
                        new VillagerTrades.ItemsForEmeralds(CoffeeUtil.getBaseCoffee(CoffeeTypes.MEDIUM_ROAST), 3, 1, 12, 10),
                        new VillagerTrades.ItemsForEmeralds(CoffeeUtil.getBaseCoffee(CoffeeTypes.DARK_ROAST), 3, 1, 12, 10),
                        new VillagerTrades.ItemsForEmeralds(LatteItem.getStandardLatte(), 5, 1, 12, 15),
                        new VillagerTrades.EmeraldForItems(PDItems.MILK_BOTTLE, 16, 12, 5)
                },
                4, new VillagerTrades.ItemListing[] {
                        new VillagerTrades.ItemsForEmeralds(CoffeeUtil.getWithAdditions(LatteItem.getStandardLatte(), "pdapi:chorus_fruit"), 10, 1, 12, 20),
                        new VillagerTrades.ItemsForEmeralds(CoffeeUtil.getWithAdditions(LatteItem.getStandardLatte(), "pdapi:glow_berries"), 7, 1, 12, 20),
                        new VillagerTrades.ItemsForEmeralds(CoffeeUtil.getWithAdditions(LatteItem.getStandardLatte(), "pdapi:honey", "pdapi:sugar"), 5, 1, 12, 20),
                        new VillagerTrades.ItemsForEmeralds(CoffeeUtil.getWithAdditions(LatteItem.getStandardLatte(), "plutoscoffee:mocha_syrup", "plutoscoffee:caramel"), 8, 1, 12, 20),
                        new VillagerTrades.ItemsAndEmeraldsToItems(ModItems.DARK_ROAST_BEAN, 16, 2, ModItems.DECAF_ROAST_BEAN, 16, 12, 20, 0.05F)
                },
                5, new VillagerTrades.ItemListing[]{
                        new VillagerTrades.ItemsForEmeralds(ModItems.COFFEE_BREWER, 24, 1, 12, 25),
                        new VillagerTrades.ItemsForEmeralds(ModItems.COFFEE_GRINDR, 20, 1, 12, 25),
                        new VillagerTrades.ItemsForEmeralds(ModItems.ESPRESSO_MACHINE, 30, 1, 12, 25)
                }
        )));
    }

    private static VillagerProfession register(String id, VillagerProfession profession ) {
        return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, PlutosCoffee.asId(id), profession);
    }

}
