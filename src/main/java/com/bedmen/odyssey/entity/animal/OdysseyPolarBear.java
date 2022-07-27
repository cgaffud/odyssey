package com.bedmen.odyssey.entity.animal;

import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class OdysseyPolarBear extends PolarBear {

    public OdysseyPolarBear(EntityType<? extends OdysseyPolarBear> entityType, Level level) {
        super(entityType, level);
    }

    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return EntityTypeRegistry.POLAR_BEAR.get().create(serverLevel);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.targetSelector.addGoal(4, new PolarBearNearestAttackableTargetGoal(this));
    }

    public boolean doHurtTarget(Entity entity) {
        boolean flag = entity.hurt(DamageSource.mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            this.doEnchantDamageEffects(this, entity);
        }

        if(entity instanceof Pig pig && !pig.isAlive() && this.canFallInLove() && !this.isBaby()){
            this.setInLove(null);
        }

        return flag;
    }

    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (!this.isBaby() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.spawnAtLocation(ItemRegistry.POLAR_BEAR_FUR.get(), 1);
        }
    }

    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if (itemstack.is(Items.PORKCHOP)) {
            int i = this.getAge();
            if (this.isBaby()) {
                this.usePlayerItem(player, interactionHand, itemstack);
                this.ageUp((int)((float)(-i / 20) * 0.1F), true);
                this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }
        return super.mobInteract(player, interactionHand);
    }

    static class PolarBearNearestAttackableTargetGoal extends NearestAttackableTargetGoal<Pig>{

        public PolarBearNearestAttackableTargetGoal(OdysseyPolarBear odysseyPolarBear) {
            super(odysseyPolarBear, Pig.class, 10, true, true, null);
        }

        public boolean canUse() {
            return !this.mob.isBaby() && super.canUse();
        }
    }

}