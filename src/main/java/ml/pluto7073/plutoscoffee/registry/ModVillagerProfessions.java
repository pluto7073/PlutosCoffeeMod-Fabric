package ml.pluto7073.plutoscoffee.registry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import ml.pluto7073.pdapi.item.PDItems;
import ml.pluto7073.plutoscoffee.CoffeeUtil;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import ml.pluto7073.plutoscoffee.items.LatteItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.function.Predicate;

public class ModVillagerProfessions {

    public static final VillagerProfession BARISTA;

    private static final Predicate<RegistryEntry<PointOfInterestType>> WORKSTATION_PREDICATE = (entry) -> entry.matchesKey(ModPointOfInterests.DRINK_WORKSTATION);

    static {
        BARISTA = register("barista",
                new VillagerProfession(
                        PlutosCoffee.asId("barista").toString(),
                        WORKSTATION_PREDICATE,
                        WORKSTATION_PREDICATE,
                        ImmutableSet.of(),
                        ImmutableSet.of(),
                        SoundEvents.ENTITY_VILLAGER_WORK_CLERIC
                )
        );
    }

    public static void init() {}

    public static void postInit() {
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(BARISTA, new Int2ObjectOpenHashMap<>(ImmutableMap.of(
                1, new TradeOffers.Factory[]{
                        new TradeOffers.BuyForOneEmeraldFactory(ModItems.COFFEE_BERRY, 24, 16, 2),
                        new TradeOffers.SellItemFactory(ModItems.MOCHA_SAUCE, 1, 3, 16, 2),
                        new TradeOffers.BuyForOneEmeraldFactory(ModItems.COFFEE_BEAN, 22, 16, 2),
                        new TradeOffers.BuyForOneEmeraldFactory(ModItems.USED_COFFEE_GROUNDS, 26, 16, 2)
                },
                2, new TradeOffers.Factory[]{
                        new TradeOffers.ProcessItemFactory(ModItems.LIGHT_ROAST_BEAN, 3, 2, ModItems.GROUND_LIGHT_ROAST, 12, 12, 5),
                        new TradeOffers.ProcessItemFactory(ModItems.MEDIUM_ROAST_BEAN, 3, 2, ModItems.GROUND_MEDIUM_ROAST, 12, 12, 5),
                        new TradeOffers.ProcessItemFactory(ModItems.DARK_ROAST_BEAN, 3, 2, ModItems.GROUND_DARK_ROAST, 12, 12, 5),
                        new TradeOffers.ProcessItemFactory(ModItems.ESPRESSO_ROAST_BEAN, 3, 2, ModItems.GROUND_ESPRESSO_ROAST, 12, 12, 5)
                },
                3, new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(CoffeeUtil.getBaseCoffee(CoffeeTypes.LIGHT_ROAST), 3, 1, 12, 10),
                        new TradeOffers.SellItemFactory(CoffeeUtil.getBaseCoffee(CoffeeTypes.MEDIUM_ROAST), 3, 1, 12, 10),
                        new TradeOffers.SellItemFactory(CoffeeUtil.getBaseCoffee(CoffeeTypes.DARK_ROAST), 3, 1, 12, 10),
                        new TradeOffers.SellItemFactory(LatteItem.getStandardLatte(), 5, 1, 12, 15),
                        new TradeOffers.BuyForOneEmeraldFactory(PDItems.MILK_BOTTLE, 16, 12, 5)
                },
                4, new TradeOffers.Factory[] {
                        new TradeOffers.SellItemFactory(CoffeeUtil.getWithAdditions(LatteItem.getStandardLatte(), "pdapi:chorus_fruit"), 10, 1, 12, 20),
                        new TradeOffers.SellItemFactory(CoffeeUtil.getWithAdditions(LatteItem.getStandardLatte(), "pdapi:glow_berries"), 7, 1, 12, 20),
                        new TradeOffers.SellItemFactory(CoffeeUtil.getWithAdditions(LatteItem.getStandardLatte(), "pdapi:honey", "pdapi:sugar"), 5, 1, 12, 20),
                        new TradeOffers.SellItemFactory(CoffeeUtil.getWithAdditions(LatteItem.getStandardLatte(), "plutoscoffee:mocha_syrup", "plutoscoffee:caramel"), 8, 1, 12, 20)
                },
                5, new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(ModItems.COFFEE_BREWER, 24, 1, 12, 25),
                        new TradeOffers.SellItemFactory(ModItems.COFFEE_GRINDR, 20, 1, 12, 25),
                        new TradeOffers.SellItemFactory(ModItems.ESPRESSO_MACHINE, 30, 1, 12, 25)
                }
        )));
    }

    private static VillagerProfession register(String id, VillagerProfession profession ) {
        return Registry.register(Registries.VILLAGER_PROFESSION, PlutosCoffee.asId(id), profession);
    }

}
