package ru.sanberdir.lesson_1_20_1_forge.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
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

    public static final RegistryObject<Item> USUAL_SAPLING = ITEMS.register("usual_sapling",
            () -> new FuelItemBlock(InitBlocks.USUAL_SAPLING.get(), new Item.Properties(), 450));

    public static final RegistryObject<Item> USUAL_LEAVES = ITEMS.register("usual_leaves",
            () -> new ItemNameBlockItem(InitBlocks.USUAL_LEAVES.get(), new Item.Properties()));

    public static final RegistryObject<Item> USUAL_BOAT = ITEMS.register("usual_boat",
            () -> new ModBoatItem(false, ModBoatEntityUsual.Type.USUAL, new Item.Properties()));

    public static final RegistryObject<Item> USUAL_CHEST_BOAT = ITEMS.register("usual_chest_boat",
            () -> new ModBoatItem(true, ModBoatEntityUsual.Type.USUAL, new Item.Properties()));
    
    public static final RegistryObject<Item> CALCINE = ITEMS.register("calcine",
            () -> new FlameItem(new Item.Properties().food((new FoodProperties.Builder()).nutrition(6).saturationMod(0.5F)
                    // meet - можно кормить собак,   alwaysEat -  можно есть всегда, fast - есться быстро
                    .meat().alwaysEat().fast()
                    .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 120, 0), 1F)
                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 2), 0.1F)
                    .build())));
}