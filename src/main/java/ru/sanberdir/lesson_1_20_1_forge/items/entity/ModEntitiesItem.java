package ru.sanberdir.lesson_1_20_1_forge.items.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;

public class ModEntitiesItem {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LessonForge1201.MODID);

    public static final RegistryObject<EntityType<ModBoatEntityUsual>> MOD_BOAT_USUAL =
            ENTITIES.register("mod_boat_usual", () -> EntityType.Builder.<ModBoatEntityUsual>of(ModBoatEntityUsual::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("mod_boat_usual"));

    public static final RegistryObject<EntityType<ModChestBoatEntityUsual>> MOD_CHEST_BOAT_USUAL =
            ENTITIES.register("mod_chest_boat_usual", () -> EntityType.Builder.<ModChestBoatEntityUsual>of(ModChestBoatEntityUsual::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("mod_chest_boat_usual"));



}