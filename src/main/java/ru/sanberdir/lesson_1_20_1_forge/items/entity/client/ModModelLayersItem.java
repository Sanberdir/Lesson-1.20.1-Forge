package ru.sanberdir.lesson_1_20_1_forge.items.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;

public class ModModelLayersItem {

    public static final ModelLayerLocation USUAL_BOAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(LessonForge1201.MODID, "boat/usual"), "main");
    public static final ModelLayerLocation USUAL_CHEST_BOAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(LessonForge1201.MODID, "chest_boat/usual"), "main");

}