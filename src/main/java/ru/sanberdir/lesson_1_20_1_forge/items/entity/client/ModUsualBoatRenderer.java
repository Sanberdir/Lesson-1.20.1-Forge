package ru.sanberdir.lesson_1_20_1_forge.items.entity.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;
import ru.sanberdir.lesson_1_20_1_forge.items.entity.ModBoatEntityUsual;
import ru.sanberdir.lesson_1_20_1_forge.items.entity.ModChestBoatEntityUsual;

import java.util.Map;
import java.util.stream.Stream;

public class ModUsualBoatRenderer extends BoatRenderer {
    private final Map<ModBoatEntityUsual.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public ModUsualBoatRenderer(EntityRendererProvider.Context context, boolean chestBoat) {
        super(context, chestBoat);
        this.boatResources = Stream.of(ModBoatEntityUsual.Type.values()).collect(ImmutableMap.toImmutableMap(type -> type,
                type -> Pair.of(new ResourceLocation(LessonForge1201.MODID, getTextureLocation(type, chestBoat)), this.createBoatModel(context, type, chestBoat))));
    }

    private static String getTextureLocation(ModBoatEntityUsual.Type type, boolean chestBoat) {
        return chestBoat ? "textures/entity/chest_boat/" + type.getName() + ".png" : "textures/entity/boat/" + type.getName() + ".png";
    }

    private ListModel<Boat> createBoatModel(EntityRendererProvider.Context context, ModBoatEntityUsual.Type type, boolean chestBoat) {
        ModelLayerLocation modellayerlocation = chestBoat ? ModUsualBoatRenderer.createChestBoatModelName(type) : ModUsualBoatRenderer.createBoatModelName(type);
        ModelPart modelpart = context.bakeLayer(modellayerlocation);
        return chestBoat ? new ChestBoatModel(modelpart) : new BoatModel(modelpart);
    }

    public static ModelLayerLocation createBoatModelName(ModBoatEntityUsual.Type type) {
        return createLocation("boat/" + type.getName(), "main");
    }

    public static ModelLayerLocation createChestBoatModelName(ModBoatEntityUsual.Type type) {
        return createLocation("chest_boat/" + type.getName(), "main");
    }

    private static ModelLayerLocation createLocation(String path, String  model) {
        return new ModelLayerLocation(new ResourceLocation(LessonForge1201.MODID, path),  model);
    }

    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        if(boat instanceof ModBoatEntityUsual modBoat) {
            return this.boatResources.get(modBoat.getModVariant());
        } else if(boat instanceof ModChestBoatEntityUsual modChestBoatEntity) {
            return this.boatResources.get(modChestBoatEntity.getModVariant());
        } else {
            return null;
        }
    }
}