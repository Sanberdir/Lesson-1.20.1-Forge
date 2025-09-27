package ru.sanberdir.lesson_1_20_1_forge.world.features;

import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModOrePlacement {

    /**
     * Базовый метод для размещения руды с использованием двух модификаторов размещения
     * @param placementModifier Первый модификатор (обычно счетчик или редкость)
     * @param placementModifier2 Второй модификатор (обычно диапазон высот)
     * @return Список модификаторов размещения для руды
     */
    public static List<PlacementModifier> orePlacement(PlacementModifier placementModifier, PlacementModifier placementModifier2) {
        return List.of(
                placementModifier,          // Модификатор количества/редкости
                InSquarePlacement.spread(), // Распределение в квадратной области
                placementModifier2,         // Модификатор высоты
                BiomeFilter.biome()         // Фильтр по биомам
        );
    }

    /**
     * Метод для обычного размещения руды с фиксированным количеством жил на чанк
     * @param pCount Количество жил руды на чанк
     * @param pHeightRange Модификатор диапазона высот
     * @return Список модификаторов для обычной руды
     */
    public static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
        return orePlacement(CountPlacement.of(pCount), pHeightRange);
    }

    /**
     * Метод для редкого размещения руды с шансом появления в чанке
     * @param pChance Шанс появления руды (1 из pChance чанков)
     * @param pHeightRange Модификатор диапазона высот
     * @return Список модификаторов для редкой руды
     */
    public static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
    }
}