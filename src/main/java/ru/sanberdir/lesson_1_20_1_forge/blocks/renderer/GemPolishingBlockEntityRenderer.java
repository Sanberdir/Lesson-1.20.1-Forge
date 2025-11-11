package ru.sanberdir.lesson_1_20_1_forge.blocks.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import ru.sanberdir.lesson_1_20_1_forge.blocks.entity.GemPolishingStationBlockEntity;

/**
 * Рендерер для блочной сущности станции для полировки драгоценностей
 * Отвечает за визуализацию предмета на станции
 */
public class GemPolishingBlockEntityRenderer implements BlockEntityRenderer<GemPolishingStationBlockEntity> {

    /**
     * Конструктор рендерера
     * @param context контекст для предоставления зависимостей
     */
    public GemPolishingBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        // Конструктор может использоваться для получения дополнительных зависимостей,
        // но в данном случае не выполняет никаких действий
    }

    /**
     * Основной метод рендеринга, вызываемый каждый кадр для отрисовки блочной сущности
     * @param pBlockEntity сущность станции для полировки
     * @param pPartialTick частичный тик для интерполяции
     * @param pPoseStack стек матриц для трансформаций
     * @param pBuffer источник буферов для рендеринга
     * @param pPackedLight упакованное значение освещения
     * @param pPackedOverlay упакованное значение оверлея
     */
    @Override
    public void render(GemPolishingStationBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        // Получаем рендерер предметов из Minecraft клиента
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        // Получаем предмет для отрисовки из блочной сущности
        ItemStack itemStack = pBlockEntity.getRenderStack();

        // Сохраняем текущее состояние матрицы
        pPoseStack.pushPose();

        // Трансформации для позиционирования предмета:
        // Перемещаем в центр блока и немного выше поверхности станции
        pPoseStack.translate(0.5f, 0.85f, 0.5f);
        // Уменьшаем размер предмета
        pPoseStack.scale(0.35f, 0.35f, 0.35f);
        // Поворачиваем предмет на 270 градусов по оси X (ложим его горизонтально)
        pPoseStack.mulPose(Axis.XP.rotationDegrees(270));

        // Рендерим предмет с вычисленным освещением
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);

        // Восстанавливаем предыдущее состояние матрицы
        pPoseStack.popPose();
    }

    /**
     * Вспомогательный метод для получения уровня освещения в позиции блока
     * @param level мир, в котором находится блок
     * @param pos позиция блока
     * @return упакованное значение освещения для рендеринга
     */
    private int getLightLevel(Level level, BlockPos pos) {
        // Получаем блочное освещение (от источников света)
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        // Получаем небесное освещение (от неба)
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        // Упаковываем оба значения в одно целое число
        return LightTexture.pack(bLight, sLight);
    }
}