package ru.sanberdir.lesson_1_20_1_forge.world.features;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;

public class ModBiomeModifiers {
    // Ключи ресурсов для модификаторов биомов, которые будут добавлять изумрудную руду в разные измерения
    public static final ResourceKey<BiomeModifier> ADD_EMERALD_ORE = registerKey("add_emerald_ore");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_EMERALD_ORE = registerKey("add_nether_emerald_ore");
    public static final ResourceKey<BiomeModifier> ADD_END_EMERALD_ORE = registerKey("add_end_emerald_ore");

    /**
     * Метод инициализации модификаторов биомов
     * Вызывается во время загрузки данных мода
     */
    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        // Получаем доступ к реестрам размещенных features и биомов
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        // Регистрируем модификатор для добавления изумрудной руды в Оверворлд
        context.register(ADD_EMERALD_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD), // Все биомы Оверворлда
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.EMERALD_ORE_PLACED_KEY)), // Размещенная feature изумрудной руды
                GenerationStep.Decoration.UNDERGROUND_ORES)); // Стадия генерации - подземные руды

        // Регистрируем модификатор для добавления изумрудной руды в Незер
        context.register(ADD_NETHER_EMERALD_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER), // Все биомы Незера
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.NETHER_EMERALD_ORE_PLACED_KEY)), // Незер версия изумрудной руды
                GenerationStep.Decoration.UNDERGROUND_ORES)); // Стадия генерации - подземные руды

        // Регистрируем модификатор для добавления изумрудной руды в Энд
        context.register(ADD_END_EMERALD_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_END), // Все биомы Энда
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.END_EMERALD_ORE_PLACED_KEY)), // Энд версия изумрудной руды
                GenerationStep.Decoration.UNDERGROUND_ORES)); // Стадия генерации - подземные руды
    }

    /**
     * Вспомогательный метод для создания ключей ресурсов модификаторов биомов
     * @param name - имя модификатора
     * @return ResourceKey для BiomeModifier
     */
    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
                new ResourceLocation(LessonForge1201.MODID, name));
    }
}