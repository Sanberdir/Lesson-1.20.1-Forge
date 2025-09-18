package ru.sanberdir.lesson_1_20_1_forge.blocks.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.blocks.InitBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LessonForge1201.MODID);

    public static final RegistryObject<BlockEntityType<ModSignBlockEntity>> USUAL_SIGN =
            BLOCK_ENTITIES.register("usual_sign", () ->
                    BlockEntityType.Builder.of(ModSignBlockEntity::new,
                            InitBlocks.USUAL_SIGN.get(), InitBlocks.USUAL_WALL_SIGN.get()).build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntity>> USUAL_HANGING_SIGN =
            BLOCK_ENTITIES.register("usual_hanging_sign", () ->
                    BlockEntityType.Builder.of(ModHangingSignBlockEntity::new,
                            InitBlocks.USUAL_HANGING_SIGN.get(), InitBlocks.USUAL_WALL_HANGING_SIGN.get()).build(null));
}