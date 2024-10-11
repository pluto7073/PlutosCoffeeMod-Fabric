package ml.pluto7073.plutoscoffee.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import ml.pluto7073.plutoscoffee.registry.ModBlocks;
import ml.pluto7073.plutoscoffee.registry.ModStats;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class CoffeeBrewerBlock extends BaseEntityBlock {

    /**
     * 0 - No Bottle <br>
     * 1 - Empty Bottle <br>
     * 2 - Filled Bottle
     */
    public static final IntegerProperty BOTTLE_PROPERTY;
    public static final MapCodec<CoffeeBrewerBlock> CODEC = simpleCodec(CoffeeBrewerBlock::new);
    protected static final VoxelShape SHAPE;

    public CoffeeBrewerBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(BOTTLE_PROPERTY, 0));
    }

    @Override
    protected MapCodec<CoffeeBrewerBlock> codec() {
        return CODEC;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CoffeeBrewerBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlocks.COFFEE_BREWER_BLOCK_ENTITY_TYPE, CoffeeBrewerBlockEntity::tick);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CoffeeBrewerBlockEntity) {
                player.openMenu((CoffeeBrewerBlockEntity) blockEntity);
                player.awardStat(ModStats.INTERACT_WITH_COFFEE_BREWER);
            }
            return InteractionResult.CONSUME;
        }
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.4 + (double) random.nextFloat() * 0.2;
        double y = pos.getY() + 0.3 + (double) random.nextFloat() * 0.3;
        double z = pos.getZ() + 0.4 + (double) random.nextFloat() * 0.2;
        BlockEntity entity = level.getBlockEntity(pos);
        if (!(entity instanceof CoffeeBrewerBlockEntity)) return;
        if (((CoffeeBrewerBlockEntity) entity).containerData.get(CoffeeBrewerBlockEntity.BREW_TIME_PROPERTY_INDEX) > 0)
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CoffeeBrewerBlockEntity) {
                Containers.dropContents(level, pos, (CoffeeBrewerBlockEntity) blockEntity);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BOTTLE_PROPERTY);
    }

    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    static {
        BOTTLE_PROPERTY = IntegerProperty.create("bottle", 0, 2);
        SHAPE = Block.box(5, 0, 5, 11, 8, 11);
    }

}
