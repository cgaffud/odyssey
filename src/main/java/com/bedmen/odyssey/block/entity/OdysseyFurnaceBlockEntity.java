package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.recipes.OdysseyFurnaceRecipe;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public abstract class OdysseyFurnaceBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {
    protected NonNullList<ItemStack> items;
    protected final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    protected int litTime;
    protected int litDuration;
    protected int cookingProgress;
    protected int cookingTotalTime;
    public static final int DATA_COUNT = 4;
    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return OdysseyFurnaceBlockEntity.this.litTime;
                case 1:
                    return OdysseyFurnaceBlockEntity.this.litDuration;
                case 2:
                    return OdysseyFurnaceBlockEntity.this.cookingProgress;
                case 3:
                    return OdysseyFurnaceBlockEntity.this.cookingTotalTime;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    OdysseyFurnaceBlockEntity.this.litTime = value;
                    break;
                case 1:
                    OdysseyFurnaceBlockEntity.this.litDuration = value;
                    break;
                case 2:
                    OdysseyFurnaceBlockEntity.this.cookingProgress = value;
                    break;
                case 3:
                    OdysseyFurnaceBlockEntity.this.cookingTotalTime = value;
            }

        }

        public int getCount() {
            return DATA_COUNT;
        }
    };

    protected final RecipeType<? extends OdysseyFurnaceRecipe> recipeType;
    protected OdysseyFurnaceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, RecipeType<? extends OdysseyFurnaceRecipe> recipeType) {
        super(blockEntityType, blockPos, blockState);
        this.recipeType = recipeType;
    }

    protected static int getTotalCookTime(Level level, RecipeType<? extends OdysseyFurnaceRecipe> recipeType, OdysseyFurnaceBlockEntity odysseyFurnaceBlockEntity) {
        return level.getRecipeManager().getRecipeFor((RecipeType<OdysseyFurnaceRecipe>)recipeType, odysseyFurnaceBlockEntity, level).map(OdysseyFurnaceRecipe::getCookingTime).orElse(odysseyFurnaceBlockEntity.getStandardBurnTime());
    }

    protected abstract int getStandardBurnTime();

    protected abstract boolean isResultSlot(int index);

    protected abstract boolean isFuelSlot(int index);

    protected abstract int[] getSlotsForSide();

    protected abstract int[] getSlotsForUp();

    protected abstract int[] getSlotsForDown();

    protected boolean isLit() {
        return this.litTime > 0;
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items);
        this.litTime = compoundTag.getInt("BurnTime");
        this.cookingProgress = compoundTag.getInt("CookTime");
        this.cookingTotalTime = compoundTag.getInt("CookTimeTotal");
        this.litDuration = this.getBurnDuration(this.items.get(1));
        CompoundTag compoundTag1 = compoundTag.getCompound("RecipesUsed");

        for(String s : compoundTag1.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundTag1.getInt(s));
        }
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("BurnTime", this.litTime);
        compoundTag.putInt("CookTime", this.cookingProgress);
        compoundTag.putInt("CookTimeTotal", this.cookingTotalTime);
        ContainerHelper.saveAllItems(compoundTag, this.items);
        CompoundTag compoundtag1 = new CompoundTag();
        this.recipesUsed.forEach((p_187449_, p_187450_) -> {
            compoundtag1.putInt(p_187449_.toString(), p_187450_);
        });
        compoundTag.put("RecipesUsed", compoundtag1);
    }

    protected int getBurnDuration(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            Item item = itemStack.getItem();
            return net.minecraftforge.common.ForgeHooks.getBurnTime(itemStack, this.recipeType);
        }
    }

    public boolean canTakeItemThroughFace(int index, ItemStack itemStack, Direction direction) {
        if (direction == Direction.DOWN && isFuelSlot(index)) {
            return itemStack.is(Items.BUCKET);
        } else {
            return true;
        }
    }

    public int getContainerSize() {
        return this.items.size();
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

    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.items, index);
    }

    public void setItem(int index, ItemStack itemStack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !itemStack.isEmpty() && itemStack.sameItem(itemstack) && ItemStack.tagMatches(itemStack, itemstack);
        this.items.set(index, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        if (index == 0 && !flag) {
            this.cookingTotalTime = getTotalCookTime(this.level, this.recipeType, this);
            this.cookingProgress = 0;
            this.setChanged();
        }
    }

    public boolean stillValid(Player player) {
        if (Objects.requireNonNull(this.level).getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    public boolean canPlaceItem(int index, ItemStack itemStack) {
        if (isResultSlot(index)) {
            return false;
        } else if (!isFuelSlot(index)) {
            return true;
        } else {
            return net.minecraftforge.common.ForgeHooks.getBurnTime(itemStack, this.recipeType) > 0;
        }
    }

    public void clearContent() {
        this.items.clear();
    }

    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }
    }

    @Nullable
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    public void awardUsedRecipes(Player player) {
    }

    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return getSlotsForDown();
        } else {
            return direction == Direction.UP ? getSlotsForUp() : getSlotsForSide();
        }
    }

    public boolean canPlaceItemThroughFace(int id, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(id, itemStack);
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer serverPlayer) {
        List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience(serverPlayer.getLevel(), serverPlayer.position());
        serverPlayer.awardRecipes(list);
        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel serverLevel, Vec3 vec3) {
        List<Recipe<?>> list = Lists.newArrayList();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            serverLevel.getRecipeManager().byKey(entry.getKey()).ifPresent((p_155023_) -> {
                list.add(p_155023_);
                createExperience(serverLevel, vec3, entry.getIntValue(), ((OdysseyFurnaceRecipe)p_155023_).getExperience());
            });
        }

        return list;
    }

    protected static void createExperience(ServerLevel serverLevel, Vec3 vec3, int i1, float f1) {
        int i = Mth.floor((float)i1 * f1);
        float f = Mth.frac((float)i1 * f1);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }
        ExperienceOrb.award(serverLevel, vec3, i);
    }

    public void fillStackedContents(StackedContents stackedContents) {
        for(ItemStack itemstack : this.items) {
            stackedContents.accountStack(itemstack);
        }
    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.handlers = net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
    }
}
