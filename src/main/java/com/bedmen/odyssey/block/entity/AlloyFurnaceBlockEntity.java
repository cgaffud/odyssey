package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.inventory.AlloyFurnaceMenu;
import com.bedmen.odyssey.recipes.AlloyRecipe;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class AlloyFurnaceBlockEntity extends OdysseyFurnaceBlockEntity {
    public static final int SLOT_INPUT_0 = 0;
    public static final int SLOT_INPUT_1 = 1;
    public static final int SLOT_FUEL = 2;
    public static final int SLOT_RESULT = 3;
    public static final int SLOT_COUNT = 4;
    private static final int[] SLOTS_FOR_UP = new int[]{SLOT_INPUT_0, SLOT_INPUT_1};
    private static final int[] SLOTS_FOR_DOWN = new int[]{SLOT_FUEL, SLOT_RESULT};
    private static final int[] SLOTS_FOR_SIDES = new int[]{SLOT_FUEL};
    public static final int BURN_COOL_SPEED = 2;

    public AlloyFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.ALLOY_FURNACE.get(), blockPos, blockState, RecipeTypeRegistry.ALLOYING.get());
        items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, AlloyFurnaceBlockEntity alloyFurnaceBlockEntity) {
        boolean flag = alloyFurnaceBlockEntity.isLit();
        boolean flag1 = false;
        if (alloyFurnaceBlockEntity.isLit()) {
            --alloyFurnaceBlockEntity.litTime;
        }

        ItemStack fuelStack = alloyFurnaceBlockEntity.items.get(SLOT_FUEL);
        if (alloyFurnaceBlockEntity.isLit() || !fuelStack.isEmpty() && !alloyFurnaceBlockEntity.items.get(0).isEmpty()) {
            Recipe<?> recipe = level.getRecipeManager().getRecipeFor((RecipeType<AlloyRecipe>)alloyFurnaceBlockEntity.recipeType, alloyFurnaceBlockEntity, level).orElse(null);
            int maxStackSize = alloyFurnaceBlockEntity.getMaxStackSize();
            if (!alloyFurnaceBlockEntity.isLit() && alloyFurnaceBlockEntity.canBurn(recipe, alloyFurnaceBlockEntity.items, maxStackSize)) {
                alloyFurnaceBlockEntity.litTime = alloyFurnaceBlockEntity.getBurnDuration(fuelStack);
                alloyFurnaceBlockEntity.litDuration = alloyFurnaceBlockEntity.litTime;
                if (alloyFurnaceBlockEntity.isLit()) {
                    flag1 = true;
                    if (fuelStack.hasContainerItem())
                        alloyFurnaceBlockEntity.items.set(SLOT_FUEL, fuelStack.getContainerItem());
                    else
                    if (!fuelStack.isEmpty()) {
                        fuelStack.shrink(1);
                        if (fuelStack.isEmpty()) {
                            alloyFurnaceBlockEntity.items.set(SLOT_FUEL, fuelStack.getContainerItem());
                        }
                    }
                }
            }

            if (alloyFurnaceBlockEntity.isLit() && alloyFurnaceBlockEntity.canBurn(recipe, alloyFurnaceBlockEntity.items, maxStackSize)) {
                ++alloyFurnaceBlockEntity.cookingProgress;
                if (alloyFurnaceBlockEntity.cookingProgress == alloyFurnaceBlockEntity.cookingTotalTime) {
                    alloyFurnaceBlockEntity.cookingProgress = 0;
                    alloyFurnaceBlockEntity.cookingTotalTime = getTotalCookTime(level, alloyFurnaceBlockEntity.recipeType, alloyFurnaceBlockEntity);
                    if (alloyFurnaceBlockEntity.burn(recipe, alloyFurnaceBlockEntity.items, maxStackSize)) {
                        alloyFurnaceBlockEntity.setRecipeUsed(recipe);
                    }

                    flag1 = true;
                }
            } else {
                alloyFurnaceBlockEntity.cookingProgress = 0;
            }
        } else if (!alloyFurnaceBlockEntity.isLit() && alloyFurnaceBlockEntity.cookingProgress > 0) {
            alloyFurnaceBlockEntity.cookingProgress = Mth.clamp(alloyFurnaceBlockEntity.cookingProgress - BURN_COOL_SPEED, 0, alloyFurnaceBlockEntity.cookingTotalTime);
        }

        if (flag != alloyFurnaceBlockEntity.isLit()) {
            flag1 = true;
            blockState = blockState.setValue(AbstractFurnaceBlock.LIT, alloyFurnaceBlockEntity.isLit());
            level.setBlock(blockPos, blockState, 3);
        }

        if (flag1) {
            setChanged(level, blockPos, blockState);
        }
    }

    private boolean canBurn(@Nullable Recipe<?> recipe, NonNullList<ItemStack> itemStacks, int maxStackSize) {
        if (!itemStacks.get(SLOT_INPUT_0).isEmpty() && !itemStacks.get(SLOT_INPUT_1).isEmpty() && recipe != null) {
            ItemStack resultStackFromRecipe = ((Recipe<WorldlyContainer>) recipe).assemble(this);
            if (resultStackFromRecipe.isEmpty()) {
                return false;
            } else {
                ItemStack resultStack = itemStacks.get(SLOT_RESULT);
                if (resultStack.isEmpty()) {
                    return true;
                } else if (!resultStack.sameItem(resultStackFromRecipe)) {
                    return false;
                } else if (resultStack.getCount() + resultStackFromRecipe.getCount() <= maxStackSize && resultStack.getCount() + resultStackFromRecipe.getCount() <= resultStack.getMaxStackSize()) {
                    return true;
                } else {
                    return resultStack.getCount() + resultStackFromRecipe.getCount() <= resultStackFromRecipe.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    private boolean burn(@Nullable Recipe<?> recipe, NonNullList<ItemStack> itemStacks, int maxStackSize) {
        if (recipe != null && this.canBurn(recipe, itemStacks, maxStackSize)) {
            ItemStack input0Stack = itemStacks.get(SLOT_INPUT_0);
            ItemStack input1Stack = itemStacks.get(SLOT_INPUT_1);
            ItemStack resultStackFromRecipe = ((Recipe<WorldlyContainer>) recipe).assemble(this);
            ItemStack resultStack = itemStacks.get(SLOT_RESULT);
            if (resultStack.isEmpty()) {
                itemStacks.set(SLOT_RESULT, resultStackFromRecipe.copy());
            } else if (resultStack.is(resultStackFromRecipe.getItem())) {
                resultStack.grow(resultStackFromRecipe.getCount());
            }

            input0Stack.shrink(1);
            input1Stack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int getStandardBurnTime() {
        return 200;
    }

    @Override
    protected boolean isResultSlot(int index) {
        return index == SLOT_RESULT;
    }

    protected int getBurnDuration(ItemStack itemStack) {
        return super.getBurnDuration(itemStack) / 2;
    }

    @Override
    protected boolean isFuelSlot(int index) {
        return index == SLOT_FUEL;
    }

    @Override
    protected int[] getSlotsForSide() {
        return SLOTS_FOR_SIDES;
    }

    @Override
    protected int[] getSlotsForUp() {
        return SLOTS_FOR_UP;
    }

    @Override
    protected int[] getSlotsForDown() {
        return SLOTS_FOR_DOWN;
    }

    protected Component getDefaultName() {
        return new TranslatableComponent("container.oddc.alloy_furnace");
    }

    protected AbstractContainerMenu createMenu(int containerCounter, Inventory inventory) {
        return new AlloyFurnaceMenu(containerCounter, inventory, this, this.dataAccess);
    }
}