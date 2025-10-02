package ru.sanberdir.lesson_1_20_1_forge.events;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.items.InitItems;

import java.util.List;

/**
 * Основной класс для обработки событий мода
 * Использует систему событий Forge для добавления кастомных торгов
 */
@Mod.EventBusSubscriber(modid = LessonForge1201.MODID)
public class ModBusEventsVillagers {

    /**
     * Обработчик события добавления кастомных торгов деревенским жителям
     * Добавляет специальные сделки для определенных профессий на разных уровнях
     *
     * @param event Событие торговли деревенских жителей
     */
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        // Добавление торгов для ФЕРМЕРА
        if(event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // Уровень 1 фермера: Изумруды -> Ядовитый фиолетовый картофель
            trades.get(1).add((trader, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),          // Цена: 2 изумруда
                    new ItemStack(InitItems.POISONOUS_PURPLE_POTATO.get(), 12), // Товар: 12 ядовитых фиолетовых картофелин
                    15,     // Максимальное использование сделки
                    8,      // Опыт за сделку
                    0.02f)); // Множитель цены

            // Уровень 2 фермера: Изумруды -> Фиолетовый картофель
            trades.get(2).add((trader, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5),          // Цена: 5 изумрудов
                    new ItemStack(InitItems.PURPLE_POTATO.get(), 6), // Товар: 6 фиолетовых картофелин
                    5,      // Максимальное использование сделки
                    9,      // Опыт за сделку
                    0.035f)); // Множитель цены

            // Уровень 3 фермера: Золотые слитки -> Изумруды
            trades.get(3).add((trader, random) -> new MerchantOffer(
                    new ItemStack(Items.GOLD_INGOT, 8),       // Цена: 8 золотых слитков
                    new ItemStack(Items.EMERALD, 2),          // Товар: 2 изумруда
                    2,      // Максимальное использование сделки
                    12,     // Опыт за сделку
                    0.075f)); // Множитель цены
        }

        // Добавление торгов для БИБЛИОТЕКАРЯ
        if(event.getType() == VillagerProfession.LIBRARIAN) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // Создание зачарованной книги с зачаровыванием "Шипы II"
            ItemStack enchantedBook = EnchantedBookItem.createForEnchantment(
                    new EnchantmentInstance(Enchantments.THORNS, 2) // Зачаровывание Шипы II уровня
            );

            // Уровень 1 библиотекаря: Изумруды -> Зачарованная книга
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 32),         // Цена: 32 изумруда
                    enchantedBook,                            // Товар: зачарованная книга с Шипами II
                    2,      // Максимальное использование сделки
                    8,      // Опыт за сделку
                    0.02f)); // Множитель цены
        }
    }

    /**
     * Обработчик события добавления кастомных торгов странствующему торговцу
     * Добавляет сделки в обычные и редкие категории странствующего торговца
     *
     * @param event Событие торговли странствующего торговца
     */
    @SubscribeEvent
    public static void addCustomWanderingTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();  // Обычные сделки
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();        // Редкие сделки

        // Обычная сделка: Изумруды -> Кальцин
        genericTrades.add((trader, random) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 12),             // Цена: 12 изумрудов
                new ItemStack(InitItems.CALCINE.get(), 1),    // Товар: 1 кальцин
                3,      // Максимальное использование сделки
                2,      // Опыт за сделку
                0.2f)); // Множитель цены

        // Редкая сделка: Изумруды -> Тигровый камень
        rareTrades.add((trader, random) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 24),             // Цена: 24 изумруда
                new ItemStack(InitItems.TIGER_STONE.get(), 1), // Товар: 1 тигровый камень
                2,      // Максимальное использование сделки
                12,     // Опыт за сделку
                0.15f)); // Множитель цены
    }
}