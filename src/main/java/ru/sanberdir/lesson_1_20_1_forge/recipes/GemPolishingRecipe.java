package ru.sanberdir.lesson_1_20_1_forge.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import ru.sanberdir.lesson_1_20_1_forge.LessonForge1201;

/**
 * Реализация пользовательского рецепта для полировки драгоценных камней
 * Обрабатывает преобразование одного предмета в другой
 */
public class GemPolishingRecipe implements Recipe<SimpleContainer> {
    // Список ингредиентов (обычно один входной предмет)
    private final NonNullList<Ingredient> inputItems;
    // Результирующий предмет после полировки
    private final ItemStack output;
    // Уникальный идентификатор рецепта
    private final ResourceLocation id;

    public GemPolishingRecipe(NonNullList<Ingredient> inputItems, ItemStack output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
    }

    /**
     * Проверяет, соответствует ли содержимое контейнера рецепту
     * @param pContainer контейнер с предметами (обычно инвентарь станции полировки)
     * @param pLevel уровень мира
     * @return true если рецепт может быть выполнен
     */
    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        // На клиенте не выполняем проверки
        if (pLevel.isClientSide()) {
            return false;
        }

        // Проверяем, соответствует ли предмет в первом слоте требуемому ингредиенту
        return inputItems.get(0).test(pContainer.getItem(0));
    }

    /**
     * Создает результат крафта (вызывается при завершении крафта)
     */
    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy(); // Возвращаем копию чтобы не изменять оригинал
    }

    /**
     * Определяет минимальный размер сетки крафта
     */
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true; // Рецепт может работать в любом размере
    }

    /**
     * Возвращает результат рецепта для отображения в GUI
     */
    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    /**
     * Возвращает уникальный идентификатор рецепта
     */
    @Override
    public ResourceLocation getId() {
        return id;
    }

    /**
     * Возвращает сериализатор для этого типа рецепта
     */
    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    /**
     * Возвращает тип этого рецепта
     */
    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    // 🔧 ВНУТРЕННИЕ КЛАССЫ ДЛЯ РЕГИСТРАЦИИ СИСТЕМЫ РЕЦЕПТОВ 🔧

    /**
     * Тип рецепта для регистрации в системе рецептов Minecraft
     */
    public static class Type implements RecipeType<GemPolishingRecipe> {
        // Единственный экземпляр типа рецепта
        public static final Type INSTANCE = new Type();
        // Строковый идентификатор типа
        public static final String ID = "gem_polishing";
    }

    /**
     * Сериализатор для преобразования между JSON/сетью и объектом рецепта
     */
    public static class Serializer implements RecipeSerializer<GemPolishingRecipe> {
        // Единственный экземпляр сериализатора
        public static final Serializer INSTANCE = new Serializer();
        // Уникальный идентификатор сериализатора
        public static final ResourceLocation ID = new ResourceLocation(LessonForge1201.MODID, "gem_polishing");

        /**
         * Создание рецепта из JSON (загрузка из datapack)
         */
        @Override
        public GemPolishingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            // Чтение результата из JSON
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            // Чтение массива ингредиентов
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            // Создание списка ингредиентов (фиксированный размер 1 для этого рецепта)
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            // Заполнение списка ингредиентов
            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new GemPolishingRecipe(inputs, output, pRecipeId);
        }

        /**
         * Создание рецепта из сетевого пакета (синхронизация клиент-сервер)
         */
        @Override
        public @Nullable GemPolishingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            // Чтение количества ингредиентов
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            // Чтение каждого ингредиента из буфера
            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            // Чтение результата
            ItemStack output = pBuffer.readItem();
            return new GemPolishingRecipe(inputs, output, pRecipeId);
        }

        /**
         * Запись рецепта в сетевой пакет (синхронизация сервер-клиент)
         */
        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, GemPolishingRecipe pRecipe) {
            // Запись количества ингредиентов
            pBuffer.writeInt(pRecipe.inputItems.size());

            // Запись каждого ингредиента
            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            // Запись результата
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}