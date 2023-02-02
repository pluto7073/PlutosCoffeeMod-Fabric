package ml.pluto7073.plutoscoffee.blocks;

import ml.pluto7073.plutoscoffee.gui.CoffeeWorkstationScreenHandler;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CoffeeWorkstationBlock extends CraftingTableBlock {

    private static final Text TITLE = Text.translatable("container.coffee_workstation");

    public CoffeeWorkstationBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            //player.incrementStat();
            return ActionResult.CONSUME;
        }
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntity) -> {
            return new CoffeeWorkstationScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
        }, TITLE);
    }

    @Override
    public Item asItem() {
        return ModItems.COFFEE_WORKSTATION;
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.COFFEE_WORKSTATION, 1);
    }

}
