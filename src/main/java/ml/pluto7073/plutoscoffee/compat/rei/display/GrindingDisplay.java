package ml.pluto7073.plutoscoffee.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import ml.pluto7073.plutoscoffee.compat.rei.CoffeeREI;
import ml.pluto7073.plutoscoffee.compat.rei.GrindingRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class GrindingDisplay extends BasicDisplay {

    private final EntryIngredient beans;
    private final EntryStack<?> grounds;

    public GrindingDisplay(GrindingRecipe recipe) {
        this(recipe.beans(), recipe.grounds());
    }

    public GrindingDisplay(Ingredient beans, ItemStack grounds) {
        this(EntryIngredients.ofIngredient(beans), EntryStacks.of(grounds));
    }

    public GrindingDisplay(EntryIngredient beans, EntryStack<?> grounds) {
        super(List.of(beans), List.of(EntryIngredient.of(grounds)));
        this.beans = beans;
        this.grounds = grounds;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CoffeeREI.GRINDING_DISPLAY;
    }

    public static DisplaySerializer<GrindingDisplay> serializer() {
        return new DisplaySerializer<>() {
            @Override
            public CompoundTag save(CompoundTag tag, GrindingDisplay display) {
                tag.put("beans", display.beans.saveIngredient());
                tag.put("grounds", display.grounds.saveStack());
                return tag;
            }

            @Override
            public GrindingDisplay read(CompoundTag tag) {
                EntryIngredient beans = EntryIngredient.read(tag.getList("beans", Tag.TAG_COMPOUND));
                EntryStack<?> grounds = EntryStack.read(tag.getCompound("grounds"));
                return new GrindingDisplay(beans, grounds);
            }
        };
    }

    public EntryStack<?> getGrounds() {
        return grounds;
    }

}
