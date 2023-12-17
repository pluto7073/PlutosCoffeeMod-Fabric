package ml.pluto7073.plutoscoffee.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ml.pluto7073.plutoscoffee.Utils;
import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.registry.ModMisc;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class CoffeeWorkstationRecipe implements Recipe<Inventory> {

    final Ingredient base;
    final Ingredient addition;
    final CoffeeStackBuilder result;
    //public final Identifier id;

    public CoffeeWorkstationRecipe(Ingredient base, Ingredient addition, CoffeeStackBuilder result) {
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return base.test(inventory.getStack(0)) && addition.test(inventory.getStack(1));
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return craft(inventory);
    }

    public ItemStack craft(Inventory inventory) {
        ItemStack stack = new ItemStack(ModItems.BREWED_COFFEE, 1);
        NbtList sourceAdds = inventory.getStack(0).getOrCreateSubNbt("Coffee").getList("Additions", NbtElement.STRING_TYPE);
        NbtList resAdds = new NbtList();
        resAdds.addAll(sourceAdds);
        resAdds.add(Utils.stringAsNbt(result.addition()));
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
    public ItemStack getResult(DynamicRegistryManager registryManager) {
        return this.result.example();
    }

    public boolean testAddition(ItemStack stack) {
        return addition.test(stack);
    }

    public boolean testBase(ItemStack stack) {
        return base.test(stack);
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

        private static final MapCodec<CoffeeStackBuilder> RESULT_STACK_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("result").forGetter(CoffeeStackBuilder::addition)).apply(instance, CoffeeStackBuilder::new));

        private static final Codec<CoffeeWorkstationRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(Ingredient.ALLOW_EMPTY_CODEC.fieldOf("base").forGetter((recipe) -> recipe.base), Ingredient.ALLOW_EMPTY_CODEC.fieldOf("addition").forGetter((recipe) -> recipe.addition), RESULT_STACK_CODEC.forGetter(recipe -> recipe.result)).apply(instance, CoffeeWorkstationRecipe::new));

        public Serializer() {}

        @Override
        public Codec<CoffeeWorkstationRecipe> codec() {
            return CODEC;
        }

        @Override
        public CoffeeWorkstationRecipe read(PacketByteBuf buf) {
            Ingredient ingredient = Ingredient.fromPacket(buf);
            Ingredient ingredient2 = Ingredient.fromPacket(buf);
            String result = buf.readString();
            return new CoffeeWorkstationRecipe(ingredient, ingredient2, new CoffeeStackBuilder(result));
        }

        @Override
        public void write(PacketByteBuf buf, CoffeeWorkstationRecipe recipe) {
            recipe.base.write(buf);
            recipe.addition.write(buf);
            buf.writeString(recipe.result.addition());
        }

    }

    public record CoffeeStackBuilder(String addition) {

        ItemStack example() {
            ItemStack example = new ItemStack(ModItems.BREWED_COFFEE, 1);
            NbtList list = new NbtList();
            list.add(Utils.stringAsNbt(addition()));
            example.getOrCreateSubNbt("Coffee").put("Additions", list);
            return example;
        }

    }

}
