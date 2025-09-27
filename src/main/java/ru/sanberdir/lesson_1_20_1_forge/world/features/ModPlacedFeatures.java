package ru.sanberdir.lesson_1_20_1_forge.world.features;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.blocks.InitBlocks;

import java.util.List;

public class ModPlacedFeatures {
    // Ключи ресурсов для размещенных features (объектов с правилами генерации)
    public static final ResourceKey<PlacedFeature> USUAL_TREE_PLACED_KEY = registerKey("usual_tree_placed");

    public static final ResourceKey<PlacedFeature> EMERALD_ORE_PLACED_KEY = registerKey("emerald_ore_placed");
    public static final ResourceKey<PlacedFeature> NETHER_EMERALD_ORE_PLACED_KEY = registerKey("nether_emerald_ore_placed");
    public static final ResourceKey<PlacedFeature> END_EMERALD_ORE_PLACED_KEY = registerKey("end_emerald_ore_placed");

    /**
     * Метод инициализации размещенных features
     * Вызывается во время загрузки данных мода
     */
    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        // Получаем доступ к реестру сконфигурированных features
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // Регистрация размещения изумрудной руды в Оверворлде
        register(context, EMERALD_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_EMERALD_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12, // 12 жил на чанк
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80)))); // Высота от -64 до 80

        // Регистрация размещения изумрудной руды в Незере
        register(context, NETHER_EMERALD_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.NETHER_EMERALD_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        // Регистрация размещения изумрудной руды в Энде
        register(context, END_EMERALD_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.END_EMERALD_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        // Регистрация размещения пользовательского дерева
        register(context, USUAL_TREE_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.USUAL_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(3, 0.1f, 2), // Базовое количество + случайный шанс
                        InitBlocks.USUAL_SAPLING.get())); // Саженец для генерации
    }

    /**
     * Вспомогательный метод для создания ключей ресурсов размещенных features
     * @param name - имя размещенного feature
     * @return ResourceKey для PlacedFeature
     */
    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE,
                new ResourceLocation(LessonForge1201.MODID, name));
    }

    /**
     * Метод регистрации размещенного feature
     * @param context - контекст загрузки данных
     * @param key - ключ ресурса
     * @param configuration - сконфигурированный feature
     * @param modifiers - модификаторы размещения
     */
    private static void register(BootstapContext<PlacedFeature> context,
                                 ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}