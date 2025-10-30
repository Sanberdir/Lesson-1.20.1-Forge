package ru.sanberdir.lesson_1_20_1_forge.world.biome;

import net.minecraft.resources.ResourceLocation;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import terrablender.api.Regions;

public class ModTerrablender {
    public static void registerBiomes() {
        Regions.register(new ModOverworldRegion(new ResourceLocation(LessonForge1201.MODID, "overworld"), 5));
    }
}