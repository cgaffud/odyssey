package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.inventory.ResearchTableMenu;
import com.bedmen.odyssey.items.QuillItem;
import com.bedmen.odyssey.items.TomeItem;
import com.bedmen.odyssey.recipes.AlloyRecipe;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
    public static final int DATA_COUNT = 2;
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
            return DATA_COUNT;
        }
    };
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private int researchTime;
    private int ink;
    private static final String RESEARCH_TIME_TAG = "ResearchTime";
    private static final String INK_TAG = "Ink";
    private static final int SAC_TO_INK_FACTOR = 5;
    public static final int TOTAL_RESEARCH_TIME = 12000;

    public ResearchTableBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.RESEARCH_TABLE.get(), blockPos, blockState);
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items);
        this.researchTime = compoundTag.getInt(RESEARCH_TIME_TAG);
        this.ink = compoundTag.getInt(INK_TAG);
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt(RESEARCH_TIME_TAG, this.researchTime);
        compoundTag.putInt(INK_TAG, this.ink);
        ContainerHelper.saveAllItems(compoundTag, this.items);
    }

    protected boolean isResearching() {
        return this.researchTime > -1;
    }

    protected ItemStack getInputItemStack(){
        return this.items.get(SLOT_INPUT);
    }

    protected ItemStack getQuillItemStack(){
        return this.items.get(SLOT_QUILL);
    }

    protected ItemStack getInkItemStack(){
        return this.items.get(SLOT_INK);
    }

    protected ItemStack getTomeItemStack(){
        return this.items.get(SLOT_TOME);
    }

    protected void stopResearch(){
        this.researchTime = -1;
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, ResearchTableBlockEntity researchTableBlockEntity) {
        boolean isDirty = false;

        // Ink
        ItemStack inkStack = researchTableBlockEntity.getInkItemStack();
        if(researchTableBlockEntity.ink <= 0 && inkStack.getCount() > 0) {
            researchTableBlockEntity.ink = SAC_TO_INK_FACTOR;
            inkStack.shrink(1);
        }

        // Start research if possible
        if(!researchTableBlockEntity.isResearching() && researchTableBlockEntity.canResearch() && researchTableBlockEntity.ink > 0){
            researchTableBlockEntity.researchTime = 0;
            isDirty = true;
            researchTableBlockEntity.ink--;
        }

        // Research
        if(researchTableBlockEntity.isResearching() && researchTableBlockEntity.canResearch()) {
            if(researchTableBlockEntity.researchTime == TOTAL_RESEARCH_TIME) {
                researchTableBlockEntity.doResearch();
                researchTableBlockEntity.stopResearch();
            } else {
                researchTableBlockEntity.researchTime++;
            }
        } else {
            researchTableBlockEntity.stopResearch();
        }

        if (isDirty) {
            setChanged(level, blockPos, blockState);
        }
    }

    private boolean canResearch() {
        ItemStack inputStack = this.getInputItemStack();
        ItemStack tomeStack = this.getTomeItemStack();
        Item tomeItem = tomeStack.getItem();
        ItemStack quillStack = this.getQuillItemStack();
        if (!inputStack.isEmpty() &&
                tomeItem instanceof TomeItem tomeItem1 &&
                quillStack.getItem() instanceof QuillItem) {
            return tomeItem1.canBeResearched(inputStack, quillStack, tomeStack);
        } else {
            return false;
        }
    }

    private boolean doResearch() {
        if (this.canResearch()) {
            ItemStack inputStack = this.getInputItemStack();
            ItemStack tomeStack = this.getTomeItemStack();
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

    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        if (direction == Direction.UP) {
            return true;
        } else if (direction.getAxis().isHorizontal()) {
            return isGlowInk(itemStack);
        }
        return false;
    }

    public static boolean isGlowInk(ItemStack itemStack) {
        return itemStack.getItem() == Items.GLOW_INK_SAC;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return false;
    }
}
