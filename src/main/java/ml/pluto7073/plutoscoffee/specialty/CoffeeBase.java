package ml.pluto7073.plutoscoffee.specialty;

import ml.pluto7073.pdapi.specialty.SpecialtyDrinkBase;
import ml.pluto7073.pdapi.specialty.SpecialtyDrinkBaseSerializer;
import ml.pluto7073.plutoscoffee.CoffeeUtil;
import ml.pluto7073.plutoscoffee.coffee.CoffeeType;
import ml.pluto7073.plutoscoffee.coffee.CoffeeTypes;
import ml.pluto7073.plutoscoffee.registry.ModDrinkBases;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.world.item.ItemStack;

public record CoffeeBase(CoffeeType type) implements SpecialtyDrinkBase {

    @Override
    public ItemStack buildItemStack() {
        return CoffeeUtil.getBaseCoffee(type);
    }

    @Override
    public boolean matches(ItemStack stack) {
        if (!stack.is(ModItems.BREWED_COFFEE)) return false;
        return CoffeeUtil.getCoffeeType(stack) == type;
    }

    @Override
    public SpecialtyDrinkBaseSerializer serializer() {
        return ModDrinkBases.COFFEE_BASE;
    }

}
