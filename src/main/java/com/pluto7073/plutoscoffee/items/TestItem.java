package com.pluto7073.plutoscoffee.items;

import com.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class TestItem extends QuickItem {

    static void setVars() {
        name = "test_item";
        tab = ItemGroup.BUILDING_BLOCKS;
    }

    public TestItem() {
        super(new Item.Settings().group(tab).fireproof());
    }

    public static void register() {
        setVars();
        final Item ITEM = new TestItem();
        Registry.register(Registry.ITEM, new Identifier(PlutosCoffee.MOD_ID, name), ITEM);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        createExplosion(player, player.getX(), player.getY(), player.getZ(), true, 5.0f);
        return super.use(world, user, hand);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return true;
    }

    @Override
    public boolean isFireproof() {
        return true;
    }
}
