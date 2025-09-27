package ru.sanberdir.lesson_1_20_1_forge.world.features;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.blocks.InitBlocks;

import java.util.List;
import java.util.OptionalInt;

public class ModConfiguredFeatures {
    // Ключи ресурсов для сконфигурированных features (настроенных объектов генерации)
    public static final ResourceKey<ConfiguredFeature<?, ?>> USUAL_TREE = registerKey("usual_tree");


    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_EMERALD_ORE_KEY = registerKey("emerald_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_EMERALD_ORE_KEY = registerKey("nether_emerald_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> END_EMERALD_ORE_KEY = registerKey("end_emerald_ore");

    /**
     * Метод инициализации сконфигурированных features
     * Вызывается во время загрузки данных мода
     */
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        // Регистрация fancy oak дерева с пользовательскими блоками
        register(context, USUAL_TREE, Feature.TREE, createFancyOak().build());

        // Определение RuleTest'ов для заменяемых блоков в разных измерениях
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES); // Каменные блоки в Оверворлде
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES); // Дипслейт блоки
        RuleTest netherrackReplacables = new BlockMatchTest(Blocks.NETHERRACK); // Незеррак в Незере
        RuleTest endReplaceables = new BlockMatchTest(Blocks.END_STONE); // Эндстоун в Энде

        // Конфигурация для изумрудной руды в Оверворлде (камень и дипслейт)
        List<OreConfiguration.TargetBlockState> overworldSapphireOres = List.of(
                OreConfiguration.target(stoneReplaceable, Blocks.EMERALD_ORE.defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, Blocks.EMERALD_ORE.defaultBlockState()));

        // Регистрация features руды для разных измерений
        register(context, OVERWORLD_EMERALD_ORE_KEY, Feature.ORE, new OreConfiguration(overworldSapphireOres, 9));
        register(context, NETHER_EMERALD_ORE_KEY, Feature.ORE, new OreConfiguration(netherrackReplacables,
                Blocks.EMERALD_ORE.defaultBlockState(), 9));
        register(context, END_EMERALD_ORE_KEY, Feature.ORE, new OreConfiguration(endReplaceables,
                Blocks.EMERALD_ORE.defaultBlockState(), 9));
    }

    /**
     * Создание конфигурации для fancy oak дерева с пользовательскими блоками
     * @return TreeConfiguration.Builder для fancy дерева
     */
    private static TreeConfiguration.TreeConfigurationBuilder createFancyOak() {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(InitBlocks.USUAL_LOG.get()),     // Ствол из пользовательского блока
                new FancyTrunkPlacer(3, 11, 0),                           // Генератор ствола: базовая высота 3, случайная 11
                BlockStateProvider.simple(InitBlocks.USUAL_LEAVES.get()),  // Листья из пользовательского блока
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), // Генератор листвы: радиус 2-4, высота 4
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))      // Размер feature: два слоя, минимальный зазор 4
        ).ignoreVines(); // Игнорировать лианы при генерации
    }

    /**
     * Вспомогательный метод для создания ключей ресурсов сконфигурированных features
     * @param name - имя feature
     * @return ResourceKey для ConfiguredFeature
     */
    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                new ResourceLocation(LessonForge1201.MODID, name));
    }

    /**
     * Универсальный метод регистрации сконфигурированных features
     * @param context - контекст загрузки данных
     * @param key - ключ ресурса
     * @param feature - тип feature (дерево, руда и т.д.)
     * @param configuration - конфигурация feature
     */
    private static <FC extends FeatureConfiguration, F extends Feature<FC>>
    void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                  ResourceKey<ConfiguredFeature<?, ?>> key,
                  F feature,
                  FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}