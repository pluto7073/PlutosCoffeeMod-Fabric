package ml.pluto7073.plutoscoffee.blocks;

import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.registry.ModStats;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CoffeeGrindrBlock extends BlockWithEntity {

    public static final BooleanProperty FULL_PROPERTY;
    public static final DirectionProperty FACING_PROPERTY;
    protected static final VoxelShape SHAPE;

    public CoffeeGrindrBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(FULL_PROPERTY, false)
                .with(FACING_PROPERTY, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CoffeeGrindrBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, ModBlocks.COFFEE_GRINDR_BLOCK_ENTITY_TYPE, CoffeeGrindrBlockEntity::tick);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FULL_PROPERTY).add(FACING_PROPERTY);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CoffeeGrindrBlockEntity) {
                player.openHandledScreen((CoffeeGrindrBlockEntity) blockEntity);
                player.incrementStat(ModStats.INTERACT_WITH_COFFEE_GRINDR);
            }
            return ActionResult.CONSUME;
        }
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CoffeeGrindrBlockEntity) {
                ((CoffeeGrindrBlockEntity) blockEntity).setCustomName(stack.getName());
            }
        }
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CoffeeGrindrBlockEntity) {
                ItemScatterer.spawn(world, pos, (CoffeeGrindrBlockEntity) blockEntity);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING_PROPERTY, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public Item asItem() {
        return ModItems.COFFEE_GRINDR;
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.COFFEE_GRINDR, 1);
    }

    static {
        FULL_PROPERTY = BooleanProperty.of("full");
        FACING_PROPERTY = Properties.HORIZONTAL_FACING;
        SHAPE = Block.createCuboidShape(3, 0, 3, 13, 16, 13);
    }

}
