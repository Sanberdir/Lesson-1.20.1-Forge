package ru.sanberdir.lesson_1_20_1_forge.entity.client;


import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.entity.custom.Chomper;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ChomperRenderer extends GeoEntityRenderer<Chomper> {

    public ChomperRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ChomperModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(Chomper instance) {
        return new ResourceLocation(LessonForge1201.MODID, "textures/entity/chomper_texture.png");
    }

    @Override
    public RenderType getRenderType(Chomper animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
