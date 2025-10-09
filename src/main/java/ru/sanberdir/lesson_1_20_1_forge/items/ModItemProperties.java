package ru.sanberdir.lesson_1_20_1_forge.items;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItemProperties {

    /**
     * Основной метод для добавления кастомных свойств предметов
     * Вызывается при инициализации клиентской части мода
     */
    public static void addCustomItemProperties() {
        // Добавляем свойства для лука из мода
        makeBow(InitItems.MOD_BOW.get());
    }

    /**
     * Метод для настройки свойств отрисовки лука
     * @param item - предмет лука, для которого настраиваются свойства
     */
    private static void makeBow(Item item) {
        // Регистрируем свойство "pull" - определяет степень натяжения лука (от 0.0 до 1.0)
        ItemProperties.register(item, new ResourceLocation("pull"),
                (itemStack, clientLevel, livingEntity, seed) -> {
                    // Если entity не существует, возвращаем 0 (лук не натянут)
                    if (livingEntity == null) {
                        return 0.0F;
                    } else {
                        // Проверяем, что entity использует именно этот лук
                        // Если используется другой предмет, возвращаем 0
                        return livingEntity.getUseItem() != itemStack ? 0.0F
                                // Вычисляем прогресс натяжения: (общее время использования - оставшееся время) / 20.0
                                // Делим на 20, так как время в тиках (20 тиков = 1 секунда)
                                : (float)(itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
                    }
                });

        // Регистрируем свойство "pulling" - определяет, натягивается ли лук в данный момент (0 или 1)
        ItemProperties.register(item, new ResourceLocation("pulling"),
                (itemStack, clientLevel, livingEntity, seed) -> {
                    // Возвращаем 1.0 если entity существует, использует предмет и это именно наш лук
                    // В противном случае возвращаем 0.0
                    return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
                });
    }
}