package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.entity.IRotationallyIncompetent;
import com.bedmen.odyssey.entity.player.IPlayerPermanentBuffs;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.MineralLeviathanPacket;
import com.bedmen.odyssey.network.packet.PermanentBuffsPacket;
import com.bedmen.odyssey.network.packet.UpdateEntityRotationPacket;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.BossUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod.EventBusSubscriber
public class MineralLeviathanPartEntity extends MonsterEntity implements IRotationallyIncompetent, IMineralLeviathanSegment {
    private float trueYRot;
    private float trueXRot;
    public  MineralLeviathanEntity head;
    public IMineralLeviathanSegment prevSegment;

    public MineralLeviathanPartEntity(EntityType<? extends MineralLeviathanPartEntity> entityType, World world) {
        this(entityType, world, null, null);
    }

    public MineralLeviathanPartEntity(EntityType<? extends MineralLeviathanPartEntity> entityType, World world, MineralLeviathanEntity head, IMineralLeviathanSegment prevSegment) {
        super(entityType, world);
        this.setHealth(this.getMaxHealth());
        this.noPhysics = true;
        this.setNoGravity(true);
        this.head = head;
        this.prevSegment = prevSegment;
    }

    public void tick() {
        this.setNoGravity(true);
        this.noPhysics = true;
        super.tick();
    }

    public void aiStep() {
        if (this.level.isClientSide) {
            if (!this.isSilent()) {
                //Player Sounds Here
            }
        }

        if(!this.isNoAi()){
            if(!this.level.isClientSide){
                //Movement
                Vector3d prevSegmentPosition = this.prevSegment.getPosition(1.0f);
                Vector3d movement = prevSegmentPosition.subtract(this.getPosition(1.0f));
                if(movement.length() > 1.8d){
                    Vector3d newPosition = movement.normalize().scale(-1.75d).add(prevSegmentPosition);
                    this.setPos(newPosition.x, newPosition.y, newPosition.z);
                }
                this.setRotation(movement);
                OdysseyNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new UpdateEntityRotationPacket(this.trueYRot, this.trueXRot, this.getId()));

                //Damage
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.getX()-1.0d,this.getY(),this.getZ()-1.0d,this.getX()+1.0d,this.getY()+2.0d,this.getZ()+1.0d);
                List<LivingEntity> livingEntityList =  this.level.getEntitiesOfClass(LivingEntity.class, axisAlignedBB);
                for(LivingEntity livingEntity : livingEntityList){
                    if(!(livingEntity instanceof MineralLeviathanEntity) && !(livingEntity instanceof MineralLeviathanPartEntity) && this.isAlive()){
                        livingEntity.hurt(DamageSource.mobAttack(this), (float)this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE) * BossUtil.difficultMultiplier(this.level.getDifficulty()));
                    }
                }
            }
        }
        super.aiStep();
    }

    protected void setRotation(Vector3d vector3d) {
        float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(vector3d));
        if (vector3d.lengthSqr() != 0.0D) {
            this.trueYRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
            this.trueXRot = (float)(MathHelper.atan2(f, vector3d.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getYRot() {
        return this.trueYRot;
    }

    @OnlyIn(Dist.CLIENT)
    public float getXRot() {
        return this.trueXRot;
    }

    public void setYRot(float f) {
        this.trueYRot = f;
    }

    public void setXRot(float f) {
        this.trueXRot = f;
    }

    public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
    }

    public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
    }

    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.remove();
        } else {
            this.noActionTime = 0;
        }
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        if(this.head != null && this.head.isAlive()){
            return this.head.hurt(damageSource, amount);
        } else {
            return super.hurt(damageSource, amount);
        }
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEFINED;
    }

    public boolean canChangeDimensions() {
        return false;
    }

    protected boolean canRide(Entity p_184228_1_) {
        return false;
    }

    public boolean addEffect(EffectInstance p_195064_1_) {
        return false;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 1.0D).add(Attributes.ATTACK_DAMAGE, MineralLeviathanEntity.DAMAGE * 0.5d);
    }
}
