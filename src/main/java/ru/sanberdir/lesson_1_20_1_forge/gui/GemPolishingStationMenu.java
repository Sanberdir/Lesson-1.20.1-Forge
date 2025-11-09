package ru.sanberdir.lesson_1_20_1_forge.gui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import ru.sanberdir.lesson_1_20_1_forge.blocks.InitBlocks;
import ru.sanberdir.lesson_1_20_1_forge.blocks.entity.GemPolishingStationBlockEntity;

public class GemPolishingStationMenu extends AbstractContainerMenu {
    public final GemPolishingStationBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    // Конструктор для клиента (получает данные из сетевого пакета)
    public GemPolishingStationMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    // Основной конструктор
    public GemPolishingStationMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.GEM_POLISHING_MENU.get(), pContainerId);
        checkContainerSize(inv, 2);
        blockEntity = ((GemPolishingStationBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        // Добавляем слоты инвентаря игрока
        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // Добавляем слоты самого станка для полировки камней
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            // Слот для входного предмета (сырого камня)
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 80, 11));
            // Слот для выходного предмета (обработанного камня)
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 80, 59));
        });

        // Регистрируем данные для синхронизации между сервером и клиентом
        addDataSlots(data);
    }

    // Проверяет, идет ли в данный момент процесс крафта
    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    // Вычисляет масштабированный прогресс для отрисовки стрелки
    public int getScaledProgress() {
        int progress = this.data.get(0);        // Текущий прогресс
        int maxProgress = this.data.get(1);     // Максимальный прогресс (общее время крафта)
        int progressArrowSize = 26;             // Высота стрелки прогресса в пикселях

        // Вычисляем высоту отображаемой части стрелки пропорционально прогрессу
        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    // Константы для организации слотов
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // Количество слотов в самом станке для полировки
    private static final int TE_INVENTORY_SLOT_COUNT = 2;  // 2 слота: вход и выход

    // Обработка быстрого перемещения предметов (Shift+клик)
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Если кликнули по слоту из инвентаря игрока
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // Пытаемся переместить в слоты станка
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        }
        // Если кликнули по слоту станка
        else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // Пытаемся переместить в инвентарь игрока
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }

        // Если весь стек был перемещен, очищаем слот
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    // Проверяет, может ли игрок использовать этот интерфейс
    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, InitBlocks.GEM_POLISHING_STATION.get());
    }

    // Добавляет основной инвентарь игрока (27 слотов)
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    // Добавляет хотбар игрока (9 слотов)
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}