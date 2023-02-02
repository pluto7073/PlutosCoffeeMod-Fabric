package ml.pluto7073.plutoscoffee.recipes;

import com.google.gson.JsonObject;
import ml.pluto7073.plutoscoffee.Utils;
import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModMisc;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class CoffeeWorkstationRecipe implements Recipe<Inventory> {

    final Ingredient base;
    final Ingredient addition;
    final ItemStack result;
    private final Identifier id;

    public CoffeeWorkstationRecipe(Identifier id, Ingredient base, Ingredient addition, ItemStack result) {
        this.base = base;
        this.addition = addition;
        this.result = result;
        this.id = id;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return base.test(inventory.getStack(0)) && addition.test(inventory.getStack(1));
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        ItemStack stack = result.copy();
        NbtList sourceAdds = inventory.getStack(0).getOrCreateSubNbt("Coffee").getList("Additions", NbtElement.STRING_TYPE);
        NbtList resAdds = stack.getOrCreateSubNbt("Coffee").getList("Additions", NbtElement.STRING_TYPE);
        resAdds.addAll(sourceAdds);
        String coffeeType = inventory.getStack(0).getOrCreateSubNbt("Coffee").getString("CoffeeType");
        stack.getOrCreateSubNbt("Coffee").put("Additions", resAdds);
        stack.getOrCreateSubNbt("Coffee").putString("CoffeeType", coffeeType);

        return stack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getOutput() {
        return this.result;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    public boolean testAddition(ItemStack stack) {
        return addition.test(stack);
    }

    public ItemStack createIcon() {
        return new ItemStack(ModBlocks.COFFEE_WORKSTATION);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModMisc.COFFEE_WORK_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModMisc.COFFEE_WORK_RECIPE_TYPE;
    }

    public boolean isEmpty() {
        return Stream.of(this.base, this.addition).anyMatch((ingredient) -> ingredient.getMatchingStacks().length == 0);
    }

    public static class Serializer implements RecipeSerializer<CoffeeWorkstationRecipe> {

        public Serializer() {}

        @Override
        public CoffeeWorkstationRecipe read(Identifier id, JsonObject jsonObject) {
            Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "base"));
            Ingredient ingredient2 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "addition"));
            JsonObject resultObject = JsonHelper.getObject(jsonObject, "result");
            Item item = ShapedRecipe.getItem(resultObject);
            int count = JsonHelper.getInt(resultObject, "count", 1);
            ItemStack itemStack = new ItemStack(item, count);
            NbtList list = new NbtList();
            list.add(Utils.stringAsNbt(JsonHelper.getString(resultObject, "addition")));
            itemStack.getOrCreateSubNbt("Coffee").put("Additions", list);
            return new CoffeeWorkstationRecipe(id, ingredient, ingredient2, itemStack);
        }

        @Override
        public CoffeeWorkstationRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient ingredient = Ingredient.fromPacket(buf);
            Ingredient ingredient2 = Ingredient.fromPacket(buf);
            ItemStack itemStack = buf.readItemStack();
            return new CoffeeWorkstationRecipe(id, ingredient, ingredient2, itemStack);
        }

        @Override
        public void write(PacketByteBuf buf, CoffeeWorkstationRecipe recipe) {
            recipe.base.write(buf);
            recipe.addition.write(buf);
            buf.writeItemStack(recipe.result);
        }

    }

}
