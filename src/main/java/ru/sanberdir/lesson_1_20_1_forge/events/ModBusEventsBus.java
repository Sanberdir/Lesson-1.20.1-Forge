package ru.sanberdir.lesson_1_20_1_forge.events;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.entity.ModEntities;
import ru.sanberdir.lesson_1_20_1_forge.entity.custom.Chomper;
import ru.sanberdir.lesson_1_20_1_forge.entity.custom.RhinoEntity;

@Mod.EventBusSubscriber(modid = LessonForge1201.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEventsBus {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.RHINO.get(), RhinoEntity.createAttributes().build());
        event.put(ModEntities.CHOMPER.get(), Chomper.setAttributesChomper());
    }
}