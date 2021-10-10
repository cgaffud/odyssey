package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.container.OdysseyPlayerContainer;
import com.bedmen.odyssey.entity.player.IPlayerPermanentBuffs;
import com.bedmen.odyssey.entity.player.OdysseyPlayerInventory;
import com.bedmen.odyssey.items.QuiverItem;
import com.bedmen.odyssey.items.equipment.DualWieldItem;
import com.bedmen.odyssey.items.equipment.IEquipment;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectUtils;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements IPlayerPermanentBuffs {

    @Shadow
    public void startFallFlying() {}
    @Shadow
    public int experienceLevel;
    @Shadow
    public PlayerAbilities abilities;
    @Shadow
    public CooldownTracker getCooldowns() {return null;}
    @Shadow
    public int takeXpDelay;
    @Shadow
    private int sleepCounter;
    @Shadow
    public void stopSleepInBed(boolean p_225652_1_, boolean p_225652_2_) {}
    @Shadow
    protected boolean updateIsUnderwater() {return false;}
    @Shadow
    public Container containerMenu;
    @Shadow
    public void closeContainer() {}
    @Shadow
    public PlayerContainer inventoryMenu;
    @Shadow
    private void moveCloak() {}
    @Shadow
    protected FoodStats foodData;
    @Shadow
    public void awardStat(Stat<?> p_71029_1_) {}
    @Shadow
    public void awardStat(ResourceLocation resourceLocation) {}
    @Shadow
    private ItemStack lastItemInMainHand;
    @Shadow
    public void resetAttackStrengthTicker() {}
    @Shadow
    private void turtleHelmetTick() {}
    @Shadow
    private CooldownTracker cooldowns;
    @Shadow
    protected void updatePlayerPose() {}
    @Shadow
    public void awardStat(ResourceLocation p_195067_1_, int p_195067_2_) {}
    @Shadow
    public void causeFoodExhaustion(float p_71020_1_) {}
    @Shadow
    public void magicCrit(Entity p_71047_1_) {}
    @Shadow
    public float getAttackStrengthScale(float p_184825_1_) {return 0.0f;}
    @Shadow
    public void sweepAttack() {}
    @Shadow
    public void crit(Entity p_71009_1_) {}

    public final PlayerInventory inventory = new OdysseyPlayerInventory(getPlayerEntity());

    public int lifeFruits = 0;

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public int getLifeFruits(){
        return lifeFruits;
    }

    public void setLifeFruits(int i){
        lifeFruits = MathHelper.clamp(i, 0, 10);
    }

    public void incrementLifeFruits(){
        lifeFruits = MathHelper.clamp(lifeFruits+1, 0, 10);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void onPlayerEntity(World p_i241920_1_, BlockPos p_i241920_2_, float p_i241920_3_, GameProfile p_i241920_4_, CallbackInfo ci) {
        this.inventoryMenu = new OdysseyPlayerContainer(this.inventory, !p_i241920_1_.isClientSide, getPlayerEntity());
        this.containerMenu = this.inventoryMenu;
    }

    //Remade the amount of xp needed up to level 50 because Odyssey has lvl 50 enchants
    public int getXpNeededForNextLevel() {
        if (this.experienceLevel >= 50) {
            return 304 + (this.experienceLevel - 49) * 12;
        } else if (this.experienceLevel >= 40) {
            return 204 + (this.experienceLevel - 39) * 10;
        } else if (this.experienceLevel >= 30) {
            return 124 + (this.experienceLevel - 29) * 8;
        } else if (this.experienceLevel >= 20) {
            return 64 + (this.experienceLevel - 19) * 6;
        } else if (this.experienceLevel >= 10) {
            return 24 + (this.experienceLevel - 9) * 4;
        } else {
            return 4 + (this.experienceLevel+1) * 2;
        }
    }

    //Allows for ammo to be found from quivers
    public ItemStack getProjectile(ItemStack shootable) {
        if (!(shootable.getItem() instanceof ShootableItem)) {
            return ItemStack.EMPTY;
        } else {
            Predicate<ItemStack> predicate = ((ShootableItem)shootable.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ShootableItem.getHeldProjectile(this, predicate);
            if (!itemstack.isEmpty()) {
                return itemstack;
            } else {
                NonNullList<ItemStack> offhand = this.inventory.offhand;
                for (ItemStack itemstack1 : offhand) {
                    Item item = itemstack1.getItem();
                    if (item instanceof QuiverItem) {
                        CompoundNBT compoundNBT = itemstack1.getOrCreateTag();
                        if (compoundNBT.contains("Items", 9)) {
                            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(((QuiverItem) item).getSize(), ItemStack.EMPTY);
                            ItemStackHelper.loadAllItems(compoundNBT, nonnulllist);
                            for (int j = 0; j < nonnulllist.size(); j++) {
                                ItemStack itemstack2 = nonnulllist.get(j);
                                if (predicate.test(itemstack2)) {
                                    return itemstack2;
                                }
                            }
                        }
                    }
                }

                predicate = ((ShootableItem)shootable.getItem()).getAllSupportedProjectiles();

                for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
                    ItemStack itemstack1 = this.inventory.getItem(i);
                    if (predicate.test(itemstack1)) {
                        return itemstack1;
                    }
                }

                return this.abilities.instabuild ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
        }
    }

    //Disables All kinds of shields
    public void disableShield(boolean p_190777_1_) {
        float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
        if (p_190777_1_) {
            f += 0.75F;
        }

        if (this.random.nextFloat() < f) {
            for(Item item : OdysseyItemTags.SHIELD_TAG){
                this.getCooldowns().addCooldown(item, EnchantmentUtil.getRecoveryTicks(this));
            }
            this.stopUsingItem();
            this.level.broadcastEntityEvent(this, (byte)30);
        }

    }

    protected void hurtCurrentlyUsedShield(float p_184590_1_) {
        if (this.useItem.isShield(this)) {
            if (!this.level.isClientSide) {
                this.awardStat(Stats.ITEM_USED.get(this.useItem.getItem()));
            }
            //Changed from 3.0f to 0.0f so that shield lose durability on soft attacks
            if (p_184590_1_ >= 0.0F) {
                int i = 1 + MathHelper.floor(p_184590_1_);
                Hand hand = this.getUsedItemHand();
                this.useItem.hurtAndBreak(i, this, (p_213833_1_) -> {
                    p_213833_1_.broadcastBreakEvent(hand);
                    ForgeEventFactory.onPlayerDestroyItem(getPlayerEntity(), this.useItem, hand);
                });
                if (this.useItem.isEmpty()) {
                    if (hand == Hand.MAIN_HAND) {
                        this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                    } else {
                        this.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                    }

                    this.useItem = ItemStack.EMPTY;
                    this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
                }
            }
        }
    }

    //Removed turtle helmet tick
    public void tick() {
        net.minecraftforge.fml.hooks.BasicEventHooks.onPlayerPreTick(getPlayerEntity());
        this.noPhysics = this.isSpectator();
        if (this.isSpectator()) {
            this.onGround = false;
        }

        if (this.takeXpDelay > 0) {
            --this.takeXpDelay;
        }

        if (this.isSleeping()) {
            ++this.sleepCounter;
            if (this.sleepCounter > 100) {
                this.sleepCounter = 100;
            }

            if (!this.level.isClientSide && !net.minecraftforge.event.ForgeEventFactory.fireSleepingTimeCheck(getPlayerEntity(), getSleepingPos())) {
                this.stopSleepInBed(false, true);
            }
        } else if (this.sleepCounter > 0) {
            ++this.sleepCounter;
            if (this.sleepCounter >= 110) {
                this.sleepCounter = 0;
            }
        }

        this.updateIsUnderwater();
        super.tick();
        if (!this.level.isClientSide && this.containerMenu != null && !this.containerMenu.stillValid(getPlayerEntity())) {
            this.closeContainer();
            this.containerMenu = this.inventoryMenu;
        }

        this.moveCloak();
        if (!this.level.isClientSide) {
            this.foodData.tick(getPlayerEntity());
            this.awardStat(Stats.PLAY_ONE_MINUTE);
            if (this.isAlive()) {
                this.awardStat(Stats.TIME_SINCE_DEATH);
            }

            if (this.isDiscrete()) {
                this.awardStat(Stats.CROUCH_TIME);
            }

            if (!this.isSleeping()) {
                this.awardStat(Stats.TIME_SINCE_REST);
            }
        }

        int i = 29999999;
        double d0 = MathHelper.clamp(this.getX(), -2.9999999E7D, 2.9999999E7D);
        double d1 = MathHelper.clamp(this.getZ(), -2.9999999E7D, 2.9999999E7D);
        if (d0 != this.getX() || d1 != this.getZ()) {
            this.setPos(d0, this.getY(), d1);
        }

        ++this.attackStrengthTicker;
        ItemStack itemstack = this.getMainHandItem();
        if (!ItemStack.matches(this.lastItemInMainHand, itemstack)) {
            if (!ItemStack.isSameIgnoreDurability(this.lastItemInMainHand, itemstack)) {
                this.resetAttackStrengthTicker();
            }

            this.lastItemInMainHand = itemstack.copy();
        }

        this.cooldowns.tick();
        this.updatePlayerPose();
        net.minecraftforge.fml.hooks.BasicEventHooks.onPlayerPostTick(getPlayerEntity());
    }

    public void updateSwimming() {
        if (this.abilities.flying) {
            this.setSwimming(false);
        } else {
            boolean vulcan = 0 < EnchantmentUtil.getVulcanStrider(this);
            if (this.isSwimming()) {
                this.setSwimming(this.isSprinting() && (this.isInWater() || (vulcan && this.isInLava())) && !this.isPassenger());
            } else {
                this.setSwimming(this.isSprinting() && (this.isUnderWater() || (vulcan && this.isInLava() && this.isEyeInFluid(FluidTags.LAVA))) && !this.isPassenger());
            }
        }
    }

    public float getDigSpeed(BlockState p_184813_1_, @Nullable BlockPos pos) {
        float f = this.inventory.getDestroySpeed(p_184813_1_);
        if (f > 1.0F) {
            int i = EnchantmentHelper.getBlockEfficiency(this);
            ItemStack itemstack = this.getMainHandItem();
            if (i > 0 && !itemstack.isEmpty()) {
                f += (float)(i * i + 1);
            }
        }

        if (EffectUtils.hasDigSpeed(this)) {
            f *= 1.0F + (float)(EffectUtils.getDigSpeedAmplification(this) + 1) * 0.2F;
        }

        if (this.hasEffect(Effects.DIG_SLOWDOWN)) {
            float f1;
            switch(this.getEffect(Effects.DIG_SLOWDOWN).getAmplifier()) {
                case 0:
                    f1 = 0.3F;
                    break;
                case 1:
                    f1 = 0.09F;
                    break;
                case 2:
                    f1 = 0.0027F;
                    break;
                case 3:
                default:
                    f1 = 8.1E-4F;
            }

            f *= f1;
        }

        if ((this.isEyeInFluid(FluidTags.WATER) && !EnchantmentUtil.hasAquaAffinity(getPlayerEntity()))
                || (this.isEyeInFluid(FluidTags.LAVA) && !EnchantmentUtil.hasMoltenAffinity(getPlayerEntity()))) {
            f /= 5.0F;
        }

        if (!this.onGround) {
            f /= 5.0F;
        }

        f = net.minecraftforge.event.ForgeEventFactory.getBreakSpeed(getPlayerEntity(), p_184813_1_, f, pos);
        return f;
    }

    protected void actuallyHurt(DamageSource damageSource, float amount) {
        if (!this.isInvulnerableTo(damageSource)) {
            amount = net.minecraftforge.common.ForgeHooks.onLivingHurt(this, damageSource, amount);
            if (amount <= 0) return;
            amount = this.getDamageAfterArmorAbsorb(damageSource, amount);
            if(amount >= 10.0f && this.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ItemRegistry.HOLLOW_COCONUT.get() && damageSource != DamageSource.FALL){
                Consumer<PlayerEntity> consumer = (p_233653_0_) -> {
                    p_233653_0_.broadcastBreakEvent(EquipmentSlotType.HEAD);
                };
                PlayerEntity player = getPlayerEntity();
                ItemStack itemStack = this.getItemBySlot(EquipmentSlotType.HEAD);
                Item item = itemStack.getItem();
                item.damageItem(itemStack, 100, player, consumer);
                consumer.accept(player);
                itemStack.shrink(1);
                player.awardStat(Stats.ITEM_BROKEN.get(item));
            }
            amount = this.getDamageAfterMagicAbsorb(damageSource, amount);
            float f2 = Math.max(amount - this.getAbsorptionAmount(), 0.0F);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (amount - f2));
            f2 = net.minecraftforge.common.ForgeHooks.onLivingDamage(this, damageSource, f2);
            float f = amount - f2;
            if (f > 0.0F && f < 3.4028235E37F) {
                this.awardStat(Stats.DAMAGE_ABSORBED, Math.round(f * 10.0F));
            }

            if (f2 != 0.0F) {
                this.causeFoodExhaustion(damageSource.getFoodExhaustion());
                float f1 = this.getHealth();
                this.setHealth(this.getHealth() - f2);
                this.getCombatTracker().recordDamage(damageSource, f1, f2);
                if (f2 < 3.4028235E37F) {
                    this.awardStat(Stats.DAMAGE_TAKEN, Math.round(f2 * 10.0F));
                }

            }
        }
    }

    public void attack(Entity p_71059_1_) {
        if (!net.minecraftforge.common.ForgeHooks.onPlayerAttackTarget(getPlayerEntity(), p_71059_1_)) return;
        if (p_71059_1_.isAttackable()) {
            if (!p_71059_1_.skipAttackInteraction(this)) {
                float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                float f1;
                if (p_71059_1_ instanceof LivingEntity) {
                    f1 = EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)p_71059_1_).getMobType());
                } else {
                    f1 = EnchantmentHelper.getDamageBonus(this.getMainHandItem(), CreatureAttribute.UNDEFINED);
                }

                float f2 = this.getAttackStrengthScale(0.5F);
                f = f * (0.2F + f2 * f2 * 0.8F);
                f1 = f1 * f2;
                this.resetAttackStrengthTicker();
                if (f > 0.0F || f1 > 0.0F) {
                    boolean flag = f2 > 0.9F;
                    boolean flag1 = false;
                    int i = 0;
                    i = i + EnchantmentHelper.getKnockbackBonus(this);
                    if (this.isSprinting() && flag) {
                        this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, this.getSoundSource(), 1.0F, 1.0F);
                        ++i;
                        flag1 = true;
                    }

                    boolean flag2 = flag && this.fallDistance > 0.0F && !this.onGround && !this.onClimbable() && !this.isInWater() && !this.hasEffect(Effects.BLINDNESS) && !this.isPassenger() && p_71059_1_ instanceof LivingEntity && !DualWieldItem.isDualWielding(getPlayerEntity());
                    flag2 = flag2 && !this.isSprinting();
                    net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(getPlayerEntity(), p_71059_1_, flag2, flag2 ? 1.5F : 1.0F);
                    flag2 = hitResult != null;
                    if (flag2) {
                        f *= hitResult.getDamageModifier();
                    }

                    f = f + f1;
                    boolean flag3 = false;
                    double d0 = (double)(this.walkDist - this.walkDistO);
                    if (flag && !flag2 && !flag1 && this.onGround && d0 < (double)this.getSpeed()) {
                        ItemStack itemstack = this.getItemInHand(Hand.MAIN_HAND);
                        Item item = itemstack.getItem();
                        if (item instanceof SwordItem || ((IEquipment)item).canSweep()) {
                            flag3 = true;
                        }
                    }

                    float f4 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.getFireAspect(this);
                    if (p_71059_1_ instanceof LivingEntity) {
                        f4 = ((LivingEntity)p_71059_1_).getHealth();
                        if (j > 0 && !p_71059_1_.isOnFire()) {
                            flag4 = true;
                            p_71059_1_.setSecondsOnFire(1);
                        }
                    }

                    Vector3d vector3d = p_71059_1_.getDeltaMovement();
                    boolean flag5 = p_71059_1_.hurt(DamageSource.playerAttack(getPlayerEntity()), f);
                    if (flag5) {
                        if (i > 0) {
                            if (p_71059_1_ instanceof LivingEntity) {
                                ((LivingEntity)p_71059_1_).knockback((float)i * 0.5F, (double)MathHelper.sin(this.yRot * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(this.yRot * ((float)Math.PI / 180F))));
                            } else {
                                p_71059_1_.push((double)(-MathHelper.sin(this.yRot * ((float)Math.PI / 180F)) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(this.yRot * ((float)Math.PI / 180F)) * (float)i * 0.5F));
                            }

                            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                            this.setSprinting(false);
                        }

                        if (flag3) {
                            float f3 = 1.0f + EnchantmentHelper.getSweepingDamageRatio(this) * f;

                            for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, p_71059_1_.getBoundingBox().inflate(1.0D, 0.25D, 1.0D))) {
                                if (livingentity != this && livingentity != p_71059_1_ && !this.isAlliedTo(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity)livingentity).isMarker()) && this.distanceToSqr(livingentity) < 9.0D) {
                                    livingentity.knockback(0.4F, (double)MathHelper.sin(this.yRot * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(this.yRot * ((float)Math.PI / 180F))));
                                    livingentity.hurt(DamageSource.playerAttack(getPlayerEntity()), f3);
                                }
                            }

                            this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, this.getSoundSource(), 1.0F, 1.0F);
                            this.sweepAttack();
                        }

                        if (p_71059_1_ instanceof ServerPlayerEntity && p_71059_1_.hurtMarked) {
                            ((ServerPlayerEntity)p_71059_1_).connection.send(new SEntityVelocityPacket(p_71059_1_));
                            p_71059_1_.hurtMarked = false;
                            p_71059_1_.setDeltaMovement(vector3d);
                        }

                        if (flag2) {
                            this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, this.getSoundSource(), 1.0F, 1.0F);
                            this.crit(p_71059_1_);
                        }

                        if (!flag2 && !flag3) {
                            if (flag) {
                                this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, this.getSoundSource(), 1.0F, 1.0F);
                            } else {
                                this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, this.getSoundSource(), 1.0F, 1.0F);
                            }
                        }

                        if (f1 > 0.0F) {
                            this.magicCrit(p_71059_1_);
                        }

                        this.setLastHurtMob(p_71059_1_);
                        if (p_71059_1_ instanceof LivingEntity) {
                            EnchantmentHelper.doPostHurtEffects((LivingEntity)p_71059_1_, this);
                        }

                        EnchantmentHelper.doPostDamageEffects(this, p_71059_1_);
                        ItemStack itemstack1 = this.getMainHandItem();
                        Entity entity = p_71059_1_;
                        if (p_71059_1_ instanceof net.minecraftforge.entity.PartEntity) {
                            entity = ((net.minecraftforge.entity.PartEntity<?>) p_71059_1_).getParent();
                        }

                        if (!this.level.isClientSide && !itemstack1.isEmpty() && entity instanceof LivingEntity) {
                            ItemStack copy = itemstack1.copy();
                            itemstack1.hurtEnemy((LivingEntity)entity, getPlayerEntity());
                            if (itemstack1.isEmpty()) {
                                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(getPlayerEntity(), copy, Hand.MAIN_HAND);
                                this.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }

                        if (p_71059_1_ instanceof LivingEntity) {
                            float f5 = f4 - ((LivingEntity)p_71059_1_).getHealth();
                            this.awardStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                            if (j > 0) {
                                p_71059_1_.setSecondsOnFire(j * 4);
                            }

                            if (this.level instanceof ServerWorld && f5 > 2.0F) {
                                int k = (int)((double)f5 * 0.5D);
                                ((ServerWorld)this.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR, p_71059_1_.getX(), p_71059_1_.getY(0.5D), p_71059_1_.getZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }

                        this.causeFoodExhaustion(0.1F);
                    } else {
                        this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, this.getSoundSource(), 1.0F, 1.0F);
                        if (flag4) {
                            p_71059_1_.clearFire();
                        }
                    }
                }

            }
        }
    }

    public float getCurrentItemAttackStrengthDelay() {
        if(DualWieldItem.isDualWielding(getPlayerEntity())){
            return (float)(1.0D / this.getAttributeValue(Attributes.ATTACK_SPEED) * 10.0D);
        }
        return (float)(1.0D / this.getAttributeValue(Attributes.ATTACK_SPEED) * 20.0D);
    }

    private PlayerEntity getPlayerEntity(){
        return (PlayerEntity)(Object)this;
    }
}
