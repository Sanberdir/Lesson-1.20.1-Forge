package ru.sanberdir.lesson_1_20_1_forge.world.wood;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;

public class ModWoodTypes {
    public static final WoodType USUAL = WoodType.register(new WoodType(LessonForge1201.MODID + ":usual", BlockSetType.OAK));
}