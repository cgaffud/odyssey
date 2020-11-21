package entities;

import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import util.RegistryHandler;

public class RubyGolemEntity extends GolemEntity {
   protected static final DataParameter<Byte> PLAYER_CREATED = EntityDataManager.createKey(RubyGolemEntity.class, DataSerializers.BYTE);
   private int attackTimer;
   private static final RangedInteger field_234196_bu_ = TickRangeConverter.convertRange(20, 39);

   public RubyGolemEntity(EntityType<? extends RubyGolemEntity> type, World worldIn) {
      super(type, worldIn);
      this.stepHeight = 1.0F;
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
      this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
      this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
      this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
      this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_234199_0_) -> {
         return p_234199_0_ instanceof IMob && !(p_234199_0_ instanceof CreeperEntity);
      }));
   }

   protected void registerData() {
      super.registerData();
      this.dataManager.register(PLAYER_CREATED, (byte)0);
   }

   public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
      return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 150.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D).createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 20.0D);
   }

   /**
    * Decrements the entity's air supply when underwater
    */
   protected int decreaseAirSupply(int air) {
      return air;
   }

   protected void collideWithEntity(Entity entityIn) {
      if (entityIn instanceof IMob && !(entityIn instanceof CreeperEntity) && this.getRNG().nextInt(20) == 0) {
         this.setAttackTarget((LivingEntity)entityIn);
      }

      super.collideWithEntity(entityIn);
   }

   /**
    * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
    * use this to react to sunlight and start to burn.
    */
   public void livingTick() {
      super.livingTick();
      if (this.attackTimer > 0) {
         --this.attackTimer;
      }

      if (horizontalMag(this.getMotion()) > (double)2.5000003E-7F && this.rand.nextInt(5) == 0) {
         int i = MathHelper.floor(this.getPosX());
         int j = MathHelper.floor(this.getPosY() - (double)0.2F);
         int k = MathHelper.floor(this.getPosZ());
         BlockPos pos = new BlockPos(i, j, k);
         BlockState blockstate = this.world.getBlockState(pos);
         if (!blockstate.isAir(this.world, pos)) {
            this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getPosX() + ((double)this.rand.nextFloat() - 0.5D) * (double)this.getWidth(), this.getPosY() + 0.1D, this.getPosZ() + ((double)this.rand.nextFloat() - 0.5D) * (double)this.getWidth(), 4.0D * ((double)this.rand.nextFloat() - 0.5D), 0.5D, ((double)this.rand.nextFloat() - 0.5D) * 4.0D);
         }
      }

   }

   public boolean canAttack(EntityType<?> typeIn) {
      if (this.isPlayerCreated() && typeIn == EntityType.PLAYER) {
         return false;
      } else {
         return typeIn == EntityType.CREEPER ? false : super.canAttack(typeIn);
      }
   }

   public void writeAdditional(CompoundNBT compound) {
      super.writeAdditional(compound);
      compound.putBoolean("PlayerCreated", this.isPlayerCreated());
   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
   public void readAdditional(CompoundNBT compound) {
      super.readAdditional(compound);
      this.setPlayerCreated(compound.getBoolean("PlayerCreated"));
   }

   private float func_226511_et_() {
      return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
   }

   public boolean attackEntityAsMob(Entity entityIn) {
      this.attackTimer = 10;
      this.world.setEntityState(this, (byte)4);
      float f = this.func_226511_et_();
      float f1 = (int)f > 0 ? f / 2.0F + (float)this.rand.nextInt((int)f) : f;
      boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f1);
      if (flag) {
         entityIn.setMotion(entityIn.getMotion().add(0.0D, (double)0.4F, 0.0D));
         this.applyEnchantments(this, entityIn);
      }

      this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
      return flag;
   }

   /**
    * Called when the entity is attacked.
    */
   public boolean attackEntityFrom(DamageSource source, float amount) {
      RubyGolemEntity.Cracks RubyGolementity$cracks = this.func_226512_l_();
      boolean flag = super.attackEntityFrom(source, amount);
      if (flag && this.func_226512_l_() != RubyGolementity$cracks) {
         this.playSound(SoundEvents.ENTITY_IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
      }

      return flag;
   }

   public RubyGolemEntity.Cracks func_226512_l_() {
      return RubyGolemEntity.Cracks.func_226515_a_(this.getHealth() / this.getMaxHealth());
   }

   /**
    * Handler for {@link World#setEntityState}
    */
   @OnlyIn(Dist.CLIENT)
   public void handleStatusUpdate(byte id) {
      if (id == 4) {
         this.attackTimer = 10;
         this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
      } else {
         super.handleStatusUpdate(id);
      }

   }

   @OnlyIn(Dist.CLIENT)
   public int getAttackTimer() {
      return this.attackTimer;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SoundEvents.ENTITY_IRON_GOLEM_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
   }

   protected ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
      ItemStack itemstack = p_230254_1_.getHeldItem(p_230254_2_);
      Item item = itemstack.getItem();
      if (item != RegistryHandler.RUBY.get()) {
         return ActionResultType.PASS;
      } else {
         float f = this.getHealth();
         this.heal(25.0F);
         if (this.getHealth() == f) {
            return ActionResultType.PASS;
         } else {
            float f1 = 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
            this.playSound(SoundEvents.ENTITY_IRON_GOLEM_REPAIR, 1.0F, f1);
            if (!p_230254_1_.abilities.isCreativeMode) {
               itemstack.shrink(1);
            }

            return ActionResultType.func_233537_a_(this.world.isRemote);
         }
      }
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1.0F, 1.0F);
   }

   public boolean isPlayerCreated() {
	      return (this.dataManager.get(PLAYER_CREATED) & 1) != 0;
	   }

   public void setPlayerCreated(boolean playerCreated) {
      byte b0 = this.dataManager.get(PLAYER_CREATED);
      if (playerCreated) {
         this.dataManager.set(PLAYER_CREATED, (byte)(b0 | 1));
      } else {
         this.dataManager.set(PLAYER_CREATED, (byte)(b0 & -2));
      }

   }

   /**
    * Called when the mob's health reaches 0.
    */
   public void onDeath(DamageSource cause) {
      super.onDeath(cause);
   }

   public boolean isNotColliding(IWorldReader worldIn) {
      BlockPos blockpos = this.getPosition();
      BlockPos blockpos1 = blockpos.down();
      BlockState blockstate = worldIn.getBlockState(blockpos1);
      if (!blockstate.canSpawnMobs(worldIn, blockpos1, this)) {
         return false;
      } else {
         for(int i = 1; i < 3; ++i) {
            BlockPos blockpos2 = blockpos.up(i);
            BlockState blockstate1 = worldIn.getBlockState(blockpos2);
            if (!WorldEntitySpawner.func_234968_a_(worldIn, blockpos2, blockstate1, blockstate1.getFluidState(), EntityType.IRON_GOLEM)) {
               return false;
            }
         }

         return WorldEntitySpawner.func_234968_a_(worldIn, blockpos, worldIn.getBlockState(blockpos), Fluids.EMPTY.getDefaultState(), EntityType.IRON_GOLEM) && worldIn.checkNoEntityCollision(this);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public Vector3d func_241205_ce_() {
      return new Vector3d(0.0D, (double)(0.875F * this.getEyeHeight()), (double)(this.getWidth() * 0.4F));
   }

   public static enum Cracks {
      NONE(1.0F),
      LOW(0.75F),
      MEDIUM(0.5F),
      HIGH(0.25F);

      private static final List<RubyGolemEntity.Cracks> field_226513_e_ = Stream.of(values()).sorted(Comparator.comparingDouble((p_226516_0_) -> {
         return (double)p_226516_0_.field_226514_f_;
      })).collect(ImmutableList.toImmutableList());
      private final float field_226514_f_;

      private Cracks(float p_i225732_3_) {
         this.field_226514_f_ = p_i225732_3_;
      }

      public static RubyGolemEntity.Cracks func_226515_a_(float p_226515_0_) {
         for(RubyGolemEntity.Cracks RubyGolementity$cracks : field_226513_e_) {
            if (p_226515_0_ < RubyGolementity$cracks.field_226514_f_) {
               return RubyGolementity$cracks;
            }
         }

         return NONE;
      }
   }

}
