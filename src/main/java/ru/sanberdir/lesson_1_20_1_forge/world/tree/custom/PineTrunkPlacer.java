package ru.sanberdir.lesson_1_20_1_forge.world.tree.custom;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import ru.sanberdir.lesson_1_20_1_forge.world.tree.ModTrunkPlacerTypes;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Кастомный TrunkPlacer для сосны.
 * Создает ствол дерева с горизонтальными ветвями в случайных направлениях.
 */
public class PineTrunkPlacer extends TrunkPlacer {
    // Кодек для сериализации/десериализации конфигурации
    public static final Codec<PineTrunkPlacer> CODEC = RecordCodecBuilder.create(pineTrunkPlacerInstance ->
            trunkPlacerParts(pineTrunkPlacerInstance).apply(pineTrunkPlacerInstance, PineTrunkPlacer::new));

    /**
     * Конструктор.
     * @param pBaseHeight базовая высота ствола
     * @param pHeightRandA случайная вариация высоты A
     * @param pHeightRandB случайная вариация высоты B
     */
    public PineTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    /**
     * Возвращает тип этого TrunkPlacer для регистрации.
     */
    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.PINE_TRUNK_PLACER.get();
    }

    /**
     * Основной метод размещения ствола и ветвей дерева.
     *
     * @param pLevel уровень для чтения
     * @param pBlockSetter установщик блоков
     * @param pRandom источник случайности
     * @param pFreeTreeHeight свободная высота дерева (без учета листвы)
     * @param pPos позиция основания дерева
     * @param pConfig конфигурация дерева
     * @return список точек для размещения листвы
     */
    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter,
                                                            RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
        // Устанавливаем блок земли под деревом
        setDirtAt(pLevel, pBlockSetter, pRandom, pPos.below(), pConfig);

        // Рассчитываем общую высоту дерева с добавлением случайности
        int height = pFreeTreeHeight + pRandom.nextInt(heightRandA, heightRandA + 3) + pRandom.nextInt(heightRandB - 1, heightRandB + 1);

        // Строим основной ствол
        for (int i = 0; i < height; i++) {
            // Размещаем блок ствола на текущей высоте
            placeLog(pLevel, pBlockSetter, pRandom, pPos.above(i), pConfig);

            // На каждом четном уровне с некоторой вероятностью создаем ветви
            if (i % 2 == 0 && pRandom.nextBoolean()) {

                // Создаем ветви в четырех направлениях (север, юг, восток, запад)
                // Каждая ветвь имеет 75% шанс быть созданной (pRandom.nextFloat() > 0.25f)

                // Ветвь на север (ось Z)
                if (pRandom.nextFloat() > 0.25f) {
                    for (int x = 0; x < 4; x++) {
                        // Получаем состояние блока ствола и устанавливаем правильную ось
                        BlockState branchState = pConfig.trunkProvider.getState(pRandom, pPos)
                                .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
                        // Размещаем блок ветви
                        pBlockSetter.accept(pPos.above(i).relative(Direction.NORTH, x), branchState);
                    }
                }

                // Ветвь на юг (ось Z)
                if (pRandom.nextFloat() > 0.25f) {
                    for (int x = 0; x < 4; x++) {
                        BlockState branchState = pConfig.trunkProvider.getState(pRandom, pPos)
                                .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
                        pBlockSetter.accept(pPos.above(i).relative(Direction.SOUTH, x), branchState);
                    }
                }

                // Ветвь на восток (ось X)
                if (pRandom.nextFloat() > 0.25f) {
                    for (int x = 0; x < 4; x++) {
                        BlockState branchState = pConfig.trunkProvider.getState(pRandom, pPos)
                                .setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
                        pBlockSetter.accept(pPos.above(i).relative(Direction.EAST, x), branchState);
                    }
                }

                // Ветвь на запад (ось X)
                if (pRandom.nextFloat() > 0.25f) {
                    for (int x = 0; x < 4; x++) {
                        BlockState branchState = pConfig.trunkProvider.getState(pRandom, pPos)
                                .setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
                        pBlockSetter.accept(pPos.above(i).relative(Direction.WEST, x), branchState);
                    }
                }
            }
        }

        // Возвращаем позицию для размещения листвы на вершине дерева
        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(pPos.above(height), 0, false));
    }
}