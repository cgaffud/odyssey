package com.bedmen.odyssey.blocks;

import com.bedmen.odyssey.util.ItemRegistry;
import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NewCauldronBlock extends Block {
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
    public EffectInstance[] effectInstances = new EffectInstance[3];
    private static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.join(VoxelShapes.block(), VoxelShapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), IBooleanFunction.ONLY_FIRST);

    public NewCauldronBlock() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, Integer.valueOf(0)));
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    public VoxelShape getInteractionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return INSIDE;
    }

    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        int i = state.getValue(LEVEL);
        float f = (float)pos.getY() + (6.0F + (float)(3 * i)) / 16.0F;
        if (!worldIn.isClientSide && entityIn.isOnFire() && i > 0 && entityIn.getY() <= (double)f) {
            entityIn.clearFire();
            this.setWaterLevel(worldIn, pos, state, i - 1);
        }

    }

    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getItemInHand(handIn);
        if (itemstack.isEmpty()) {
            return ActionResultType.PASS;
        } else {
            int i = state.getValue(LEVEL);
            Item item = itemstack.getItem();
            if (item == Items.WATER_BUCKET) {
                if (i < 3 && !worldIn.isClientSide) {
                    if (!player.abilities.instabuild) {
                        player.setItemInHand(handIn, new ItemStack(Items.BUCKET));
                    }

                    player.awardStat(Stats.FILL_CAULDRON);
                    this.setWaterLevel(worldIn, pos, state, 3);
                    for(int j = i; j < 3; j++){
                        this.setEffectInstances(j,null);
                    }
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResultType.sidedSuccess(worldIn.isClientSide);
            } else if (item == Items.BUCKET) {
                if (i == 3 && this.isAllWater() && !worldIn.isClientSide) {
                    if (!player.abilities.instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.setItemInHand(handIn, new ItemStack(Items.WATER_BUCKET));
                        } else if (!player.inventory.add(new ItemStack(Items.WATER_BUCKET))) {
                            player.drop(new ItemStack(Items.WATER_BUCKET), false);
                        }
                    }

                    player.awardStat(Stats.USE_CAULDRON);
                    this.setWaterLevel(worldIn, pos, state, 0);
                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResultType.sidedSuccess(worldIn.isClientSide);
            } else if (item == Items.GLASS_BOTTLE) {
                if (i > 0 && (this.getEffectInstance(i-1) == null) && !worldIn.isClientSide) {
                    if (!player.abilities.instabuild) {
                        ItemStack itemstack4 = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                        player.awardStat(Stats.USE_CAULDRON);
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.setItemInHand(handIn, itemstack4);
                        } else if (!player.inventory.add(itemstack4)) {
                            player.drop(itemstack4, false);
                        } else if (player instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity)player).refreshContainer(player.inventoryMenu);
                        }
                    }

                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.setWaterLevel(worldIn, pos, state, i - 1);
                }

                return ActionResultType.sidedSuccess(worldIn.isClientSide);
            } else if (item == Items.POTION) {
                if (PotionUtils.getPotion(itemstack) == Potions.WATER) {
                    if (i < 3 && !worldIn.isClientSide) {
                        if (!player.abilities.instabuild) {
                            ItemStack itemstack3 = new ItemStack(Items.GLASS_BOTTLE);
                            player.awardStat(Stats.USE_CAULDRON);
                            player.setItemInHand(handIn, itemstack3);
                            if (player instanceof ServerPlayerEntity) {
                                ((ServerPlayerEntity) player).refreshContainer(player.inventoryMenu);
                            }
                        }

                        worldIn.playSound((PlayerEntity) null, pos, SoundEvents.BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        this.setWaterLevel(worldIn, pos, state, i + 1);
                        this.setEffectInstances(i, null);
                    }
                } else {
                    List<EffectInstance> instances = PotionUtils.getMobEffects(itemstack);
                    if (instances.size() == 1) {
                        EffectInstance instance = instances.get(0);
                        if (i < 3 && !worldIn.isClientSide) {
                            if (!player.abilities.instabuild) {
                                ItemStack itemstack3 = new ItemStack(Items.GLASS_BOTTLE);
                                player.awardStat(Stats.USE_CAULDRON);
                                player.setItemInHand(handIn, itemstack3);
                                if (player instanceof ServerPlayerEntity) {
                                    ((ServerPlayerEntity) player).refreshContainer(player.inventoryMenu);
                                }
                            }

                            worldIn.playSound((PlayerEntity) null, pos, SoundEvents.BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            this.setWaterLevel(worldIn, pos, state, i + 1);
                            this.setEffectInstances(i, instance);
                        }
                    }
                }

                return ActionResultType.sidedSuccess(worldIn.isClientSide);
            } else if(item == ItemRegistry.BIG_GLASS_BOTTLE.get()) {
                if (i == 3 && this.atleastOneEffect() && !worldIn.isClientSide) {
                    EffectInstance e0 = effectInstances[0];
                    EffectInstance e1 = effectInstances[1];
                    EffectInstance e2 = effectInstances[2];

                    if(e0 != null && e1 != null && e0.getEffect() == e1.getEffect() && e0.getAmplifier() == e1.getAmplifier()){
                        e0 = new EffectInstance(e0.getEffect(),e0.getDuration()+e1.getDuration(), e0.getAmplifier());
                        e1 = null;
                    }
                    if(e0 != null && e2 != null && e0.getEffect() == e2.getEffect() && e0.getAmplifier() == e2.getAmplifier()){
                        e0 = new EffectInstance(e0.getEffect(),e0.getDuration()+e2.getDuration(), e0.getAmplifier());
                        e2 = null;
                    }
                    if(e1 != null && e2 != null && e1.getEffect() == e2.getEffect() && e1.getAmplifier() == e2.getAmplifier()){
                        e1 = new EffectInstance(e1.getEffect(),e1.getDuration()+e2.getDuration(), e1.getAmplifier());
                        e2 = null;
                    }

                    player.awardStat(Stats.USE_CAULDRON);
                    ItemStack itemstack5 = PotionUtils.setPotion(new ItemStack(ItemRegistry.BIG_POTION.get()), Potions.EMPTY);
                    List<EffectInstance> effects = Lists.newArrayList();
                    if(e0 != null) effects.add(e0);
                    if(e1 != null) effects.add(e1);
                    if(e2 != null) effects.add(e2);
                    PotionUtils.setCustomEffects(itemstack5, effects);

                    if (!player.abilities.instabuild) {
                        itemstack.shrink(1);
                    }

                    if (itemstack.isEmpty()) {
                        player.setItemInHand(handIn, itemstack5);
                    } else if (!player.inventory.add(itemstack5)) {
                        player.drop(itemstack5, false);
                    } else if (player instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity)player).refreshContainer(player.inventoryMenu);
                    }

                    worldIn.playSound((PlayerEntity)null, pos, SoundEvents.BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.setWaterLevel(worldIn, pos, state, 0);
                    this.setAllEffectInstances(null);
                }

                return ActionResultType.sidedSuccess(worldIn.isClientSide);
            } else {
                if (i > 0 && item instanceof IDyeableArmorItem) {
                    IDyeableArmorItem idyeablearmoritem = (IDyeableArmorItem)item;
                    if (idyeablearmoritem.hasCustomColor(itemstack) && !worldIn.isClientSide) {
                        idyeablearmoritem.clearColor(itemstack);
                        this.setWaterLevel(worldIn, pos, state, i - 1);
                        player.awardStat(Stats.CLEAN_ARMOR);
                        return ActionResultType.SUCCESS;
                    }
                }

                if (i > 0 && item instanceof BannerItem) {
                    if (BannerTileEntity.getPatternCount(itemstack) > 0 && !worldIn.isClientSide) {
                        ItemStack itemstack2 = itemstack.copy();
                        itemstack2.setCount(1);
                        BannerTileEntity.removeLastPattern(itemstack2);
                        player.awardStat(Stats.CLEAN_BANNER);
                        if (!player.abilities.instabuild) {
                            itemstack.shrink(1);
                            this.setWaterLevel(worldIn, pos, state, i - 1);
                        }

                        if (itemstack.isEmpty()) {
                            player.setItemInHand(handIn, itemstack2);
                        } else if (!player.inventory.add(itemstack2)) {
                            player.drop(itemstack2, false);
                        } else if (player instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity)player).refreshContainer(player.inventoryMenu);
                        }
                    }

                    return ActionResultType.sidedSuccess(worldIn.isClientSide);
                } else if (i > 0 && item instanceof BlockItem) {
                    Block block = ((BlockItem)item).getBlock();
                    if (block instanceof ShulkerBoxBlock && !worldIn.isClientSide()) {
                        ItemStack itemstack1 = new ItemStack(Blocks.SHULKER_BOX, 1);
                        if (itemstack.hasTag()) {
                            itemstack1.setTag(itemstack.getTag().copy());
                        }

                        player.setItemInHand(handIn, itemstack1);
                        this.setWaterLevel(worldIn, pos, state, i - 1);
                        player.awardStat(Stats.CLEAN_SHULKER_BOX);
                        return ActionResultType.SUCCESS;
                    } else {
                        return ActionResultType.CONSUME;
                    }
                } else {
                    return ActionResultType.PASS;
                }
            }
        }
    }

    public void setWaterLevel(World worldIn, BlockPos pos, BlockState state, int level) {
        worldIn.setBlock(pos, state.setValue(LEVEL, Integer.valueOf(MathHelper.clamp(level, 0, 3))), 2);
        worldIn.updateNeighbourForOutputSignal(pos, this);
    }

    public EffectInstance getEffectInstance(int i){
        return this.effectInstances[i];
    }

    public boolean isAllWater(){
        for(int i = 0; i < 3; i++){
            if(this.getEffectInstance(i) != null) return false;
        }
        return true;
    }

    public void setEffectInstances(int i, EffectInstance effectInstance){
        this.effectInstances[i] = effectInstance;
    }

    public void setAllEffectInstances(EffectInstance effectInstance){
        for(int i = 0; i < 3; i++) this.effectInstances[i] = effectInstance;
    }


    public boolean atleastOneEffect(){
        for(int i = 0; i < 3; i++) {
            if(getEffectInstance(i) != null) return true;
        }
        return false;
    }

    /**
     * Called similar to random ticks, but only when it is raining.
     */
    public void handleRain(World worldIn, BlockPos pos) {
        if (worldIn.random.nextInt(20) == 1) {
            float f = worldIn.getBiome(pos).getTemperature(pos);
            if (!(f < 0.15F)) {
                BlockState blockstate = worldIn.getBlockState(pos);
                if (blockstate.getValue(LEVEL) < 3) {
                    worldIn.setBlock(pos, blockstate.cycle(LEVEL), 2);
                }

            }
        }
    }

    /**
     * @deprecated call via IBlockState#hasComparatorInputOverride() whenever possible. Implementing/overriding
     * is fine.
     */
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    /**
     * @deprecated call via IBlockState#getComparatorInputOverride(World,BlockPos) whenever possible.
     * Implementing/overriding is fine.
     */
    public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(LEVEL);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }
}
