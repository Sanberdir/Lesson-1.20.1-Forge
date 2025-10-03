package ru.sanberdir.lesson_1_20_1_forge.events;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.items.InitItems;
import ru.sanberdir.lesson_1_20_1_forge.villagers.InitVillagers;

import java.util.List;

/**
 * Класс для добавления кастомных торговых предложений профессии Fabricator Master
 */
@Mod.EventBusSubscriber(modid = LessonForge1201.MODID)
public class ModBusEventsFabricator {

    /**
     * Добавляет торговые предложения для профессии Fabricator Master на разных уровнях
     */
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        // Добавляем сделки только для нашей кастомной профессии Fabricator Master
        if (event.getType() == InitVillagers.FABRICATOR_MASTER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // Уровень 1: Базовые сделки
            int fabricatorLevel = 1;
            trades.get(fabricatorLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5),  // Цена: 5 изумрудов
                    new ItemStack(Items.ACACIA_LEAVES, 15),                           // Товар: 15 листьев акации
                    10,                              // Максимальное количество использований
                    8,                               // Награда опытом
                    0.02F));                         // Множитель спроса

            // Уровень 2: Торговля зачарованными книгами
            fabricatorLevel = 2;

            // Продажа книги с Починкой за 20 изумрудов
            trades.get(fabricatorLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 20),
                    EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Enchantments.MENDING, 1)),

                    5,
                    10,
                    0.05F));

            // Покупка книги с Удачей III за 10 изумрудов
            trades.get(fabricatorLevel).add((trader, rand) -> new MerchantOffer(
                    EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Enchantments.BLOCK_FORTUNE, 3)),
                    new ItemStack(Items.EMERALD, 10),
                    10, 10, 0.02F));

            // Уровень 3: Торговля зельями
            fabricatorLevel = 3;

            // Продажа зелья силы в разных формах
            trades.get(fabricatorLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 7),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH),
                    6, 10, 0.05F));

            trades.get(fabricatorLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 7),
                    PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), Potions.STRENGTH),
                    6, 10, 0.05F));
            fabricatorLevel = 4;
            trades.get(fabricatorLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 7),
                    PotionUtils.setPotion(new ItemStack(Items.TIPPED_ARROW), Potions.STRENGTH),
                    6, 10, 0.05F));

            // Покупка зелий исцеления в обмен на книги
            trades.get(fabricatorLevel).add((trader, rand) -> new MerchantOffer(
                    PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HEALING),
                    new ItemStack(Items.BOOK, 4),
                    8, 10, 0));
            fabricatorLevel = 5;
            trades.get(fabricatorLevel).add((trader, rand) -> new MerchantOffer(
                    PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRONG_HEALING),
                    new ItemStack(Items.BOOK, 4),
                    8, 10, 0));

            trades.get(fabricatorLevel).add((trader, rand) -> new MerchantOffer(
                    PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), Potions.STRONG_HEALING),
                    new ItemStack(Items.BOOK, 4),
                    8, 10, 0));
        }
    }
}