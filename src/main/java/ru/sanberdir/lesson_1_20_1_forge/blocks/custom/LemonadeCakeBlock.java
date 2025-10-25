package ru.sanberdir.lesson_1_20_1_forge.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LemonadeCakeBlock extends Block {
    public static final int MAX_STAGE = 6;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, MAX_STAGE);

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(1, 0, 1, 15, 8, 15),
            Block.box(3, 0, 1, 15, 8, 15),
            Block.box(5, 0, 1, 15, 8, 15),
            Block.box(7, 0, 1, 15, 8, 15),
            Block.box(9, 0, 1, 15, 8, 15),
            Block.box(11, 0, 1, 15, 8, 15),
            Block.box(13, 0, 1, 15, 8, 15)
    };

    public LemonadeCakeBlock(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(STAGE)];
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {

        ItemStack stack = player.getItemInHand(hand);

        if (tryAddCandle(state, level, pos, player, stack)) {
            return InteractionResult.SUCCESS;
        }

        if (level.isClientSide) {
            if (consumeSlice(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }
            if (stack.isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        return consumeSlice(level, pos, state, player);
    }

    private boolean tryAddCandle(BlockState state, Level level, BlockPos pos,
                                 Player player, ItemStack stack) {
        if (!stack.is(ItemTags.CANDLES)) return false;
        if (state.getValue(STAGE) != 0) return false;

        Block candle = Block.byItem(stack.getItem());
        if (!(candle instanceof CandleBlock)) return false;

        if (!player.isCreative()) {
            stack.shrink(1);
        }

        level.playSound(null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1F, 1F);
        level.setBlockAndUpdate(pos, CandleLemonadeCake.byCandle(candle));
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        return true;
    }

    public static InteractionResult consumeSlice(LevelAccessor level, BlockPos pos,
                                                 BlockState state, Player player) {
        if (!player.canEat(false)) return InteractionResult.PASS;

        player.awardStat(Stats.EAT_CAKE_SLICE);
        player.getFoodData().eat(4, 0.1F);

        int stage = state.getValue(STAGE);

        if (stage < MAX_STAGE) {
            level.setBlock(pos, state.setValue(STAGE, stage + 1), 3);
            level.playSound((Player)null, pos, SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 1.0F, 1.0F);
        } else {
            level.removeBlock(pos, false);
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        }

        level.gameEvent(player, GameEvent.EAT, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return getRedstoneSignal(state.getValue(STAGE));
    }

    public static int getRedstoneSignal(int stage) {
        return (MAX_STAGE + 1 - stage) * 2;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }
}
