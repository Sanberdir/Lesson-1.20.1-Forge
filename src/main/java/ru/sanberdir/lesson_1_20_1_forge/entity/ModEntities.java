package ru.sanberdir.lesson_1_20_1_forge.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.entity.custom.Chomper;
import ru.sanberdir.lesson_1_20_1_forge.entity.custom.RhinoEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LessonForge1201.MODID);

    public static final RegistryObject<EntityType<RhinoEntity>> RHINO =
            ENTITY_TYPES.register("rhino", () -> EntityType.Builder.of(RhinoEntity::new, MobCategory.CREATURE)
                    .sized(2.5f, 2.5f).build("rhino"));

    public static final RegistryObject<EntityType<Chomper>> CHOMPER =
            ENTITY_TYPES.register("chomper",
                    () -> EntityType.Builder.of(Chomper::new, MobCategory.MONSTER)
                            .sized(0.4f, 1.5f)
                            .build(new ResourceLocation(LessonForge1201.MODID, "chomper").toString()));
}