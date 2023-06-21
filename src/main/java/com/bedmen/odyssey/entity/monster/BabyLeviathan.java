package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanMaster;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Random;

public class BabyLeviathan extends Monster {

    public BabyLeviathan(EntityType<? extends BabyLeviathan> p_i48550_1_, Level p_i48550_2_) {
        super(p_i48550_1_, p_i48550_2_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 10.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
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

    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    public void die(DamageSource damageSource) {
        Entity entity = damageSource.getEntity();
        if(entity instanceof Player && !this.level.isClientSide){
            EntityTypeRegistry.MINERAL_LEVIATHAN_MASTER.get().spawn((ServerLevel) this.level, null, (Player)entity, new BlockPos(entity.getX(), Integer.max(-70, (int) (entity.getY() - MineralLeviathanMaster.FOLLOW_RANGE * 0.8d)), entity.getZ()), MobSpawnType.TRIGGERED, true, true);
        }
        super.die(damageSource);
    }

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getItem() == Items.BUCKET && this.isAlive()) {
            this.playSound(SoundEvents.NETHERITE_BLOCK_PLACE  , 1.0F, 0.5F);
            itemstack.shrink(1);
            ItemStack itemstack1 = new ItemStack(ItemRegistry.BABY_LEVIATHAN_BUCKET.get());
            this.saveToBucketTag(itemstack1);
            if (!this.level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)pPlayer, itemstack1);
            }

            if (itemstack.isEmpty()) {
                pPlayer.setItemInHand(pHand, itemstack1);
            } else if (!pPlayer.getInventory().add(itemstack1)) {
                pPlayer.drop(itemstack1, false);
            }

            this.discard();
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    protected void saveToBucketTag(ItemStack p_204211_1_) {
        if (this.hasCustomName()) {
            p_204211_1_.setHoverName(this.getCustomName());
        }
    }

    public static boolean spawnPredicate(EntityType<? extends Monster> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return Monster.checkMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) && blockPos.getY() < 0;
    }
}
