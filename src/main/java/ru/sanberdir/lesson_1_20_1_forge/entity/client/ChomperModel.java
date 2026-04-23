package ru.sanberdir.lesson_1_20_1_forge.entity.client;

import net.minecraft.resources.ResourceLocation;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.entity.custom.Chomper;
import software.bernie.geckolib.model.GeoModel;

public class ChomperModel extends GeoModel<Chomper> {
    @Override
    public ResourceLocation getModelResource(Chomper object) {
        return new ResourceLocation(LessonForge1201.MODID, "geo/chomper.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Chomper object) {
        return new ResourceLocation(LessonForge1201.MODID, "textures/entity/chomper_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Chomper animatable) {
        return new ResourceLocation(LessonForge1201.MODID, "animations/chomper.animation.json");
    }
}
