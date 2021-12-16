package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.inventory.AlloyFurnaceMenu;
import com.bedmen.odyssey.recipes.AlloyRecipe;
import com.bedmen.odyssey.recipes.OdysseyRecipeType;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class AlloyFurnaceBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {
    public static final int SLOT_INPUT_0 = 0;
    public static final int SLOT_INPUT_1 = 1;
    public static final int SLOT_FUEL = 2;
    public static final int SLOT_RESULT = 3;
    public static final int SLOT_COUNT = 4;
    private static final int[] SLOTS_FOR_UP = new int[]{SLOT_INPUT_0, SLOT_INPUT_1};
    private static final int[] SLOTS_FOR_DOWN = new int[]{SLOT_FUEL, SLOT_RESULT};
    private static final int[] SLOTS_FOR_SIDES = new int[]{SLOT_FUEL};
    public static final int BURN_TIME_STANDARD = 200;
    public static final int BURN_COOL_SPEED = 2;
    public static final int DATA_COUNT = 4;
    protected NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    int litTime;
    int litDuration;
    int cookingProgress;
    int cookingTotalTime;
    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return AlloyFurnaceBlockEntity.this.litTime;
                case 1:
                    return AlloyFurnaceBlockEntity.this.litDuration;
                case 2:
                    return AlloyFurnaceBlockEntity.this.cookingProgress;
                case 3:
                    return AlloyFurnaceBlockEntity.this.cookingTotalTime;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    AlloyFurnaceBlockEntity.this.litTime = value;
                    break;
                case 1:
                    AlloyFurnaceBlockEntity.this.litDuration = value;
                    break;
                case 2:
                    AlloyFurnaceBlockEntity.this.cookingProgress = value;
                    break;
                case 3:
                    AlloyFurnaceBlockEntity.this.cookingTotalTime = value;
            }

        }

        public int getCount() {
            return DATA_COUNT;
        }
    };
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeType<? extends AlloyRecipe> recipeType;

    public AlloyFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.ALLOY_FURNACE.get(), blockPos, blockState);
        this.recipeType = OdysseyRecipeType.ALLOYING;
    }

    private boolean isLit() {
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
        CompoundTag compoundtag = compoundTag.getCompound("RecipesUsed");

        for(String s : compoundtag.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
        }

    }

    public CompoundTag save(CompoundTag compoundTag) {
        super.save(compoundTag);
        compoundTag.putInt("BurnTime", this.litTime);
        compoundTag.putInt("CookTime", this.cookingProgress);
        compoundTag.putInt("CookTimeTotal", this.cookingTotalTime);
        ContainerHelper.saveAllItems(compoundTag, this.items);
        CompoundTag compoundtag = new CompoundTag();
        this.recipesUsed.forEach((p_58382_, p_58383_) -> {
            compoundtag.putInt(p_58382_.toString(), p_58383_);
        });
        compoundTag.put("RecipesUsed", compoundtag);
        return compoundTag;
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

    protected int getBurnDuration(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            Item item = itemStack.getItem();
            return net.minecraftforge.common.ForgeHooks.getBurnTime(itemStack, this.recipeType) / 2;
        }
    }

    private static int getTotalCookTime(Level level, RecipeType<? extends AlloyRecipe> recipeType, Container container) {
        return level.getRecipeManager().getRecipeFor((RecipeType<AlloyRecipe>)recipeType, container, level).map(AlloyRecipe::getCookingTime).orElse(BURN_TIME_STANDARD);
    }

    public static boolean isFuel(ItemStack itemStack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(itemStack, null) > 0;
    }

    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
        }
    }

    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(index, itemStack);
    }

    public boolean canTakeItemThroughFace(int index, ItemStack itemStack, Direction direction) {
        if (direction == Direction.DOWN && index == SLOT_FUEL) {
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
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    public boolean canPlaceItem(int index, ItemStack itemStack) {
        if (index == SLOT_RESULT) {
            return false;
        } else if (index != SLOT_FUEL) {
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
                createExperience(serverLevel, vec3, entry.getIntValue(), ((AlloyRecipe)p_155023_).getExperience());
            });
        }

        return list;
    }

    private static void createExperience(ServerLevel serverLevel, Vec3 vec3, int i1, float f1) {
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

    protected Component getDefaultName() {
        return new TranslatableComponent("container.oddc.alloy_furnace");
    }

    protected AbstractContainerMenu createMenu(int containerCounter, Inventory inventory) {
        return new AlloyFurnaceMenu(containerCounter, inventory, this, this.dataAccess);
    }
}