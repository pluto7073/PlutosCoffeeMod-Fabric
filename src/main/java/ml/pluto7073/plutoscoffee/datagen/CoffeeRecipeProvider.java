package ml.pluto7073.plutoscoffee.datagen;

import com.mojang.datafixers.util.Pair;
import ml.pluto7073.pdapi.datagen.builder.WorkstationRecipeBuilder;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.coffee.CoffeeGrounds;
import ml.pluto7073.plutoscoffee.datagen.builders.PullingRecipeBuilder;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.tags.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CoffeeRecipeProvider extends FabricRecipeProvider {

    public static final List<Pair<ResourceLocation, WorkstationRecipeBuilder>> WORKSTATION_RECIPES = new ArrayList<>();

    public CoffeeRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        defaultPulling(exporter, ModItems.GROUND_LIGHT_ROAST, ModItems.BLONDE_ESPRESSO_SHOT);
        defaultPulling(exporter, ModItems.GROUND_ESPRESSO_ROAST, ModItems.ESPRESSO_SHOT);
        defaultPulling(exporter, ModItems.GROUND_DECAF_ROAST, ModItems.DECAF_ESPRESSO_SHOT);

        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModItems.COFFEE_BREWER)
                        .define('i', Items.IRON_INGOT).define('B', Items.BUCKET).define('P', Items.PAPER)
                        .pattern("iBi").pattern("iPi").pattern("i i")
                        .unlockedBy("has_coffee_grounds", has(ModItemTags.COFFEE_GROUNDS)).save(exporter, PlutosCoffee.asId("shaped/coffee_brewer"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModItems.ESPRESSO_MACHINE)
                        .define('I', Items.IRON_BLOCK).define('i', Items.IRON_INGOT).define('B', Items.BUCKET).define('P', Items.PAPER)
                        .pattern("IBI").pattern("iPi").pattern("iii")
                        .unlockedBy("has_coffee_grounds", has(ModItemTags.COFFEE_GROUNDS)).save(exporter, PlutosCoffee.asId("shaped/espresso_machine"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COFFEE_GRINDR)
                        .define('i', Items.IRON_INGOT).define('G', Items.GLASS).define('S', Items.STONECUTTER)
                        .pattern("iGi").pattern("iSi").pattern("i i")
                        .unlockedBy("has_grounds", has(ModItemTags.COFFEE_GROUNDS)).unlockedBy("has_beans", has(ModItemTags.COFFEE_BEANS))
                        .save(exporter, PlutosCoffee.asId("shaped/coffee_grinder"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.CARAMEL)
                        .define('s', Items.SUGAR).define('G', Items.GLASS_BOTTLE)
                        .pattern("sss").pattern("sss").pattern(" G ")
                        .unlockedBy("has_sugar", has(Items.SUGAR)).save(exporter, PlutosCoffee.asId("shaped/caramel"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.MOCHA_SAUCE)
                        .define('s', Items.SUGAR).define('G', Items.GLASS_BOTTLE).define('c', Items.COCOA_BEANS)
                        .pattern("scs").pattern("csc").pattern(" G ")
                        .unlockedBy("has_cocoa", has(Items.COCOA_BEANS)).save(exporter, PlutosCoffee.asId("shaped/mocha_syrup"));

        oneToOneConversionRecipe(exporter, ModItems.COFFEE_BEAN, ModItems.COFFEE_BERRY, null, 2);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.COFFEE_BEAN), RecipeCategory.FOOD, ModItems.LIGHT_ROAST_BEAN, 0.25f, 200)
                        .unlockedBy("has_cherry", has(ModItems.COFFEE_BERRY)).save(exporter, PlutosCoffee.asId("smelting/light_roast"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.LIGHT_ROAST_BEAN), RecipeCategory.FOOD, ModItems.MEDIUM_ROAST_BEAN, 0.25f, 200)
                        .unlockedBy("has_cherry", has(ModItems.COFFEE_BERRY)).save(exporter, PlutosCoffee.asId("smelting/medium_roast"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.MEDIUM_ROAST_BEAN), RecipeCategory.FOOD, ModItems.DARK_ROAST_BEAN, 0.25f, 200)
                        .unlockedBy("has_cherry", has(ModItems.COFFEE_BERRY)).save(exporter, PlutosCoffee.asId("smelting/dark_roast"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.DARK_ROAST_BEAN), RecipeCategory.FOOD, ModItems.ESPRESSO_ROAST_BEAN, 0.25f, 200)
                        .unlockedBy("has_cherry", has(ModItems.COFFEE_BERRY)).save(exporter, PlutosCoffee.asId("smelting/espresso_roast"));

        CoffeeGrounds.getBeansToGroundsRegistry().forEach((bean, ground) -> stoneCuttingGrounds(exporter, bean, ground));

        WORKSTATION_RECIPES.forEach(pair -> pair.getSecond().save(exporter, pair.getFirst()));
    }

    private static void stoneCuttingGrounds(RecipeOutput exporter, Item bean, Item grounds) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(grounds);

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(bean), RecipeCategory.FOOD, grounds)
                .unlockedBy("has_bean", has(bean)).save(exporter, id.withPrefix("stonecutting/"));
    }

    private static void defaultPulling(RecipeOutput exporter, Item grounds, Item shot) {
        new PullingRecipeBuilder(Ingredient.of(grounds), Ingredient.of(Items.GLASS_BOTTLE), 2, 400, new ItemStack(shot))
                .save(exporter, BuiltInRegistries.ITEM.getKey(shot).withPrefix("pulling/"));
    }

}
