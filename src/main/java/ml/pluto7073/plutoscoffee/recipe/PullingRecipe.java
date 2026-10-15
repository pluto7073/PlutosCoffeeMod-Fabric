package ml.pluto7073.plutoscoffee.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ml.pluto7073.plutoscoffee.blocks.EspressoMachineBlockEntity;
import ml.pluto7073.plutoscoffee.registry.ModRecipes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
public record PullingRecipe(Ingredient grounds, Ingredient base, int groundsRequired, int pullTime,
                            ItemStack result) implements Recipe<PullingRecipeInput> {

    @Override
    public boolean matches(PullingRecipeInput container, Level level) {
        ItemStack groundsStack = container.grounds();
        ItemStack baseStack1 = container.leftBase();
        ItemStack baseStack2 = container.rightBase();
        return grounds.test(groundsStack) && groundsStack.getCount() >= groundsRequired && (base.test(baseStack1) || base.test(baseStack2));
    }

    @Override
    public ItemStack assemble(PullingRecipeInput inventory, HolderLookup.Provider registryManager) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryManager) {
        return result.copy();
    }

    public ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(grounds, base);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.PULLING_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.PULLING_RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<PullingRecipe> {

        public static final MapCodec<PullingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(Ingredient.CODEC.fieldOf("grounds").forGetter(PullingRecipe::grounds),
                        Ingredient.CODEC.fieldOf("base").forGetter(PullingRecipe::base),
                        Codec.INT.fieldOf("groundsRequired").forGetter(PullingRecipe::groundsRequired),
                        Codec.INT.fieldOf("pullTime").forGetter(PullingRecipe::pullTime),
                        ItemStack.CODEC.fieldOf("result").forGetter(PullingRecipe::result))
                        .apply(instance, PullingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PullingRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, PullingRecipe::grounds,
                Ingredient.CONTENTS_STREAM_CODEC, PullingRecipe::base,
                ByteBufCodecs.INT, PullingRecipe::groundsRequired,
                ByteBufCodecs.INT, PullingRecipe::pullTime,
                ItemStack.STREAM_CODEC, PullingRecipe::result,
                PullingRecipe::new
        );

        @Override
        public MapCodec<PullingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PullingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

}
