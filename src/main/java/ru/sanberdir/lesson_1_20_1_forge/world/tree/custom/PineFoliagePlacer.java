package ru.sanberdir.lesson_1_20_1_forge.world.tree.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import ru.sanberdir.lesson_1_20_1_forge.world.tree.ModFoliagePlacers;

public class PineFoliagePlacer extends FoliagePlacer {

    // Кодек для чтения/записи конфигурации. Позволяет задавать height в JSON.
    // height ограничен диапазоном 0–16.
    public static final Codec<PineFoliagePlacer> CODEC = RecordCodecBuilder.create(pineFoliagePlacerInstance
            -> foliagePlacerParts(pineFoliagePlacerInstance)
            .and(Codec.intRange(0, 16).fieldOf("height")
                    .forGetter(fp -> fp.height))
            .apply(pineFoliagePlacerInstance, PineFoliagePlacer::new));

    // Высота хвои (количество вертикальных уровней), заданная конфигом.
    private final int height;

    // pRadius — IntProvider радиуса слоёв.
    // pOffset — IntProvider вертикального смещения.
    // height — высота кроны.
    public PineFoliagePlacer(IntProvider pRadius, IntProvider pOffset, int height) {
        super(pRadius, pOffset);
        this.height = height;
    }

    // Тип листвяного генератора, зарегистрированный в моде.
    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacers.PINE_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(
            LevelSimulatedReader pLevel,
            FoliageSetter pBlockSetter,
            RandomSource pRandom,
            TreeConfiguration pConfig,
            int pMaxFreeTreeHeight,
            FoliageAttachment pAttachment,
            int pFoliageHeight,
            int pFoliageRadius,
            int pOffset
    ) {

        // Точка крепления листвы (верх ствола).
        // На её основе создаются 3 слоя хвои.

        // Слой листвы на уровне Attachment + 0.
        // Радиус 2 → диаметр ≈ 5 блоков.
        // Смещение 0 → центр слоя совпадает с точкой Attachment.
        this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig,
                pAttachment.pos().above(0),
                2, 0,
                pAttachment.doubleTrunk());

        // Слой выше на 1 блок.
        this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig,
                pAttachment.pos().above(1),
                2, 0,
                pAttachment.doubleTrunk());

        // Слой выше на 2 блока.
        this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig,
                pAttachment.pos().above(2),
                2, 0,
                pAttachment.doubleTrunk());
    }

    // Возвращает высоту хвои, указанную в конфигурации.
    // Обычно Minecraft выстраивает листья по этой высоте, но здесь
    // вручную создаются 3 слоя, поэтому height используется минимально.
    @Override
    public int foliageHeight(RandomSource pRandom, int pHeight, TreeConfiguration pConfig) {
        return this.height;
    }

    // Отвечает за "пропуски" в листве (дырки, округлые кроны, сужение).
    // Возвращаем false — не пропускаем ни один блок,
    // следовательно слои получаются плотными и цилиндрическими.
    @Override
    protected boolean shouldSkipLocation(RandomSource pRandom,
                                         int pLocalX, int pLocalY, int pLocalZ,
                                         int pRange, boolean pLarge) {
        return false;
    }
}
