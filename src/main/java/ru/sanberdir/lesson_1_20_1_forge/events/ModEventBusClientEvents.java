package ru.sanberdir.lesson_1_20_1_forge.events;

import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.blocks.entity.ModBlockEntities;
import ru.sanberdir.lesson_1_20_1_forge.blocks.renderer.GemPolishingBlockEntityRenderer;
import ru.sanberdir.lesson_1_20_1_forge.entity.client.ModModelLayers;
import ru.sanberdir.lesson_1_20_1_forge.entity.client.RhinoModel;

@Mod.EventBusSubscriber(modid = LessonForge1201.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.RHINO_LAYER, RhinoModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.USUAL_SIGN.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.USUAL_HANGING_SIGN.get(), HangingSignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.GEM_POLISHING_BE.get(), GemPolishingBlockEntityRenderer::new);

    }
}