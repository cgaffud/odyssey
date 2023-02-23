package com.bedmen.odyssey.inventory;

import com.bedmen.odyssey.inventory.slot.StitchingFiberSlot;
import com.bedmen.odyssey.inventory.slot.StitchingIngredientSlot;
import com.bedmen.odyssey.recipes.object.StitchingRecipe;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class StitchingMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT_0 = 0;
    public static final int INPUT_SLOT_1 = 1;
    public static final int FIBER_SLOT = 2;
    public static final int RESULT_SLOT = 3;
    private static final int INV_SLOT_START = 4;
    private static final int INV_SLOT_END = INV_SLOT_START + 27;
    private static final int USE_ROW_SLOT_END = INV_SLOT_END + 9;
    protected final ResultContainer resultSlots = new ResultContainer();
    protected final Container inputSlots = new SimpleContainer(3) {
        public void setChanged() {
            super.setChanged();
            StitchingMenu.this.slotsChanged(this);
        }
    };
    protected final ContainerLevelAccess access;
    protected final Player player;
    private final Level level;
    @Nullable
    private StitchingRecipe selectedRecipe;
    private final List<StitchingRecipe> recipes;

    protected boolean mayPickup(Player player, boolean b) {
        return this.selectedRecipe != null && this.selectedRecipe.matches(this.inputSlots, this.level);
    }

    protected void onTake(Player player, ItemStack itemStack) {
        itemStack.onCraftedBy(player.level, player, itemStack.getCount());
        this.resultSlots.awardUsedRecipes(player);
        this.shrinkStackInSlot(INPUT_SLOT_0);
        this.shrinkStackInSlot(INPUT_SLOT_1);
        this.shrinkStackInSlot(FIBER_SLOT, this.selectedRecipe.fiberCount);
        this.level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, this.level.random.nextFloat() * 0.1F + 0.9F, false);
    }

    private void shrinkStackInSlot(int id) {
        this.shrinkStackInSlot(id, 1);
    }

    private void shrinkStackInSlot(int id, int amount) {
        ItemStack itemstack = this.inputSlots.getItem(id);
        itemstack.shrink(amount);
        this.inputSlots.setItem(id, itemstack);
    }

    protected boolean isValidBlock(BlockState blockState) {
        return blockState.is(BlockRegistry.STITCHING_TABLE.get());
    }

    public StitchingMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public StitchingMenu(int containerId, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(ContainerRegistry.STITCHING_TABLE.get(), containerId);
        this.access = containerLevelAccess;
        this.player = inventory.player;
        this.addSlot(new StitchingIngredientSlot(this, this.inputSlots, INPUT_SLOT_0, 26, 40));
        this.addSlot(new StitchingIngredientSlot(this, this.inputSlots, INPUT_SLOT_1, 90, 40));
        this.addSlot(new StitchingFiberSlot(this, this.inputSlots, FIBER_SLOT, 58, 40));
        this.addSlot(new Slot(this.resultSlots, RESULT_SLOT, 134, 40) {
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }

            public boolean mayPickup(Player player) {
                return StitchingMenu.this.mayPickup(player, this.hasItem());
            }

            public void onTake(Player player, ItemStack itemStack) {
                StitchingMenu.this.onTake(player, itemStack);
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

        this.level = inventory.player.level;
        this.recipes = this.level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.STITCHING.get());
    }

    public void createResult() {
        Optional<StitchingRecipe> optionalStitchingRecipe = this.level.getRecipeManager().getRecipesFor(RecipeTypeRegistry.STITCHING.get(), this.inputSlots, this.level).stream().findFirst();
        if (optionalStitchingRecipe.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        } else {
            this.selectedRecipe = optionalStitchingRecipe.get();
            ItemStack itemstack = this.selectedRecipe.assemble(this.inputSlots);
            this.resultSlots.setRecipeUsed(this.selectedRecipe);
            this.resultSlots.setItem(0, itemstack);
        }
    }

    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (container == this.inputSlots) {
            this.createResult();
        }
    }

    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> {
            this.clearContainer(player, this.inputSlots);
        });
    }

    public boolean stillValid(Player player) {
        return this.access.evaluate((level, blockPos) -> {
            return this.isValidBlock(level.getBlockState(blockPos)) && player.distanceToSqr((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D) <= 64.0D;
        }, true);
    }

    public boolean isIngredient(ItemStack itemStack){
        for(StitchingRecipe stitchingRecipe : recipes){
            Ingredient ingredient = stitchingRecipe.getIngredient1();
            for(ItemStack itemStack1 : ingredient.getItems()){
                if(itemStack1.is(itemStack.getItem())){
                    return true;
                }
            }
            ingredient = stitchingRecipe.getIngredient2();
            for(ItemStack itemStack1 : ingredient.getItems()){
                if(itemStack1.is(itemStack.getItem())){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFiber(ItemStack itemStack){
        for(StitchingRecipe stitchingRecipe : recipes){
            Ingredient ingredient = stitchingRecipe.getFiber();
            for(ItemStack itemStack1 : ingredient.getItems()){
                if(itemStack1.is(itemStack.getItem())){
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack quickMoveStack(Player player, int id) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(id);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (id == RESULT_SLOT) {
                if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (id >= RESULT_SLOT) {
                if (id < USE_ROW_SLOT_END) {
                    if(isFiber(itemstack)){
                        if (!this.moveItemStackTo(itemstack1, FIBER_SLOT, RESULT_SLOT, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if(isIngredient(itemstack)){
                        if (!this.moveItemStackTo(itemstack1, INPUT_SLOT_0, FIBER_SLOT, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}