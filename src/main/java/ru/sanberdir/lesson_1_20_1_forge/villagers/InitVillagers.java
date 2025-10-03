package ru.sanberdir.lesson_1_20_1_forge.villagers;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;


public class InitVillagers {
    // Создаем отложенные регистры для типов POI и профессий жителей
    // DeferredRegister позволяет безопасно регистрировать объекты в нужное время
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, LessonForge1201.MODID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, LessonForge1201.MODID);

    // Регистрируем новый тип точки интереса (Point of Interest) на основе верстака
    // POI - это точки, которые жители используют для определения своих рабочих мест
    public static final RegistryObject<PoiType> FABRICATOR_POI = POI_TYPES.register("fabricator_poi",
            () -> new PoiType(ImmutableSet.copyOf(Blocks.CRAFTING_TABLE.getStateDefinition().getPossibleStates()),
                    50, 1));
    // Параметры:
    // - Все возможные состояния верстака как рабочие блоки
    // - 50: максимальное количество жителей, которые могут использовать эту POI
    // - 1: минимальное количество тиков, которое житель будет проводить на рабочем месте

    // Регистрируем новую профессию жителя - "Мастер-изготовитель"
    public static final RegistryObject<VillagerProfession> FABRICATOR_MASTER =
            VILLAGER_PROFESSIONS.register("fabricator_master", () -> new VillagerProfession("fabricator_master",
                    holder -> holder.get() == FABRICATOR_POI.get(), // Условие поиска рабочего места
                    holder -> holder.get() == FABRICATOR_POI.get(), // Условие поиска дома
                    ImmutableSet.of(), // Набор предметов, которые житель может дать при торговле (пока пустой)
                    ImmutableSet.of(), // Набор блоков, которые житель может использовать (пока пустой)
                    SoundEvents.VILLAGER_WORK_ARMORER)); // Звуки работы (используются звуки оружейника)

    // Метод для регистрации всех объектов в системе событий Forge
    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus); // Регистрируем все POI типы
        VILLAGER_PROFESSIONS.register(eventBus); // Регистрируем все профессии жителей
    }
}