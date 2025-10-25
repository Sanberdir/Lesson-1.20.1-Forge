package ru.sanberdir.lesson_1_20_1_forge.blocks.custom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.sanberdir.lesson_1_20_1_forge.blocks.InitBlocks;

import java.util.Map;

public class CandleLemonadeCake extends AbstractCandleBlock {

    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;

    private static final VoxelShape CAKE_SHAPE =
            Block.box(1, 0, 1, 15, 8, 15);

    private static final VoxelShape CANDLE_SHAPE =
            Block.box(7, 8, 7, 9, 14, 9);

    private static final VoxelShape COMBINED_SHAPE =
            Shapes.or(CAKE_SHAPE, CANDLE_SHAPE);

    private static final Iterable<Vec3> PARTICLE_OFFSETS =
            ImmutableList.of(new Vec3(0.5, 1.0, 0.5));

    private static final Map<Block, CandleLemonadeCake> BY_CANDLE = Maps.newHashMap();

    public CandleLemonadeCake(Block candle, BlockBehaviour.Properties props) {
        super(props);
        registerDefaultState(getStateDefinition().any().setValue(LIT, false));
        BY_CANDLE.put(candle, this);
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(BlockState state) {
        return PARTICLE_OFFSETS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        return COMBINED_SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {

        ItemStack stack = player.getItemInHand(hand);
        boolean isFireStarter = stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE);

        // Поджечь свечу
        if (isFireStarter && canLight(state)) {
            level.setBlock(pos, state.setValue(LIT, true), 3);
            level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE,
                    SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.4F + 0.8F);

            if (!player.isCreative()) {
                if (stack.is(Items.FLINT_AND_STEEL)) {
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                } else {
                    stack.shrink(1);
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
// Игрок ест торт
        if (player.canEat(false)) {

            // достаем соответствующую свечу
            Block candleBlock = null;
            for (Map.Entry<Block, CandleLemonadeCake> entry : BY_CANDLE.entrySet()) {
                if (entry.getValue() == this) {
                    candleBlock = entry.getKey();
                    break;
                }
            }

            if (candleBlock != null) {
                popResource(level, pos, new ItemStack(candleBlock));
            }

            BlockState cakeState = InitBlocks.LEMONADE_CAKE.get().defaultBlockState();
            level.setBlock(pos, cakeState, 3);

            return LemonadeCakeBlock.consumeSlice(level, pos, cakeState, player);
        }
        // Если нажали на свечу и она горит — потушить
        if (isClickOnCandle(hit) && state.getValue(LIT)) {
            extinguish(player, state, level, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Если игрок ест торт — удалить свечу и сбросить предмет свечи
        BlockState cakeNoCandle = InitBlocks.LEMONADE_CAKE.get().defaultBlockState();
        InteractionResult result = LemonadeCakeBlock.consumeSlice(level, pos, cakeNoCandle, player);

        if (result.consumesAction()) {
            dropResources(state.setValue(LIT, false), level, pos); // Сбросить свечу
        }

        return result;
    }


    private boolean isClickOnCandle(BlockHitResult hit) {
        return hit.getLocation().y - hit.getBlockPos().getY() > 0.5;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return new ItemStack(InitBlocks.LEMONADE_CAKE.get());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (dir == Direction.DOWN && !canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, dir, neighborState, level, pos, neighborPos);
    }
    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);

        if (!level.isClientSide && !player.isCreative()) {
            // Выпадение свечи
            Block candleBlock = null;
            for (Map.Entry<Block, CandleLemonadeCake> entry : BY_CANDLE.entrySet()) {
                if (entry.getValue() == this) {
                    candleBlock = entry.getKey();
                    break;
                }
            }

            if (candleBlock != null) {
                popResource(level, pos, new ItemStack(candleBlock));
            }
        }
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return 2; // Небольшой сигнал пока свеча стоит на торте
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world,
                                  BlockPos pos, PathComputationType type) {
        return false;
    }

    public static BlockState byCandle(Block candle) {
        return BY_CANDLE.get(candle).defaultBlockState();
    }

    public static boolean canLight(BlockState state) {
        return state.hasProperty(LIT) && !state.getValue(LIT);
    }
}
