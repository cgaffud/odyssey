package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.items.QuillItem;
import com.bedmen.odyssey.items.TomeItem;
import com.bedmen.odyssey.recipes.AlloyRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Objects;

public class ResearchTableBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_QUILL = 1;
    public static final int SLOT_INK = 2;
    public static final int SLOT_TOME = 3;
    public static final int SLOT_COUNT = 4;
    private static final int[] SLOTS_FOR_UP_AND_DOWN = new int[]{SLOT_INPUT};
    private static final int[] SLOTS_FOR_SIDES = new int[]{SLOT_INK};
    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return ResearchTableBlockEntity.this.researchTime;
                case 1:
                    return ResearchTableBlockEntity.this.ink;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    ResearchTableBlockEntity.this.researchTime = value;
                    break;
                case 1:
                    ResearchTableBlockEntity.this.ink = value;
            }

        }

        public int getCount() {
            return 2;
        }
    };
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private int researchTime;
    private int ink;

    protected ResearchTableBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, ResearchTableBlockEntity researchTableBlockEntity) {
        boolean flag = researchTableBlockEntity.isLit();
        boolean flag1 = false;
        if (researchTableBlockEntity.isLit()) {
            --researchTableBlockEntity.litTime;
        }

        ItemStack inkStack = researchTableBlockEntity.items.get(SLOT_INK);
        if (researchTableBlockEntity.isLit() || !inkStack.isEmpty() && !researchTableBlockEntity.items.get(SLOT_INPUT).isEmpty()) {
            Recipe<?> recipe = level.getRecipeManager().getRecipeFor((RecipeType<AlloyRecipe>)alloyFurnaceBlockEntity.recipeType, alloyFurnaceBlockEntity, level).orElse(null);
            int maxStackSize = alloyFurnaceBlockEntity.getMaxStackSize();
            if (!alloyFurnaceBlockEntity.isLit() && alloyFurnaceBlockEntity.canBurn(recipe, alloyFurnaceBlockEntity.items, maxStackSize)) {
                alloyFurnaceBlockEntity.litTime = alloyFurnaceBlockEntity.getBurnDuration(inkStack);
                alloyFurnaceBlockEntity.litDuration = alloyFurnaceBlockEntity.litTime;
                if (alloyFurnaceBlockEntity.isLit()) {
                    flag1 = true;
                    if (inkStack.hasContainerItem())
                        alloyFurnaceBlockEntity.items.set(SLOT_FUEL, inkStack.getContainerItem());
                    else
                    if (!inkStack.isEmpty()) {
                        inkStack.shrink(1);
                        if (inkStack.isEmpty()) {
                            alloyFurnaceBlockEntity.items.set(SLOT_FUEL, inkStack.getContainerItem());
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

    private boolean canResearch() {
        ItemStack inputStack = this.items.get(SLOT_INPUT);
        ItemStack tome = this.items.get(SLOT_TOME);
        Item tomeItem = tome.getItem();
        ItemStack quill = this.items.get(SLOT_QUILL);
        if (!this.items.get(SLOT_INPUT).isEmpty() &&
                tomeItem instanceof TomeItem tomeItem1 &&
                quill.getItem() instanceof QuillItem) {
            return tomeItem1.canBeResearched(inputStack, quill, tome);
        } else {
            return false;
        }
    }

    private boolean doResearch() {
        if (this.canResearch()) {
            ItemStack inputStack = this.items.get(SLOT_INPUT);
            ItemStack tomeStack = this.items.get(SLOT_TOME);
            TomeItem tomeItem = (TomeItem) tomeStack.getItem();
            tomeItem.addResearchedItem(inputStack, tomeStack);
            return true;
        } else {
            return false;
        }
    }

    protected Component getDefaultName() {
        return new TranslatableComponent("container.oddc.research_table");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerCounter, Inventory inventory) {
        return new ResearchTableMenu(containerCounter, inventory, this, this.dataAccess);
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    public ItemStack removeItem(int index, int amount) {
        return ContainerHelper.removeItem(this.items, index, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.items, index);
    }

    public void setItem(int index, ItemStack itemStack) {
        if(index >= 0 && index < this.items.size()) {
            this.items.set(index, itemStack);
        }
        if(!canResearch()){
            this.dataAccess.set(0, 0);
        }
    }

    public boolean stillValid(Player player) {
        if (Objects.requireNonNull(this.level).getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    public void clearContent() {
        this.items.clear();
    }

    public int[] getSlotsForFace(Direction direction) {
        if (direction.getAxis() == Direction.Axis.Y) {
            return SLOTS_FOR_UP_AND_DOWN;
        }
        return SLOTS_FOR_SIDES;
    }

    public boolean canPlaceItemThroughFace(int id, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(id, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int p_19239_, ItemStack p_19240_, Direction p_19241_) {
        return false;
    }
}
