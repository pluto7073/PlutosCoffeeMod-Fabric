package ml.pluto7073.plutoscoffee.datagen;

import com.mojang.datafixers.util.Pair;
import ml.pluto7073.pdapi.addition.DrinkAddition;
import ml.pluto7073.pdapi.addition.action.ApplyStatusEffectAction;
import ml.pluto7073.pdapi.addition.action.OnDrinkAction;
import ml.pluto7073.pdapi.addition.action.RestoreHungerAction;
import ml.pluto7073.pdapi.datagen.builder.WorkstationRecipeBuilder;
import ml.pluto7073.pdapi.datagen.provider.DrinkAdditionProvider;
import ml.pluto7073.pdapi.tag.PDTags;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class CoffeeAdditionProvider extends DrinkAdditionProvider {

    public CoffeeAdditionProvider(FabricDataOutput out, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(out, completableFuture);
    }

    @Override
    public void buildAdditions(BiConsumer<ResourceLocation, DrinkAddition> consumer) {
        itemAddition(consumer, ModItems.BLONDE_ESPRESSO_SHOT, 1444354, 85);
        itemAddition(consumer, ModItems.ESPRESSO_SHOT, 1444354, 75);
        itemAddition(consumer, ModItems.DECAF_ESPRESSO_SHOT, 1444354, 2);
        itemAddition(consumer, ModItems.CARAMEL, 5711373, 0,
                new ApplyStatusEffectAction(MobEffects.REGENERATION, 200, 0),
                new ApplyStatusEffectAction(MobEffects.ABSORPTION, 400, 0));
        itemAddition(consumer, ModItems.MOCHA_SAUCE, 3152394, 0,
                new RestoreHungerAction(6,5));
    }

    private static void itemAddition(BiConsumer<ResourceLocation, DrinkAddition> consumer, Item item, int color, int caffeine, OnDrinkAction... actions) {
        DrinkAddition.Builder addition = builder().name("")
                .chemical("caffeine", caffeine)
                .changesColor(color != 0)
                .color(color);
        for (OnDrinkAction action : actions) {
            addition.addAction(action);
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        consumer.accept(id, addition.build());

        WorkstationRecipeBuilder builder = new WorkstationRecipeBuilder(Ingredient.of(PDTags.WORKSTATION_DRINKS), Ingredient.of(item), id);
        CoffeeRecipeProvider.WORKSTATION_RECIPES.add(new Pair<>(id.withPrefix("add_"), builder));
    }

}
