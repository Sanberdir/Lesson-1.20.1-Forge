package ru.sanberdir.lesson_1_20_1_forge.items;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class ModItemProperties {

    /**
     * Основной метод для добавления кастомных свойств предметов
     * Вызывается при инициализации клиентской части мода
     */
    public static void addCustomItemProperties() {
        // Добавляем свойства для лука из мода
        makeBow(InitItems.MOD_BOW.get());

        // Добавляем свойства для арбалета из мода
        makeCrossbow(InitItems.MOD_CROSSBOW.get());
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

    /**
     * Метод для настройки свойств отрисовки арбалета
     * @param item - предмет арбалета, для которого настраиваются свойства
     */
    private static void makeCrossbow(Item item) {
        // Регистрируем свойство "pull" - определяет степень натяжения арбалета (от 0.0 до 1.0)
        ItemProperties.register(item, new ResourceLocation("pull"),
                (itemStack, clientLevel, livingEntity, seed) -> {
                    if (livingEntity == null) {
                        return 0.0F;
                    } else {
                        // Проверяем, что entity использует именно этот арбалет
                        if (livingEntity.getUseItem() != itemStack) {
                            return 0.0F;
                        }

                        // Вычисляем прогресс натяжения арбалета
                        int useTicks = itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks();
                        float progress = (float) useTicks / (float) getChargeDuration(itemStack);
                        return progress > 1.0F ? 1.0F : progress;
                    }
                });

        // Регистрируем свойство "pulling" - определяет, натягивается ли арбалет в данный момент (0 или 1)
        ItemProperties.register(item, new ResourceLocation("pulling"),
                (itemStack, clientLevel, livingEntity, seed) -> {
                    return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
                });

        // Регистрируем свойство "charged" - определяет, заряжен ли арбалет (0 или 1)
        ItemProperties.register(item, new ResourceLocation("charged"),
                (itemStack, clientLevel, livingEntity, seed) -> {
                    return livingEntity != null && isCharged(itemStack) ? 1.0F : 0.0F;
                });

        // Регистрируем свойство "firework" - определяет, заряжен ли арбалет фейерверком (0 или 1)
        ItemProperties.register(item, new ResourceLocation("firework"),
                (itemStack, clientLevel, livingEntity, seed) -> {
                    return livingEntity != null && isCharged(itemStack) && containsFireworkRocket(itemStack) ? 1.0F : 0.0F;
                });
    }

    /**
     * Вспомогательный метод для определения времени зарядки арбалета
     * @param itemStack - предмет арбалета
     * @return время зарядки в тиках
     */
    private static int getChargeDuration(ItemStack itemStack) {
        // Базовая длительность зарядки арбалета (в тиках)
        // Можно добавить логику для быстрой зарядки с определенными зачарованиями
        int baseChargeTime = 25; // 1.25 секунды (25 тиков)

        // Если есть зачарование "Быстрая зарядка", уменьшаем время
        int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(
                Enchantments.QUICK_CHARGE, itemStack);

        if (quickChargeLevel > 0) {
            baseChargeTime -= 5 * quickChargeLevel; // Уменьшаем на 5 тиков за уровень
        }

        return Math.max(baseChargeTime, 5); // Минимальное время зарядки - 5 тиков
    }


    /**
     * Вспомогательный метод для проверки, заряжен ли арбалет
     * @param itemStack - предмет арбалета
     * @return true если арбалет заряжен
     */
    private static boolean isCharged(ItemStack itemStack) {
        // Проверяем стандартный для Minecraft NBT-тег Charged
        return itemStack.getTag() != null && itemStack.getTag().getBoolean("Charged");
    }

    /**
     * Вспомогательный метод для проверки, содержит ли арбалет фейерверк
     * @param itemStack - предмет арбалета
     * @return true если арбалет заряжен фейерверком
     */
    private static boolean containsFireworkRocket(ItemStack itemStack) {
        // Проверяем, есть ли в NBT арбалета фейерверк
        if (itemStack.getTag() != null && itemStack.getTag().contains("ChargedProjectiles")) {
            ListTag projectiles = itemStack.getTag().getList("ChargedProjectiles", 10); // 10 - тип TAG_Compound
            if (!projectiles.isEmpty()) {
                // Берем первый снаряд и проверяем, является ли он фейерверком
                CompoundTag projectile = projectiles.getCompound(0);
                // Проверяем ID предмета. "minecraft:firework_rocket" - это стандартный ID фейерверка.
                return projectile.contains("id") && projectile.getString("id").equals("minecraft:firework_rocket");
            }
        }
        return false;
    }
}