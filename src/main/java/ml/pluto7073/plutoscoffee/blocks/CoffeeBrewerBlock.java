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
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CoffeeBrewerBlock extends BlockWithEntity {

    /**
     * 0 - No Bottle <br>
     * 1 - Empty Bottle <br>
     * 2 - Filled Bottle
     */
    public static final IntProperty BOTTLE_PROPERTY;
    protected static final VoxelShape SHAPE;

    public CoffeeBrewerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(BOTTLE_PROPERTY, 0));
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CoffeeBrewerBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, ModBlocks.COFFEE_BREWER_BLOCK_ENTITY_TYPE, CoffeeBrewerBlockEntity::tick);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CoffeeBrewerBlockEntity) {
                player.openHandledScreen((CoffeeBrewerBlockEntity) blockEntity);
                player.incrementStat(ModStats.INTERACT_WITH_COFFEE_BREWER);
            }
            return ActionResult.CONSUME;
        }
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CoffeeBrewerBlockEntity) {
                ((CoffeeBrewerBlockEntity) blockEntity).setCustomName(stack.getName());
            }
        }
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double x = pos.getX() + 0.4 + (double) random.nextFloat() * 0.2;
        double y = pos.getY() + 0.3 + (double) random.nextFloat() * 0.3;
        double z = pos.getZ() + 0.4 + (double) random.nextFloat() * 0.2;
        BlockEntity entity = world.getBlockEntity(pos);
        if (!(entity instanceof CoffeeBrewerBlockEntity)) return;
        if (((CoffeeBrewerBlockEntity) entity).propertyDelegate.get(CoffeeBrewerBlockEntity.BREW_TIME_PROPERTY_INDEX) > 0)
            world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CoffeeBrewerBlockEntity) {
                ItemScatterer.spawn(world, pos, (CoffeeBrewerBlockEntity) blockEntity);
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

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BOTTLE_PROPERTY);
    }

    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public Item asItem() {
        return ModItems.COFFEE_BREWER;
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.COFFEE_BREWER, 1);
    }

    static {
        BOTTLE_PROPERTY = IntProperty.of("bottle", 0, 2);
        SHAPE = Block.createCuboidShape(5, 0, 5, 11, 8, 11);
    }

}
