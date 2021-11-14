package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BabyLeviathanEntity extends MonsterEntity {

    public BabyLeviathanEntity(EntityType<? extends BabyLeviathanEntity> p_i48550_1_, World p_i48550_2_) {
        super(p_i48550_1_, p_i48550_2_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 10.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SILVERFISH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEFINED;
    }

    public void die(DamageSource damageSource) {
        Entity entity = damageSource.getEntity();
        if(entity instanceof PlayerEntity && !this.level.isClientSide){
            EntityTypeRegistry.MINERAL_LEVIATHAN.get().spawn((ServerWorld) this.level, null, (PlayerEntity)entity, new BlockPos(entity.getX(), -5, entity.getZ()), SpawnReason.TRIGGERED, true, true);
        }
        super.die(damageSource);
    }

    protected ActionResultType mobInteract(PlayerEntity pPlayer, Hand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getItem() == Items.BUCKET && this.isAlive()) {
            this.playSound(SoundEvents.NETHERITE_BLOCK_PLACE  , 1.0F, 0.5F);
            itemstack.shrink(1);
            ItemStack itemstack1 = new ItemStack(ItemRegistry.BABY_LEVIATHAN_BUCKET.get());
            this.saveToBucketTag(itemstack1);
            if (!this.level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)pPlayer, itemstack1);
            }

            if (itemstack.isEmpty()) {
                pPlayer.setItemInHand(pHand, itemstack1);
            } else if (!pPlayer.inventory.add(itemstack1)) {
                pPlayer.drop(itemstack1, false);
            }

            this.remove();
            return ActionResultType.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    protected void saveToBucketTag(ItemStack p_204211_1_) {
        if (this.hasCustomName()) {
            p_204211_1_.setHoverName(this.getCustomName());
        }
    }

    public static boolean spawnPredicate(EntityType<? extends MonsterEntity> pType, IServerWorld pLevel, SpawnReason pReason, BlockPos pPos, Random pRandom) {
        return MonsterEntity.checkMonsterSpawnRules(pType, pLevel, pReason, pPos, pRandom) && pPos.getY() <= 24;
    }
}
