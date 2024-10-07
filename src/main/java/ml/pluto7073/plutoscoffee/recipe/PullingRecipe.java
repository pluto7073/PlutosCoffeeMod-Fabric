package ml.pluto7073.plutoscoffee.recipe;

import com.google.gson.JsonObject;
import ml.pluto7073.plutoscoffee.blocks.EspressoMachineBlockEntity;
import ml.pluto7073.plutoscoffee.registry.ModRecipes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
public class PullingRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    public final Ingredient grounds, base;
    public final int groundsRequired, pullTime;
    final ItemStack result;

    public PullingRecipe(ResourceLocation id, Ingredient grounds, Ingredient base, int groundsRequired, int pullTime, ItemStack result) {
        this.id = id;
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
    public ItemStack assemble(Container inventory, RegistryAccess registryManager) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryManager) {
        return result.copy();
    }

    public ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
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

        @Override
        public PullingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient grounds = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "grounds"));
            Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "base"));
            int groundsRequired = GsonHelper.getAsInt(json, "groundsRequired");
            int pullTime = GsonHelper.getAsInt(json, "pullTime", 400);
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new PullingRecipe(id, grounds, base, groundsRequired, pullTime, result);
        }

        @Override
        public PullingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient grounds = Ingredient.fromNetwork(buf);
            Ingredient base = Ingredient.fromNetwork(buf);
            int groundsCount = buf.readInt();
            int pullTime = buf.readInt();
            ItemStack result = buf.readItem();
            return new PullingRecipe(id, grounds, base, groundsCount, pullTime, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, PullingRecipe recipe) {
            recipe.grounds.toNetwork(buf);
            recipe.base.toNetwork(buf);
            buf.writeInt(recipe.groundsRequired);
            buf.writeInt(recipe.pullTime);
            buf.writeItem(recipe.result);
        }

    }

}
