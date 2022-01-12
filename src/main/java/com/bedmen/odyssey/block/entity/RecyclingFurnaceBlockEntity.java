package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.inventory.RecyclingFurnaceMenu;
import com.bedmen.odyssey.recipes.OdysseyRecipeType;
import com.bedmen.odyssey.recipes.RecyclingRecipe;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class RecyclingFurnaceBlockEntity extends OdysseyFurnaceBlockEntity {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_FUEL = 1;
    public static final int[][] SLOT_RESULTS = new int[][]{{2,3,4},{5,6,7},{8,9,10}};
    public static final int NUM_ROWS = SLOT_RESULTS.length;
    public static final int NUM_COLUMNS = SLOT_RESULTS[0].length;
    public static final int SLOT_COUNT = 11;
    private static final int[] SLOTS_FOR_UP = new int[]{SLOT_INPUT};
    private static final int[] SLOTS_FOR_DOWN = new int[]{
            SLOT_FUEL,
            SLOT_RESULTS[0][0],
            SLOT_RESULTS[0][1],
            SLOT_RESULTS[0][2],
            SLOT_RESULTS[1][0],
            SLOT_RESULTS[1][1],
            SLOT_RESULTS[1][2],
            SLOT_RESULTS[2][0],
            SLOT_RESULTS[2][1],
            SLOT_RESULTS[2][2]};
    private static final int[] SLOTS_FOR_SIDES = new int[]{SLOT_FUEL};
    public static final int BURN_COOL_SPEED = 2;
    public static final int[] COLUMN_WEIGHTS = new int[]{1,9,81};

    public RecyclingFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.RECYCLING_FURNACE.get(), blockPos, blockState, OdysseyRecipeType.RECYCLING);
        this.items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, RecyclingFurnaceBlockEntity recyclingFurnaceBlockEntity) {
        boolean flag = recyclingFurnaceBlockEntity.isLit();
        boolean flag1 = false;
        if (recyclingFurnaceBlockEntity.isLit()) {
            --recyclingFurnaceBlockEntity.litTime;
        }

        ItemStack fuelStack = recyclingFurnaceBlockEntity.items.get(SLOT_FUEL);
        if (recyclingFurnaceBlockEntity.isLit() || !fuelStack.isEmpty() && !recyclingFurnaceBlockEntity.items.get(0).isEmpty()) {
            Recipe<?> recipe = level.getRecipeManager().getRecipeFor((RecipeType<RecyclingRecipe>)recyclingFurnaceBlockEntity.recipeType, recyclingFurnaceBlockEntity, level).orElse(null);
            if (!recyclingFurnaceBlockEntity.isLit() && recyclingFurnaceBlockEntity.canBurn(recipe, recyclingFurnaceBlockEntity.items, null)) {
                recyclingFurnaceBlockEntity.litTime = recyclingFurnaceBlockEntity.getBurnDuration(fuelStack);
                recyclingFurnaceBlockEntity.litDuration = recyclingFurnaceBlockEntity.litTime;
                if (recyclingFurnaceBlockEntity.isLit()) {
                    flag1 = true;
                    if (fuelStack.hasContainerItem())
                        recyclingFurnaceBlockEntity.items.set(SLOT_FUEL, fuelStack.getContainerItem());
                    else
                    if (!fuelStack.isEmpty()) {
                        fuelStack.shrink(1);
                        if (fuelStack.isEmpty()) {
                            recyclingFurnaceBlockEntity.items.set(SLOT_FUEL, fuelStack.getContainerItem());
                        }
                    }
                }
            }

            if (recyclingFurnaceBlockEntity.isLit() && recyclingFurnaceBlockEntity.canBurn(recipe, recyclingFurnaceBlockEntity.items, null)) {
                ++recyclingFurnaceBlockEntity.cookingProgress;
                if (recyclingFurnaceBlockEntity.cookingProgress == recyclingFurnaceBlockEntity.cookingTotalTime) {
                    recyclingFurnaceBlockEntity.cookingProgress = 0;
                    recyclingFurnaceBlockEntity.cookingTotalTime = getTotalCookTime(level, recyclingFurnaceBlockEntity.recipeType, recyclingFurnaceBlockEntity);
                    if (recyclingFurnaceBlockEntity.burn(recipe, recyclingFurnaceBlockEntity.items)) {
                        recyclingFurnaceBlockEntity.setRecipeUsed(recipe);
                    }

                    flag1 = true;
                }
            } else {
                recyclingFurnaceBlockEntity.cookingProgress = 0;
            }
        } else if (!recyclingFurnaceBlockEntity.isLit() && recyclingFurnaceBlockEntity.cookingProgress > 0) {
            recyclingFurnaceBlockEntity.cookingProgress = Mth.clamp(recyclingFurnaceBlockEntity.cookingProgress - BURN_COOL_SPEED, 0, recyclingFurnaceBlockEntity.cookingTotalTime);
        }

        if (flag != recyclingFurnaceBlockEntity.isLit()) {
            flag1 = true;
            blockState = blockState.setValue(AbstractFurnaceBlock.LIT, recyclingFurnaceBlockEntity.isLit());
            level.setBlock(blockPos, blockState, 3);
        }

        if (flag1) {
            setChanged(level, blockPos, blockState);
        }

    }

    private boolean canBurn(@Nullable Recipe<?> recipe, NonNullList<ItemStack> itemStacks, @Nullable Map<RecyclingRecipe.Metal, Integer> rowAssignments) {
        if (!itemStacks.get(SLOT_INPUT).isEmpty() && recipe instanceof RecyclingRecipe recyclingRecipe) {
            Map<RecyclingRecipe.Metal, Integer> metalCounts = recyclingRecipe.getMetalCounts();
            boolean[] rowTaken = new boolean[]{false,false,false};
            for(Map.Entry<RecyclingRecipe.Metal, Integer> entry : metalCounts.entrySet()){
                RecyclingRecipe.Metal metal = entry.getKey();
                int count = entry.getValue();
                boolean tookRow = false;
                for(int row = 0; row < NUM_ROWS; row++){
                    if(!rowTaken[row] && canBeTaken(row, count, metal)){
                        rowTaken[row] = true;
                        tookRow = true;
                        if(rowAssignments != null){
                            rowAssignments.put(metal, row);
                        }
                        break;
                    }
                }
                if(!tookRow){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean canBeTaken(int row, int count, RecyclingRecipe.Metal metal){
        for(int col = 0; col < NUM_COLUMNS; col++){
            if(!emptyOrMatchMetal(row, col, metal)){
                return false;
            }
        }
        return getRowCount(row) + count <= getMaxRowCount(metal);
    }

    private boolean emptyOrMatchMetal(int row, int col, RecyclingRecipe.Metal metal){
        ItemStack itemStack = this.getItem(SLOT_RESULTS[row][col]);
        if(itemStack.isEmpty()){
            return true;
        }
        Item item = metal.getItemFromColumn(col);
        return itemStack.is(item);
    }

    private int getRowCount(int row){
        int total = 0;
        for(int col = 0; col < NUM_COLUMNS; col++){
            ItemStack itemStack = this.getItem(SLOT_RESULTS[row][col]);
            total += itemStack.getCount() * COLUMN_WEIGHTS[col];
        }
        return total;
    }

    private int getMaxRowCount(RecyclingRecipe.Metal metal){
        int total = 0;
        for(int col = 0; col < NUM_COLUMNS; col++){
            ItemStack itemStack = metal.getItemFromColumn(col).getDefaultInstance();
            total += itemStack.getMaxStackSize() * COLUMN_WEIGHTS[col];
        }
        return total;
    }

    private boolean burn(@Nullable Recipe<?> recipe, NonNullList<ItemStack> itemStacks) {
        Map<RecyclingRecipe.Metal, Integer> rowAssigments = new HashMap<>();
        if (recipe instanceof RecyclingRecipe recyclingRecipe && this.canBurn(recipe, itemStacks, rowAssigments)) {
            ItemStack inputStack = itemStacks.get(SLOT_INPUT);
            Map<RecyclingRecipe.Metal, Integer> metalCounts = recyclingRecipe.getMetalCounts();
            for(Map.Entry<RecyclingRecipe.Metal, Integer> entry : metalCounts.entrySet()){
                RecyclingRecipe.Metal metal = entry.getKey();
                int count = entry.getValue();
                if(inputStack.isDamageableItem()){
                    int damage = inputStack.getDamageValue();
                    int maxDamage = inputStack.getMaxDamage();
                    count = Mth.floor(((maxDamage-damage)/(float)maxDamage) * count);
                }
                count = Integer.max(1, count);
                int row = rowAssigments.get(metal);
                ItemStack[] stacks = {this.getItem(SLOT_RESULTS[row][0]), this.getItem(SLOT_RESULTS[row][1]), this.getItem(SLOT_RESULTS[row][2])};
                int[] nums = new int[NUM_COLUMNS];

                //Calculate the count for each slot
                for(int col = 0; col < NUM_COLUMNS; col++){
                    nums[col] = count + stacks[col].getCount();
                    if(col < NUM_COLUMNS - 1 && nums[col] > stacks[col].getMaxStackSize()){
                        count = Mth.ceil((nums[col] - stacks[col].getMaxStackSize()) / (float)COLUMN_WEIGHTS[1]);
                        nums[col] -= count * COLUMN_WEIGHTS[1];
                    } else {
                        count = 0;
                    }
                }

                for(int col = 0; col < NUM_COLUMNS; col++){
                    if(stacks[col].isEmpty()){
                        stacks[col] = new ItemStack(metal.getItemFromColumn(col), nums[col]);
                        this.setItem(SLOT_RESULTS[row][col], stacks[col]);
                    } else {
                        stacks[col].setCount(nums[col]);
                    }
                }
            }
            inputStack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    protected Component getDefaultName() {
        return new TranslatableComponent("container.oddc.recycling_furnace");
    }

    protected AbstractContainerMenu createMenu(int containerCounter, Inventory inventory) {
        return new RecyclingFurnaceMenu(containerCounter, inventory, this, this.dataAccess);
    }

    @Override
    protected int getStandardBurnTime() {
        return 100;
    }

    @Override
    protected boolean isResultSlot(int index) {
        return index >= SLOT_RESULTS[0][0] && index <= SLOT_RESULTS[NUM_ROWS-1][NUM_COLUMNS-1];
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
}