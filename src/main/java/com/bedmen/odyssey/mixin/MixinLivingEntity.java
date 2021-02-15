package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.NewShieldItem;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity{

    public MixinLivingEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Shadow
    public boolean getShouldBeDead() {return false;}
    @Shadow
    public boolean isPotionActive(Effect potionIn) {return false;}
    @Shadow
    public boolean isSleeping() {return false;}
    @Shadow
    public void wakeUp() {}
    @Shadow
    protected int idleTime;
    @Shadow
    public abstract ItemStack getItemStackFromSlot(EquipmentSlotType slotIn);
    @Shadow
    private boolean canBlockDamageSource(DamageSource damageSourceIn) {return false;}
    @Shadow
    protected void damageShield(float damage) {}
    @Shadow
    protected void blockUsingShield(LivingEntity entityIn) {}
    @Shadow
    public float limbSwingAmount;
    @Shadow
    protected float lastDamage;
    @Shadow
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {}
    @Shadow
    public int maxHurtTime;
    @Shadow
    public int hurtTime;
    @Shadow
    public float attackedAtYaw;
    @Shadow
    public void setRevengeTarget(@Nullable LivingEntity livingBase) {}
    @Shadow
    protected int recentlyHit;
    @Shadow
    protected PlayerEntity attackingPlayer;
    @Shadow
    public void applyKnockback(float strength, double ratioX, double ratioZ) {}
    @Shadow
    private boolean checkTotemDeathProtection(DamageSource damageSourceIn) {return false;}
    @Shadow
    protected SoundEvent getDeathSound() {return null;}
    @Shadow
    protected float getSoundVolume() {return 0.0f;}
    @Shadow
    protected float getSoundPitch() {return 0.0f;}
    @Shadow
    public void onDeath(DamageSource cause) {}
    @Shadow
    protected void playHurtSound(DamageSource source) {}
    @Shadow
    private DamageSource lastDamageSource;
    @Shadow
    private long lastDamageStamp;
    @Shadow
    protected ItemStack activeItemStack;

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!net.minecraftforge.common.ForgeHooks.onLivingAttack((LivingEntity)(Entity)this, source, amount)) return false;
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.world.isRemote) {
            return false;
        } else if (this.getShouldBeDead()) {
            return false;
        } else if (source.isFireDamage() && this.isPotionActive(Effects.FIRE_RESISTANCE)) {
            return false;
        } else {
            if (this.isSleeping() && !this.world.isRemote) {
                this.wakeUp();
            }

            this.idleTime = 0;
            float f = amount;
            if ((source == DamageSource.ANVIL || source == DamageSource.FALLING_BLOCK) && !this.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty()) {
                this.getItemStackFromSlot(EquipmentSlotType.HEAD).damageItem((int)(amount * 4.0F + this.rand.nextFloat() * amount * 2.0F), (LivingEntity)(Entity)this, (p_233653_0_) -> {
                    p_233653_0_.sendBreakAnimation(EquipmentSlotType.HEAD);
                });
                amount *= 0.75F;
            }

            boolean flag = false;
            float f1 = 0.0F;
            if (amount > 0.0F && this.canBlockDamageSource(source)) {
                this.damageShield(amount);
                f1 = amount;
                Item item = this.activeItemStack.getItem();
                if(item instanceof NewShieldItem)
                    amount -= EnchantmentUtil.getBlocking((LivingEntity)(Entity)this) * ((NewShieldItem)item).getBlock();
                if(amount < 0.0f){
                    amount = 0.0F;
                    flag = true;
                }
                if (!source.isProjectile()) {
                    Entity entity = source.getImmediateSource();
                    if (entity instanceof LivingEntity) {
                        this.blockUsingShield((LivingEntity)entity);
                    }
                }
                this.world.setEntityState(this, (byte)29);
            }

            this.limbSwingAmount = 1.5F;
            boolean flag1 = true;
            if ((float)this.hurtResistantTime > 10.0F) {
                if (amount <= this.lastDamage) {
                    return false;
                }

                this.damageEntity(source, amount - this.lastDamage);
                this.lastDamage = amount;
                flag1 = false;
            } else {
                this.lastDamage = amount;
                this.hurtResistantTime = 20;
                this.damageEntity(source, amount);
                this.maxHurtTime = 10;
                this.hurtTime = this.maxHurtTime;
            }

            this.attackedAtYaw = 0.0F;
            Entity entity1 = source.getTrueSource();
            if (entity1 != null) {
                if (entity1 instanceof LivingEntity) {
                    this.setRevengeTarget((LivingEntity)entity1);
                }

                if (entity1 instanceof PlayerEntity) {
                    this.recentlyHit = 100;
                    this.attackingPlayer = (PlayerEntity)entity1;
                } else if (entity1 instanceof net.minecraft.entity.passive.TameableEntity) {
                    net.minecraft.entity.passive.TameableEntity wolfentity = (net.minecraft.entity.passive.TameableEntity)entity1;
                    if (wolfentity.isTamed()) {
                        this.recentlyHit = 100;
                        LivingEntity livingentity = wolfentity.getOwner();
                        if (livingentity != null && livingentity.getType() == EntityType.PLAYER) {
                            this.attackingPlayer = (PlayerEntity)livingentity;
                        } else {
                            this.attackingPlayer = null;
                        }
                    }
                }
            }

            if (flag1) {
                if(flag){
                }
                else if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage()) {
                    this.world.setEntityState(this, (byte)33);
                } else {
                    byte b0;
                    if (source == DamageSource.DROWN) {
                        b0 = 36;
                    } else if (source.isFireDamage()) {
                        b0 = 37;
                    } else if (source == DamageSource.SWEET_BERRY_BUSH) {
                        b0 = 44;
                    } else {
                        b0 = 2;
                    }

                    this.world.setEntityState(this, b0);
                }

                if (source != DamageSource.DROWN && (!flag || amount > 0.0F)) {
                    this.markVelocityChanged();
                }

                if (entity1 != null) {
                    double d1 = entity1.getPosX() - this.getPosX();

                    double d0;
                    for(d0 = entity1.getPosZ() - this.getPosZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                        d1 = (Math.random() - Math.random()) * 0.01D;
                    }

                    this.attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * (double)(180F / (float)Math.PI) - (double)this.rotationYaw);
                    this.applyKnockback(0.4F, d1, d0);
                } else {
                    this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
                }
            }

            if (this.getShouldBeDead()) {
                if (!this.checkTotemDeathProtection(source)) {
                    SoundEvent soundevent = this.getDeathSound();
                    if (flag1 && soundevent != null) {
                        this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
                    }

                    this.onDeath(source);
                }
            } else if (flag1) {
                this.playHurtSound(source);
            }

            boolean flag2 = !flag || amount > 0.0F;
            if (flag2) {
                this.lastDamageSource = source;
                this.lastDamageStamp = this.world.getGameTime();
            }

            if ((LivingEntity)(Entity)this instanceof ServerPlayerEntity) {
                CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayerEntity)(Entity)this, source, f, amount, flag);
                if (f1 > 0.0F && f1 < 3.4028235E37F) {
                    ((ServerPlayerEntity)(LivingEntity)(Entity)this).addStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(f1 * 10.0F));
                }
            }

            if (entity1 instanceof ServerPlayerEntity) {
                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayerEntity)entity1, this, source, f, amount, flag);
            }

            return flag2;
        }
    }

}
