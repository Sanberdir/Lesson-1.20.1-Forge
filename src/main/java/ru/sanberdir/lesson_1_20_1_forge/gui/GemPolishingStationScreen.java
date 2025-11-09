package ru.sanberdir.lesson_1_20_1_forge.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;

public class GemPolishingStationScreen extends AbstractContainerScreen<GemPolishingStationMenu> {
    // Путь к текстуре GUI
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(LessonForge1201.MODID, "textures/gui/gem_station/gem_polishing_station_gui.png");

    public GemPolishingStationScreen(GemPolishingStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        // Скрываем стандартные тексты инвентаря и заголовка, устанавливая их за пределы экрана
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        // Настройка шейдера для отрисовки
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        // Вычисление позиции для центрирования GUI на экране
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Отрисовка основного фона GUI
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Отрисовка стрелки прогресса крафта
        renderProgressArrow(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        // Проверяем, идет ли процесс крафта в данный момент
        if(menu.isCrafting()) {
            // Отрисовываем прогресс-бар (стрелку)
            // x + 85, y + 30 - координаты стрелки в GUI
            // 176, 0 - координаты текстуры стрелки в файле текстур
            // 8 - ширина стрелки, menu.getScaledProgress() - высота (меняется в зависимости от прогресса)
            guiGraphics.blit(TEXTURE, x + 85, y + 30, 176, 0, 8, menu.getScaledProgress());
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        // Отрисовка фона, основного содержимого и тултипов
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}