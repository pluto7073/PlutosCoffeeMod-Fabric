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
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
public class PullingRecipe implements Recipe<Container> {

    public final Ingredient grounds, base;
    public final int groundsRequired, pullTime;
    final ItemStack result;

    public PullingRecipe(Ingredient grounds, Ingredient base, int groundsRequired, int pullTime, ItemStack result) {
        this.grounds = grounds;
        this.base = base;
        this.groundsRequired = groundsRequired;
        this.pullTime = pullTime;
        this.result = result;
    }

    @Override
    public boolean matches(Container container, Level level) {
        ItemStack groundsStack = container.getItem(EspressoMachineBlockEntity.GROUNDS_SLOT_INDEX);
        ItemStack baseStack1 = container.getItem(EspressoMachineBlockEntity.ESPRESSO_SLOT_INDEX);
        ItemStack baseStack2 = container.getItem(EspressoMachineBlockEntity.ESPRESSO_SLOT_2_INDEX);
        return grounds.test(groundsStack) && groundsStack.getCount() >= groundsRequired && (base.test(baseStack1) || base.test(baseStack2));
    }

    @Override
    public ItemStack assemble(Container inventory, HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
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

        private static final MapCodec<PullingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(Ingredient.CODEC.fieldOf("grounds").forGetter(recipe -> recipe.grounds),
                        Ingredient.CODEC.fieldOf("base").forGetter(recipe -> recipe.base),
                        Codec.INT.fieldOf("groundsRequired").forGetter(recipe -> recipe.groundsRequired),
                        Codec.INT.fieldOf("pullTime").orElse(400).forGetter(recipe -> recipe.pullTime),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result))
                        .apply(instance, PullingRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, PullingRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<PullingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PullingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static PullingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            Ingredient grounds = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            Ingredient base = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            int groundsCount = buf.readInt();
            int pullTime = buf.readInt();
            ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
            return new PullingRecipe(grounds, base, groundsCount, pullTime, result);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buf, PullingRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.grounds);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.base);
            buf.writeInt(recipe.groundsRequired);
            buf.writeInt(recipe.pullTime);
            ItemStack.STREAM_CODEC.encode(buf, recipe.result);
        }

    }

}
