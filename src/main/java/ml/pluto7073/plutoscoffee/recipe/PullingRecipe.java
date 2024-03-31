package ml.pluto7073.plutoscoffee.recipe;

import com.google.gson.JsonObject;
import ml.pluto7073.plutoscoffee.blocks.EspressoMachineBlockEntity;
import ml.pluto7073.plutoscoffee.registry.ModRecipes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class PullingRecipe implements Recipe<Inventory> {

    private final Identifier id;
    public final Ingredient grounds, base;
    public final int groundsRequired, pullTime;
    final ItemStack result;

    public PullingRecipe(Identifier id, Ingredient grounds, Ingredient base, int groundsRequired, int pullTime, ItemStack result) {
        this.id = id;
        this.grounds = grounds;
        this.base = base;
        this.groundsRequired = groundsRequired;
        this.pullTime = pullTime;
        this.result = result;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        ItemStack groundsStack = inventory.getStack(EspressoMachineBlockEntity.GROUNDS_SLOT_INDEX);
        ItemStack baseStack1 = inventory.getStack(EspressoMachineBlockEntity.ESPRESSO_SLOT_INDEX);
        ItemStack baseStack2 = inventory.getStack(EspressoMachineBlockEntity.ESPRESSO_SLOT_2_INDEX);
        return grounds.test(groundsStack) && groundsStack.getCount() >= groundsRequired && (base.test(baseStack1) || base.test(baseStack2));
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return result.copy();
    }

    @Override
    public Identifier getId() {
        return id;
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
        public PullingRecipe read(Identifier id, JsonObject json) {
            Ingredient grounds = Ingredient.fromJson(JsonHelper.getObject(json, "grounds"));
            Ingredient base = Ingredient.fromJson(JsonHelper.getObject(json, "base"));
            int groundsRequired = JsonHelper.getInt(json, "groundsRequired");
            int pullTime = JsonHelper.getInt(json, "pullTime", 400);
            ItemStack result = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
            return new PullingRecipe(id, grounds, base, groundsRequired, pullTime, result);
        }

        @Override
        public PullingRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient grounds = Ingredient.fromPacket(buf);
            Ingredient base = Ingredient.fromPacket(buf);
            int groundsCount = buf.readInt();
            int pullTime = buf.readInt();
            ItemStack result = buf.readItemStack();
            return new PullingRecipe(id, grounds, base, groundsCount, pullTime, result);
        }

        @Override
        public void write(PacketByteBuf buf, PullingRecipe recipe) {
            recipe.grounds.write(buf);
            recipe.base.write(buf);
            buf.writeInt(recipe.groundsRequired);
            buf.writeInt(recipe.pullTime);
            buf.writeItemStack(recipe.result);
        }

    }

}
