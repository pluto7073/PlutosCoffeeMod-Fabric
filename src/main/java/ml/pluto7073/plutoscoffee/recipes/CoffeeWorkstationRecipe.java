package ml.pluto7073.plutoscoffee.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ml.pluto7073.plutoscoffee.Utils;
import ml.pluto7073.plutoscoffee.coffee.CoffeeAddition;
import ml.pluto7073.plutoscoffee.coffee.CoffeeAdditions;
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
        boolean b = base.test(inventory.getStack(0)) && addition.test(inventory.getStack(1));
        CoffeeAddition addIn = CoffeeAddition.byId(result.addition);
        if (addIn.getMaxAmount() > 0) {
            /*CoffeeAddition[] currentAddIns = Utils.getCoffeeAddIns(inventory.getStack(0));
            int count = 0;
            for (CoffeeAddition i : currentAddIns) {
                if (i.equals(addIn)) count++;
            }
            return b && count < addIn.getMaxAmount();*/
            return b;
        } else return b;
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return craft(inventory);
    }

    public ItemStack craft(Inventory inventory) {
        ItemStack stack = inventory.getStack(0).copy();
        stack.setCount(1);
        NbtList resAdds = stack.getOrCreateSubNbt("Coffee").getList("Additions", NbtElement.STRING_TYPE);
        if (resAdds == null) resAdds = new NbtList();
        resAdds.add(Utils.stringAsNbt(result.addition()));
        stack.getOrCreateSubNbt("Coffee").put("Additions", resAdds);

        return stack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResult(DynamicRegistryManager registryManager) {
        return this.result.example(base);
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

        ItemStack example(Ingredient base) {
            ItemStack example = base.getMatchingStacks()[0].copy();
            NbtList list = new NbtList();
            list.add(Utils.stringAsNbt(addition()));
            example.getOrCreateSubNbt("Coffee").put("Additions", list);
            return example;
        }

    }

}
