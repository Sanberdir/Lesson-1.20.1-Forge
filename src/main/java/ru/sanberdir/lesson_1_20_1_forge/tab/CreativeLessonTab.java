package ru.sanberdir.lesson_1_20_1_forge.tab;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.blocks.InitBlocks;
import ru.sanberdir.lesson_1_20_1_forge.items.InitItems;

public class CreativeLessonTab extends CreativeModeTab {

    protected CreativeLessonTab(Builder builder) {
        super(builder);
    }

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LessonForge1201.MODID);

    public static final RegistryObject<CreativeModeTab> LESSON_TAB = CREATIVE_MODE_TABS.register("lesson_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(InitItems.TIGER_STONE.get()))
                    .title(Component.translatable("creative_tab.lesson_1_20_1_forge"))
                    .displayItems((parameters, output) -> {
                        //Blocks
                        output.accept(InitBlocks.DICE_BLOCK.get());
                        output.accept(InitBlocks.GEM_POLISHING_STATION.get());
                        output.accept(InitBlocks.USUAL_PLANKS.get());
                        output.accept(InitBlocks.USUAL_LOG.get());
                        output.accept(InitBlocks.USUAL_WOOD.get());
                        output.accept(InitBlocks.STRIPPED_USUAL_LOG.get());
                        output.accept(InitBlocks.STRIPPED_USUAL_WOOD.get());
                        output.accept(InitBlocks.USUAL_STAIRS.get());
                        output.accept(InitBlocks.USUAL_SLAB.get());
                        output.accept(InitBlocks.USUAL_FENCE.get());
                        output.accept(InitBlocks.USUAL_FENCE_GATE.get());
                        output.accept(InitBlocks.USUAL_PRESSURE_PLATE.get());
                        output.accept(InitBlocks.USUAL_BUTTON.get());
                        output.accept(InitBlocks.LEMONADE_CAKE.get());
                        //Items
                        output.accept(InitItems.SAPPHIRE_AXE.get());
                        output.accept(InitItems.SAPPHIRE_HOE.get());
                        output.accept(InitItems.SAPPHIRE_PICKAXE.get());
                        output.accept(InitItems.SAPPHIRE_SHOVEL.get());
                        output.accept(InitItems.SAPPHIRE_SWORD.get());

                        output.accept(InitItems.SAPPHIRE_HELMET.get());
                        output.accept(InitItems.SAPPHIRE_CHESTPLATE.get());
                        output.accept(InitItems.SAPPHIRE_LEGGINGS.get());
                        output.accept(InitItems.SAPPHIRE_BOOTS.get());

                        output.accept(InitItems.BAR_BRAWL_MUSIC_DISC.get());
                        output.accept(InitItems.SAPPHIRE_STAFF.get());
                        output.accept(InitItems.MOD_BOW.get());
                        output.accept(InitItems.MOD_CROSSBOW.get());
                        output.accept(InitItems.POISONOUS_PURPLE_POTATO.get());
                        output.accept(InitItems.BACKED_PURPLE_POTATO.get());
                        output.accept(InitItems.PURPLE_POTATO.get());
                        output.accept(InitItems.USUAL_SAPLING.get());
                        output.accept(InitItems.USUAL_LEAVES.get());
                        output.accept(InitItems.TIGER_STONE.get());
                        output.accept(InitItems.CALCINE.get());
                        output.accept(InitItems.USUAL_CHEST_BOAT.get());
                        output.accept(InitItems.USUAL_BOAT.get());
                        output.accept(InitItems.USUAL_SIGN.get());
                        output.accept(InitItems.USUAL_HANGING_SIGN.get());

                    })
                    .build());
}