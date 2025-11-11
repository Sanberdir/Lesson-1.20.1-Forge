package ru.sanberdir.lesson_1_20_1_forge.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.sanberdir.lesson_1_20_1_forge.gui.GemPolishingStationMenu;
import ru.sanberdir.lesson_1_20_1_forge.recipes.GemPolishingRecipe;

import java.util.Optional;

/**
 * BlockEntity для станции полировки драгоценных камней
 * Отвечает за логику крафта, хранение предметов и взаимодействие с GUI
 */
public class GemPolishingStationBlockEntity extends BlockEntity implements MenuProvider {

    // Обработчик слотов для хранения предметов (2 слота: вход и выход)
    private final ItemStackHandler itemHandler = new ItemStackHandler(2);

    // Константы для идентификации слотов
    private static final int INPUT_SLOT = 0;  // Слот для входного предмета
    private static final int OUTPUT_SLOT = 1; // Слот для результата крафта

    // Lazy-оптимизация для доступа к обработчику предметов
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    // Система данных для синхронизации между сервером и клиентом
    protected final ContainerData data;

    // Переменные прогресса крафта
    private int progress = 0;      // Текущий прогресс
    private int maxProgress = 78;  // Максимальный прогресс (тики)

    public GemPolishingStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GEM_POLISHING_BE.get(), pPos, pBlockState);

        // Инициализация системы данных для синхронизации с GUI
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> GemPolishingStationBlockEntity.this.progress;   // Текущий прогресс
                    case 1 -> GemPolishingStationBlockEntity.this.maxProgress; // Максимальный прогресс
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> GemPolishingStationBlockEntity.this.progress = pValue;
                    case 1 -> GemPolishingStationBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2; // Количество синхронизируемых значений
            }
        };
    }
    /**
     * Рендер предмета голограммы
     */
    public ItemStack getRenderStack() {
        if (itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty()) {
            return itemHandler.getStackInSlot(INPUT_SLOT);
        } else {
            return itemHandler.getStackInSlot(OUTPUT_SLOT);
        }
    }

    /**
     * Предоставление capability для взаимодействия с другими модами/системами
     */
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    /**
     * Инициализация lazy-оптимизации при загрузке entity
     */
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    /**
     * Очистка capability при уничтожении entity
     */
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    /**
     * Выбрасывание предметов при разрушении блока
     */
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    /**
     * Получение отображаемого имени для GUI
     */
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.tutorialmod.gem_polishing_station");
    }

    /**
     * Создание меню для GUI
     */
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new GemPolishingStationMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    /**
     * Сохранение данных в NBT
     */
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("gem_polishing_station.progress", progress);
        super.saveAdditional(pTag);
    }

    /**
     * Загрузка данных из NBT
     */
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("gem_polishing_station.progress");
    }

    /**
     * Основной метод обновления логики (вызывается каждый тик)
     */
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(hasRecipe()) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if(hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    /**
     * Сброс прогресса крафта
     */
    private void resetProgress() {
        progress = 0;
    }

    /**
     * Выполнение крафта - извлечение входного предмета и добавление результата
     */
    private void craftItem() {
        Optional<GemPolishingRecipe> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().getResultItem(null);

        // Извлечение предмета из входного слота
        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        // Добавление результата в выходной слот
        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    /**
     * Проверка наличия доступного рецепта
     */
    private boolean hasRecipe() {
        Optional<GemPolishingRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }
        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());

        // Проверка возможности вставки предмета в выходной слот
        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    /**
     * Получение текущего рецепта на основе предметов в инвентаре
     */
    private Optional<GemPolishingRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(GemPolishingRecipe.Type.INSTANCE, inventory, level);
    }

    /**
     * Проверка возможности вставки предмета в выходной слот
     */
    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    /**
     * Проверка возможности вставки количества предметов в выходной слот
     */
    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <=
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    /**
     * Проверка завершения прогресса крафта
     */
    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    /**
     * Увеличение прогресса крафта
     */
    private void increaseCraftingProgress() {
        progress++;
    }
}