package ru.sanberdir.lesson_1_20_1_forge.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.world.biome.ModBiomes;
import ru.sanberdir.lesson_1_20_1_forge.world.features.ModBiomeModifiers;
import ru.sanberdir.lesson_1_20_1_forge.world.features.ModConfiguredFeatures;
import ru.sanberdir.lesson_1_20_1_forge.world.features.ModPlacedFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Провайдер данных для генерации мира мода.
 * Отвечает за регистрацию всех features и модификаторов биомов в датапаке.
 */
public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {

    /**
     * Построитель регистров, который определяет, какие данные будут сгенерированы.
     * Содержит цепочку регистрации всех компонентов генерации мира:
     * 1. Configured Features (настроенные объекты)
     * 2. Placed Features (размещенные объекты)
     * 3. Biome Modifiers (модификаторы биомов)
     */
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            // Регистрация сконфигурированных features (что генерировать)
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            // Регистрация модификаторов биомов (где генерировать)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)
            // Регистрация размещенных features (как генерировать)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(Registries.BIOME, ModBiomes::boostrap);;


    /**
     * Конструктор провайдера данных генерации мира
     * @param output Выходной поток для записи данных
     * @param registries Future-объект с предоставленными регистрами
     */
    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(LessonForge1201.MODID));
    }
}