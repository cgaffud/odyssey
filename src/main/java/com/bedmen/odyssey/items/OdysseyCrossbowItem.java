package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrowEntity;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.BowUtil;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.StringUtil;
import com.google.common.collect.Lists;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class OdysseyCrossbowItem extends CrossbowItem implements IVanishable, INeedsToRegisterItemModelProperty {
    /** Set to {@code true} when the crossbow is 20% charged. */
    private boolean isLoadingStart = false;
    /** Set to {@code true} when the crossbow is 50% charged. */
    private boolean isLoadingMiddle = false;
    private final float velocity;
    private final int chargeTime;

    public OdysseyCrossbowItem(Item.Properties propertiesIn, float velocity, int chargeTime) {
        super(propertiesIn);
        this.velocity = velocity;
        this.chargeTime = chargeTime;
    }

    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return ARROW_OR_FIREWORK;
    }

    /**
     * Get the predicate to match ammunition when searching the player's inventory, not their main/offhand
     */
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (isCharged(itemstack)) {
            float inaccuracy = EnchantmentUtil.getAccuracyMultiplier(playerIn);
            fireProjectiles(worldIn, playerIn, handIn, itemstack, getVelocity(itemstack), inaccuracy);
            setCharged(itemstack, false);
            return ActionResult.consume(itemstack);
        } else {
            if (!playerIn.getProjectile(itemstack).isEmpty()) {
                if (!isCharged(itemstack)) {
                    this.isLoadingStart = false;
                    this.isLoadingMiddle = false;
                    playerIn.startUsingItem(handIn);
                }

                return ActionResult.consume(itemstack);
            } else {
                return ActionResult.fail(itemstack);
            }
        }
    }

    /**
     * Called when the player stops using an Item (stops holding the right mouse button).
     */
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        float f = getCharge(i, stack);
        if (f >= 1.0F && !isCharged(stack) && hasAmmo(entityLiving, stack)) {
            setCharged(stack, true);
            SoundCategory soundcategory = entityLiving instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            worldIn.playSound((PlayerEntity)null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.CROSSBOW_LOADING_END, soundcategory, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }

    private static boolean hasAmmo(LivingEntity entityIn, ItemStack stack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.MULTISHOT.get(), stack);
        int j = i == 0 ? 1 : 3;
        boolean flag = entityIn instanceof PlayerEntity && ((PlayerEntity)entityIn).abilities.instabuild;
        ItemStack itemstack = entityIn.getProjectile(stack);
        ItemStack itemstack1 = itemstack.copy();

        for(int k = 0; k < j; ++k) {
            if (k > 0) {
                itemstack = itemstack1.copy();
            }

            if (itemstack.isEmpty() && flag) {
                itemstack = new ItemStack(Items.ARROW);
                itemstack1 = itemstack.copy();
            }

            if (!loadProjectile(entityIn, stack, itemstack, k > 0, flag)) {
                return false;
            }
        }

        return true;
    }

    private static boolean loadProjectile(LivingEntity livingEntity, ItemStack crossbow, ItemStack ammo, boolean multishotArrow, boolean inCreative) {
        if (ammo.isEmpty()) {
            return false;
        } else {
            boolean flag = inCreative && ammo.getItem() instanceof ArrowItem;
            ItemStack itemstack;
            if (!flag && !inCreative && !multishotArrow) {
                boolean quiverFlag = false;
                if(livingEntity instanceof PlayerEntity){
                    quiverFlag = BowUtil.consumeQuiverAmmo((PlayerEntity)livingEntity,ammo);
                }
                itemstack = ammo.split(1);
                if(!quiverFlag) {
                    if (ammo.isEmpty() && livingEntity instanceof PlayerEntity) {
                        ((PlayerEntity) livingEntity).inventory.removeItem(ammo);
                    }
                }
            } else {
                itemstack = ammo.copy();
            }

            addChargedProjectile(crossbow, itemstack);
            return true;
        }
    }

    public static boolean isCharged(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTag();
        return compoundnbt != null && compoundnbt.getBoolean("Charged");
    }

    public static void setCharged(ItemStack stack, boolean chargedIn) {
        CompoundNBT compoundnbt = stack.getOrCreateTag();
        compoundnbt.putBoolean("Charged", chargedIn);
    }

    private static void addChargedProjectile(ItemStack crossbow, ItemStack projectile) {
        CompoundNBT compoundnbt = crossbow.getOrCreateTag();
        ListNBT listnbt;
        if (compoundnbt.contains("ChargedProjectiles", 9)) {
            listnbt = compoundnbt.getList("ChargedProjectiles", 10);
        } else {
            listnbt = new ListNBT();
        }

        CompoundNBT compoundnbt1 = new CompoundNBT();
        projectile.save(compoundnbt1);
        listnbt.add(compoundnbt1);
        compoundnbt.put("ChargedProjectiles", listnbt);
    }

    private static List<ItemStack> getChargedProjectiles(ItemStack stack) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundNBT compoundnbt = stack.getTag();
        if (compoundnbt != null && compoundnbt.contains("ChargedProjectiles", 9)) {
            ListNBT listnbt = compoundnbt.getList("ChargedProjectiles", 10);
            if (listnbt != null) {
                for(int i = 0; i < listnbt.size(); ++i) {
                    CompoundNBT compoundnbt1 = listnbt.getCompound(i);
                    list.add(ItemStack.of(compoundnbt1));
                }
            }
        }

        return list;
    }

    private static void clearProjectiles(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTag();
        if (compoundnbt != null) {
            ListNBT listnbt = compoundnbt.getList("ChargedProjectiles", 9);
            listnbt.clear();
            compoundnbt.put("ChargedProjectiles", listnbt);
        }

    }

    public static boolean hasChargedProjectile(ItemStack stack, Item ammoItem) {
        return getChargedProjectiles(stack).stream().anyMatch((p_220010_1_) -> {
            return p_220010_1_.getItem() == ammoItem;
        });
    }

    private static void fireProjectile(World worldIn, LivingEntity shooter, Hand handIn, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle) {
        if (!worldIn.isClientSide) {
            boolean flag = projectile.getItem() == Items.FIREWORK_ROCKET;
            ProjectileEntity projectileentity;
            if (flag) {
                projectileentity = new FireworkRocketEntity(worldIn, projectile, shooter, shooter.getX(), shooter.getEyeY() - (double)0.15F, shooter.getZ(), true);
            } else {
                projectileentity = createArrow(worldIn, shooter, crossbow, projectile);
                if (isCreativeMode || projectileAngle != 0.0F) {
                    ((OdysseyAbstractArrowEntity)projectileentity).pickup = OdysseyAbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                }
            }

            if (shooter instanceof ICrossbowUser) {
                ICrossbowUser icrossbowuser = (ICrossbowUser)shooter;
                icrossbowuser.shootCrossbowProjectile(icrossbowuser.getTarget(), crossbow, projectileentity, projectileAngle);
            } else {
                Vector3d vector3d1 = shooter.getUpVector(1.0F);
                Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), projectileAngle, true);
                Vector3d vector3d = shooter.getViewVector(1.0F);
                Vector3f vector3f = new Vector3f(vector3d);
                vector3f.transform(quaternion);
                projectileentity.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), velocity, inaccuracy);
            }

            crossbow.hurtAndBreak(flag ? 3 : 1, shooter, (p_220017_1_) -> {
                p_220017_1_.broadcastBreakEvent(handIn);
            });
            worldIn.addFreshEntity(projectileentity);
            worldIn.playSound((PlayerEntity)null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
        }
    }

    private static OdysseyAbstractArrowEntity createArrow(World worldIn, LivingEntity shooter, ItemStack crossbow, ItemStack ammo) {
        OdysseyArrowItem odysseyArrowItem = (OdysseyArrowItem)(ammo.getItem() instanceof OdysseyArrowItem ? ammo.getItem() : ItemRegistry.ARROW.get());
        OdysseyAbstractArrowEntity odysseyAbstractArrowEntity = odysseyArrowItem.createArrow(worldIn, ammo, shooter);

        odysseyAbstractArrowEntity.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        odysseyAbstractArrowEntity.setShotFromCrossbow(true);
        int k = EnchantmentUtil.getPunch(crossbow);
        if (k > 0) {
            odysseyAbstractArrowEntity.setKnockback(k);
        }
        k = EnchantmentUtil.getPiercing(crossbow);
        if (k > 0) {
            odysseyAbstractArrowEntity.setPierceLevel((byte)k);
        }
        k = EnchantmentUtil.getFlame(crossbow);
        if (k > 0) {
            odysseyAbstractArrowEntity.setRemainingFireTicks(100*k);
        }

        return odysseyAbstractArrowEntity;
    }

    public static void fireProjectiles(World worldIn, LivingEntity shooter, Hand handIn, ItemStack stack, float velocityIn, float inaccuracyIn) {
        List<ItemStack> list = getChargedProjectiles(stack);
        float[] afloat = getRandomSoundPitches(shooter.getRandom());

        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            boolean flag = shooter instanceof PlayerEntity && ((PlayerEntity)shooter).abilities.instabuild;
            if (!itemstack.isEmpty()) {
                if (i == 0) {
                    fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 0.0F);
                } else if (i == 1) {
                    fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, -10.0F);
                } else if (i == 2) {
                    fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 10.0F);
                }
            }
        }

        fireProjectilesAfter(worldIn, shooter, stack);
    }

    private static float[] getRandomSoundPitches(Random rand) {
        boolean flag = rand.nextBoolean();
        return new float[]{1.0F, getRandomSoundPitch(flag), getRandomSoundPitch(!flag)};
    }

    private static float getRandomSoundPitch(boolean flagIn) {
        float f = flagIn ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    /**
     * Called after {@plainlink #fireProjectiles} to clear the charged projectile and to update the player advancements.
     */
    private static void fireProjectilesAfter(World worldIn, LivingEntity shooter, ItemStack stack) {
        if (shooter instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)shooter;
            if (!worldIn.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayerentity, stack);
            }

            serverplayerentity.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        }

        clearProjectiles(stack);
    }

    /**
     * Called as the item is being used by an entity.
     */
    public void onUseTick(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if (!worldIn.isClientSide) {
            int i = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.QUICK_CHARGE.get(), stack);
            SoundEvent soundevent = this.getSoundEvent(i);
            SoundEvent soundevent1 = i == 0 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
            float f = (float)(stack.getUseDuration() - count) / (float) getChargeDuration(stack);
            if (f < 0.2F) {
                this.isLoadingStart = false;
                this.isLoadingMiddle = false;
            }

            if (f >= 0.2F && !this.isLoadingStart) {
                this.isLoadingStart = true;
                worldIn.playSound(null, livingEntityIn.getX(), livingEntityIn.getY(), livingEntityIn.getZ(), soundevent, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 0.5F && soundevent1 != null && !this.isLoadingMiddle) {
                this.isLoadingMiddle = true;
                worldIn.playSound(null, livingEntityIn.getX(), livingEntityIn.getY(), livingEntityIn.getZ(), soundevent1, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }
        }

    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack) {
        return getChargeDuration(stack) + 3;
    }

    /**
     * The time the crossbow must be used to reload it
     */
    public static int getChargeDuration(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if(item instanceof OdysseyCrossbowItem){
            return EnchantmentUtil.getQuickChargeTime(((OdysseyCrossbowItem)item).chargeTime, itemStack);
        }
        return EnchantmentUtil.getQuickChargeTime(25, itemStack);
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    private SoundEvent getSoundEvent(int enchantmentLevel) {
        switch(enchantmentLevel) {
            case 1:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_1;
            case 2:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_2;
            case 3:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_3;
            default:
                return SoundEvents.CROSSBOW_LOADING_START;
        }
    }

    private static float getCharge(int useTime, ItemStack stack) {
        float f = (float)useTime / (float) getChargeDuration(stack);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    //Basically IsCrossbow
    public boolean useOnRelease(ItemStack stack) {
        return true;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.oddc.bow.velocity").append(StringUtil.floatFormat(this.velocity)).withStyle(TextFormatting.BLUE));
        tooltip.add(new TranslationTextComponent("item.oddc.bow.charge_time").append(StringUtil.floatFormat(getChargeDuration(stack)/20f)).append("s").withStyle(TextFormatting.BLUE));
        List<ItemStack> list = getChargedProjectiles(stack);
        if (isCharged(stack) && !list.isEmpty()) {
            ItemStack itemstack = list.get(0);
            tooltip.add((new TranslationTextComponent("item.minecraft.crossbow.projectile")).append(" ").append(itemstack.getDisplayName()));
            if (flagIn.isAdvanced() && itemstack.getItem() == Items.FIREWORK_ROCKET) {
                List<ITextComponent> list1 = Lists.newArrayList();
                Items.FIREWORK_ROCKET.appendHoverText(itemstack, worldIn, list1, flagIn);
                if (!list1.isEmpty()) {
                    for(int i = 0; i < list1.size(); ++i) {
                        list1.set(i, (new StringTextComponent("  ")).append(list1.get(i)).withStyle(TextFormatting.GRAY));
                    }
                    tooltip.addAll(list1);
                }
            }
        }
    }

    private float getVelocity(ItemStack itemStack) {
        float f = BowUtil.BASE_ARROW_VELOCITY * this.velocity;
        f *= hasChargedProjectile(itemStack, Items.FIREWORK_ROCKET) ? 0.5 : 1.0f;
        return f;
    }

    public int getDefaultProjectileRange() {
        return 8;
    }

    public void registerItemModelProperties(){
        ItemModelsProperties.register(this, new ResourceLocation("pull"), (itemStack, world, entity) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return OdysseyCrossbowItem.isCharged(itemStack) ? 0.0F : (float)(itemStack.getUseDuration() - entity.getUseItemRemainingTicks()) / (float) OdysseyCrossbowItem.getChargeDuration(itemStack);
            }
        });
        ItemModelsProperties.register(this, new ResourceLocation("pulling"), (itemStack, world, entity) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == itemStack && !OdysseyCrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
        });
        ItemModelsProperties.register(this, new ResourceLocation("charged"), (itemStack, world, entity) -> {
            return entity != null && OdysseyCrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
        });
        ItemModelsProperties.register(this, new ResourceLocation("firework"), (itemStack, world, entity) -> {
            return entity != null && OdysseyCrossbowItem.isCharged(itemStack) && OdysseyCrossbowItem.hasChargedProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });
    }

}
