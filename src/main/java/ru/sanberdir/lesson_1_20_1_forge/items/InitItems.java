package ru.sanberdir.lesson_1_20_1_forge.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.blocks.InitBlocks;
import ru.sanberdir.lesson_1_20_1_forge.items.custom.ModBoatItem;
import ru.sanberdir.lesson_1_20_1_forge.items.entity.ModBoatEntityUsual;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LessonForge1201.MODID);

    public static final RegistryObject<Item> TIGER_STONE = ITEMS.register("tiger_stone",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MOD_BOW = ITEMS.register("mod_bow",
            () -> new BowItem(new Item.Properties().stacksTo(1).durability(5000)));

    public static final RegistryObject<Item> MOD_CROSSBOW = ITEMS.register("mod_crossbow",
            () -> new CrossbowItem(new Item.Properties().stacksTo(1).durability(5000)));

    public static final RegistryObject<Item> USUAL_SAPLING = ITEMS.register("usual_sapling",
            () -> new FuelItemBlock(InitBlocks.USUAL_SAPLING.get(), new Item.Properties(), 450));

    public static final RegistryObject<Item> USUAL_LEAVES = ITEMS.register("usual_leaves",
            () -> new ItemNameBlockItem(InitBlocks.USUAL_LEAVES.get(), new Item.Properties()));


    public static final RegistryObject<Item> PURPLE_POTATO = ITEMS.register("purple_potato",
            () -> new ItemNameBlockItem(InitBlocks.PURPLE_POTATO.get(), new Item.Properties()
                    .food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.3F).build())));

    public static final RegistryObject<Item> BACKED_PURPLE_POTATO = ITEMS.register("backed_purple_potato",
            () -> new Item(new Item.Properties()
                    .food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.8F).fast().build())));

    public static final RegistryObject<Item> POISONOUS_PURPLE_POTATO = ITEMS.register("poisonous_purple_potato",
            () -> new Item(new Item.Properties()
                    .food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.3F)
                            .effect(new MobEffectInstance(MobEffects.POISON, 100,0),1F).build())));


    public static final RegistryObject<Item> USUAL_BOAT = ITEMS.register("usual_boat",
            () -> new ModBoatItem(false, ModBoatEntityUsual.Type.USUAL, new Item.Properties()));

    public static final RegistryObject<Item> USUAL_CHEST_BOAT = ITEMS.register("usual_chest_boat",
            () -> new ModBoatItem(true, ModBoatEntityUsual.Type.USUAL, new Item.Properties()));

    public static final RegistryObject<Item> USUAL_SIGN = ITEMS.register("usual_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), InitBlocks.USUAL_SIGN.get(), InitBlocks.USUAL_WALL_SIGN.get()));

    public static final RegistryObject<Item> USUAL_HANGING_SIGN = ITEMS.register("usual_hanging_sign",
            () -> new HangingSignItem(InitBlocks.USUAL_HANGING_SIGN.get(), InitBlocks.USUAL_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> CALCINE = ITEMS.register("calcine",
            () -> new FlameItem(new Item.Properties().food((new FoodProperties.Builder()).nutrition(6).saturationMod(0.5F)
                    // meet - можно кормить собак,   alwaysEat -  можно есть всегда, fast - есться быстро
                    .meat().alwaysEat().fast()
                    .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 120, 0), 1F)
                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 2), 0.1F)
                    .build())));
}