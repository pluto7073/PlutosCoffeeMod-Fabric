package ml.pluto7073.plutoscoffee.blocks;

import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModItems;
import ml.pluto7073.plutoscoffee.registry.ModStats;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class EspressoMachineBlock extends BaseEntityBlock {

    public static final BooleanProperty HAS_ESPRESSO;
    public static final BooleanProperty PULLING;
    public static final BooleanProperty HAS_MILK;
    public static final DirectionProperty FACING;

    protected static VoxelShape SHAPE;

    public EspressoMachineBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(HAS_ESPRESSO, false)
                .setValue(PULLING, false)
                .setValue(HAS_MILK, false)
                .setValue(FACING, Direction.NORTH));
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EspressoMachineBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlocks.ESPRESSO_MACHINE_BLOCK_ENTITY_TYPE, EspressoMachineBlockEntity::tick);
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof EspressoMachineBlockEntity) {
                player.openMenu((EspressoMachineBlockEntity) blockEntity);
                player.awardStat(ModStats.INTERACT_WITH_ESPRESSO_MACHINE);
            }
            return InteractionResult.CONSUME;
        }
    }

    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof EspressoMachineBlockEntity) {
                ((EspressoMachineBlockEntity) blockEntity).setCustomName(stack.getHoverName());
            }
        }
    }

    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.4 + (double) random.nextFloat() * 0.2;
        double y = pos.getY() + 0.3 + (double) random.nextFloat() * 0.3;
        double z = pos.getZ() + 0.4 + (double) random.nextFloat() * 0.2;
        BlockEntity entity = world.getBlockEntity(pos);
        if (!(entity instanceof CoffeeBrewerBlockEntity)) return;
        if (state.getValue(PULLING))
            world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
    }

    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof EspressoMachineBlockEntity) {
                Containers.dropContents(world, pos, (EspressoMachineBlockEntity) blockEntity);
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_ESPRESSO).add(HAS_MILK).add(PULLING).add(FACING);
    }

    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    public BlockState getPlacementState(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    static {
        SHAPE = Block.box(1, 0, 1, 15, 13, 15);
        HAS_ESPRESSO = BooleanProperty.create("has_espresso");
        HAS_MILK = BooleanProperty.create("has_milk");
        PULLING = BooleanProperty.create("pulling");
        FACING = BlockStateProperties.HORIZONTAL_FACING;
    }

}
