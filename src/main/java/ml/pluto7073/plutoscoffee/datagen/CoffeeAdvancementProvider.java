package ml.pluto7073.plutoscoffee.datagen;

import ml.pluto7073.pdapi.PDAPI;
import ml.pluto7073.pdapi.addition.AdditionHolder;
import ml.pluto7073.pdapi.addition.DrinkAddition;
import ml.pluto7073.pdapi.addition.DrinkAdditionManager;
import ml.pluto7073.pdapi.component.DrinkAdditions;
import ml.pluto7073.pdapi.component.PDComponents;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import ml.pluto7073.plutoscoffee.registry.ModComponents;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.Util;
import net.minecraft.advancements.*;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CoffeeAdvancementProvider extends FabricAdvancementProvider {

    public CoffeeAdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        DrinkAdditionManager.register(new AdditionHolder(PDAPI.asId("milk"), dummy()));
        DrinkAdditionManager.register(new AdditionHolder(PDAPI.asId("sugar"), dummy()));
        DrinkAdditionManager.register(new AdditionHolder(PDAPI.asId("burnt"), dummy()));
        DrinkAdditionManager.register(new AdditionHolder(PDAPI.asId("diamond"), dummy()));
        DrinkAdditionManager.register(new AdditionHolder(PDAPI.asId("netherite"), dummy()));

        // Some are disabled cause errors

        AdvancementHolder obtainCoffeeCherry = Builder.advancement().parent(new AdvancementHolder(new ResourceLocation("husbandry/plant_seed"), null))
                .display(ModItems.COFFEE_BERRY,
                Component.translatable("advancements.plutoscoffee.husbandry.obtain_coffee_cherry.title"),
                Component.translatable("advancements.plutoscoffee.husbandry.obtain_coffee_cherry.description"),
                null, AdvancementType.TASK,true, true, false)
                .addCriterion("has_cherry", has(ModItems.COFFEE_BERRY)).requirements(AdvancementRequirements.Strategy.OR)
                .save(consumer, "plutoscoffee:husbandry/obtain_coffee_cherry");

//        Builder.advancement().parent(obtainCoffeeCherry).display(Items.CAMPFIRE,
//                Component.translatable("advancements.plutoscoffee.husbandry.drink_burnt_latte.title"),
//                Component.translatable("advancements.plutoscoffee.husbandry.drink_burnt_latte.description"),
//                null, AdvancementType.TASK, true, true, false)
//                .addCriterion("drank_latte", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().of(ModItems.LATTE)
//                        .hasComponents(DataComponentPredicate.builder().
//                                expect(PDComponents.ADDITIONS, DrinkAdditions.of(PDAPI.asId("burnt")))
//                                .build()))).requirements(AdvancementRequirements.Strategy.OR)
//                .save(consumer, "plutoscoffee:husbandry/drink_burnt_latte");

        AdvancementHolder brewCoffee = Builder.advancement().parent(obtainCoffeeCherry).display(ModItems.LIGHT_ROAST_BEAN,
                        Component.translatable("advancements.plutoscoffee.husbandry.brew_coffee.title"),
                        Component.translatable("advancements.plutoscoffee.husbandry.brew_coffee.description"),
                        null, AdvancementType.TASK, true, true, false)
                        .addCriterion("has_coffee", has(ModItems.BREWED_COFFEE)).requirements(AdvancementRequirements.Strategy.OR)
                        .save(consumer, "plutoscoffee:husbandry/brew_coffee");

//        AdvancementHolder sweetCoffee = Builder.advancement().display(Items.SUGAR,
//                Component.translatable("advancements.plutoscoffee.husbandry.sweet_coffee.title"),
//                Component.translatable("advancements.plutoscoffee.husbandry.sweet_coffee.description"),
//                null, AdvancementType.GOAL, true, true, false).parent(brewCoffee)
//                .addCriterion("drank_sugar_coffee", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().of(ModItems.BREWED_COFFEE)
//                        .hasComponents(DataComponentPredicate.builder().expect(PDComponents.ADDITIONS, DrinkAdditions.of(List.of(
//                                PDAPI.asId("milk"), PDAPI.asId("milk"), PDAPI.asId("milk"), PDAPI.asId("milk"), PDAPI.asId("milk"),
//                                PDAPI.asId("milk"), PDAPI.asId("milk"), PDAPI.asId("milk"), PDAPI.asId("milk"), PDAPI.asId("milk"),
//                                PDAPI.asId("sugar"), PDAPI.asId("sugar"), PDAPI.asId("sugar"), PDAPI.asId("sugar"), PDAPI.asId("sugar"),
//                                PDAPI.asId("sugar"), PDAPI.asId("sugar"), PDAPI.asId("sugar"), PDAPI.asId("sugar"), PDAPI.asId("sugar")
//                        ))).build()))).requirements(AdvancementRequirements.Strategy.OR)
//                .save(consumer, "plutoscoffee:husbandry/sweet_coffee");
//
//        AdvancementHolder drinkingDiamonds = Builder.advancement().parent(sweetCoffee).display(Items.DIAMOND,
//                Component.translatable("advancements.plutoscoffee.husbandry.drinking_diamonds.title"),
//                Component.translatable("advancements.plutoscoffee.husbandry.drinking_diamonds.description"),
//                null, AdvancementType.GOAL, true, true, false)
//                .addCriterion("drank_coffee", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().of(ModItems.BREWED_COFFEE, ModItems.LATTE)
//                        .hasComponents(DataComponentPredicate.builder().expect(PDComponents.ADDITIONS, DrinkAdditions.of(PDAPI.asId("diamond"))).build())))
//                .requirements(AdvancementRequirements.Strategy.OR).save(consumer, "plutoscoffee:husbandry/drinking_diamonds");
//
//        Builder.advancement().parent(drinkingDiamonds).display(Items.NETHERITE_INGOT,
//                Component.translatable("advancements.plutoscoffee.husbandry.whyy.title"),
//                Component.translatable("advancements.plutoscoffee.husbandry.whyy.description"),
//                null, AdvancementType.CHALLENGE, true, true, false)
//                .addCriterion("drank_coffee", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().of(ModItems.BREWED_COFFEE, ModItems.LATTE)
//                        .hasComponents(DataComponentPredicate.builder().expect(PDComponents.ADDITIONS, DrinkAdditions.of(PDAPI.asId("netherite"))).build())))
//                .requirements(AdvancementRequirements.Strategy.OR).save(consumer, "plutoscoffee:husbandry/whyy");
    }

    private static DrinkAddition dummy() {
        return new DrinkAddition.Builder().build();
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(item);
    }

}
