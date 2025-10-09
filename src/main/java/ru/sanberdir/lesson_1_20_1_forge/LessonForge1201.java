package ru.sanberdir.lesson_1_20_1_forge;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ru.sanberdir.lesson_1_20_1_forge.blocks.InitBlocks;
import ru.sanberdir.lesson_1_20_1_forge.blocks.catch_fire.ModFlammableBlocks;
import ru.sanberdir.lesson_1_20_1_forge.blocks.entity.ModBlockEntities;
import ru.sanberdir.lesson_1_20_1_forge.items.InitItems;
import ru.sanberdir.lesson_1_20_1_forge.items.ModItemProperties;
import ru.sanberdir.lesson_1_20_1_forge.items.entity.ModEntitiesItem;
import ru.sanberdir.lesson_1_20_1_forge.items.entity.client.ModUsualBoatRenderer;
import ru.sanberdir.lesson_1_20_1_forge.items.entity.client.ModModelLayersItem;
import ru.sanberdir.lesson_1_20_1_forge.tab.CreativeLessonTab;
import ru.sanberdir.lesson_1_20_1_forge.villagers.InitVillagers;
import ru.sanberdir.lesson_1_20_1_forge.world.wood.ModWoodTypes;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LessonForge1201.MODID)
public class LessonForge1201 {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "lesson_1_20_1_forge";
    // Directly reference a slf4j logger

    public LessonForge1201() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        ModEntitiesItem.ENTITIES.register(modEventBus);
        // Регистрация класса блоков
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        InitBlocks.BLOCKS.register(modEventBus);
        // Регистрация класса предметов
        InitItems.ITEMS.register(modEventBus);
        // Регистрация класса креатив табов
        CreativeLessonTab.CREATIVE_MODE_TABS.register(modEventBus);
        InitVillagers.register(modEventBus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModFlammableBlocks::registerFlammableBlocks);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(ModModelLayersItem.USUAL_BOAT_LAYER, BoatModel::createBodyModel);
            event.registerLayerDefinition(ModModelLayersItem.USUAL_CHEST_BOAT_LAYER, ChestBoatModel::createBodyModel);
        }
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ModItemProperties.addCustomItemProperties();
            Sheets.addWoodType(ModWoodTypes.USUAL);
            EntityRenderers.register(ModEntitiesItem.MOD_BOAT_USUAL.get(), pContext -> new ModUsualBoatRenderer(pContext, false));
            EntityRenderers.register(ModEntitiesItem.MOD_CHEST_BOAT_USUAL.get(), pContext -> new ModUsualBoatRenderer(pContext, true));
            event.enqueueWork(() -> {
                ComposterBlock.COMPOSTABLES.put(InitItems.USUAL_LEAVES.get(), 0.3f);
                ComposterBlock.COMPOSTABLES.put(InitItems.USUAL_SAPLING.get(), 0.2f);
                ComposterBlock.COMPOSTABLES.put(InitItems.PURPLE_POTATO.get(), 0.2f);
                ComposterBlock.COMPOSTABLES.put(InitItems.POISONOUS_PURPLE_POTATO.get(), 0.2f);
            });

        }
    }
}
