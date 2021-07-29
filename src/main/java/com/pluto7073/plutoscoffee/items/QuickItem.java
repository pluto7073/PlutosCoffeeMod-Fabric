package com.pluto7073.plutoscoffee.items;

import com.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.explosion.Explosion;

public class QuickItem extends Item {
    static PlayerEntity player;
    static World world;
    public static String name = "";
    public static ItemGroup tab = ItemGroup.MISC;
    public QuickItem(Settings settings) {
        super(settings);
    }
    static void setVars() {

    }
    public static void register() {
        setVars();
        final Item ITEM = new Item(new Item.Settings().group(tab));
        Registry.register(Registry.ITEM, new Identifier(PlutosCoffee.MOD_ID, name), ITEM);
    }
    public static void registerAll() {
        TestItem.register();
    }
    public static void createExplosion(Entity target, double x, double y, double z, boolean fire, float strength) {
        world.createExplosion(target, x, y, z, strength, fire, Explosion.DestructionType.DESTROY);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return super.use(world, user, hand);
    }

    @Override
    public boolean isFireproof() {
        return super.isFireproof();
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return super.canRepair(stack, ingredient);
    }

    public static void createLightningBolt(World world, double x, double y, double z) {
        world.disconnect();
    }
}
