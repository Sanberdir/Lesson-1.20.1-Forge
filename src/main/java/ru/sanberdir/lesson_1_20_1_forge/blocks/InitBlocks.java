package ru.sanberdir.lesson_1_20_1_forge.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.blocks.custom.StrippedWoodLogs;
import ru.sanberdir.lesson_1_20_1_forge.items.InitItems;
import ru.sanberdir.lesson_1_20_1_forge.world.tree.UsualTree;

import java.util.function.Supplier;

public class InitBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, LessonForge1201.MODID);


    public static final RegistryObject<Block> DICE_BLOCK = registerBlock("dice_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.AMETHYST).strength(1.5F,10f)));

    public static final RegistryObject<Block> USUAL_PLANKS = registerBlock("usual_planks",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()));

    public static final RegistryObject<Block> USUAL_STAIRS = registerBlock("usual_stairs",
            () -> new StairBlock(USUAL_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()));

    public static final RegistryObject<Block> USUAL_SLAB = registerBlock("usual_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()));

    public static final RegistryObject<Block> USUAL_LOG = registerBlock("usual_log",
            () -> new StrippedWoodLogs(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));
    public static final RegistryObject<Block> USUAL_WOOD = registerBlock("usual_wood",
            () -> new StrippedWoodLogs(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));
    public static final RegistryObject<Block> STRIPPED_USUAL_LOG = registerBlock("stripped_usual_log",
            () -> new StrippedWoodLogs(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));
    public static final RegistryObject<Block> STRIPPED_USUAL_WOOD = registerBlock("stripped_usual_wood",
            () -> new StrippedWoodLogs(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));


    public static final RegistryObject<Block> USUAL_SAPLING = BLOCKS.register("usual_sapling",
            () -> new SaplingBlock(new UsualTree(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> USUAL_LEAVES = BLOCKS.register("usual_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return InitItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
