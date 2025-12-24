package ru.sanberdir.lesson_1_20_1_forge.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> NEEDS_SAPPHIRE_TOOL = tag("needs_sapphire_tool");
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(LessonForge1201.MODID, name));
        }
    }

    public static class Items {
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(LessonForge1201.MODID, name));
        }
    }
}