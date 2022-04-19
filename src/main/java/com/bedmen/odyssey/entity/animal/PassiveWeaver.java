package com.bedmen.odyssey.entity.animal;

import com.bedmen.odyssey.recipes.WeavingRecipe;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PassiveWeaver extends Animal {
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.BEETROOT);
    private final Map<Item, Block> weaveMap = new HashMap<>();
    private final SimpleContainer inventory = new SimpleContainer(1);
    private static final int MAX_STRING = 3;
    private static final float STRING_CHANCE = 0.05f;
    private static final int STRING_REPLENISH_TIME = 600;
    private static final int MAX_STRING_TIMER = STRING_REPLENISH_TIME * MAX_STRING;
    private int stringTimer;

    public PassiveWeaver(EntityType<? extends PassiveWeaver> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(true);
        Collection<WeavingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.WEAVING.get());
        for(WeavingRecipe recipe : recipes){
            Item item = recipe.getResultItem().getItem();
            if(!(item instanceof BlockItem blockItem)){
                continue;
            }
            Block block = blockItem.getBlock();
            for(ItemStack itemStack : recipe.getIngredient().getItems()){
                Item item1 = itemStack.getItem();
                weaveMap.put(item1, block);
            }
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(2, new AttackGoal(this));
        this.goalSelector.addGoal(3, new WeaveGoal(this));
        this.goalSelector.addGoal(4, new PassiveWeaverBreedGoal(this));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.2D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16D).add(Attributes.MOVEMENT_SPEED, (double)0.3F).add(Attributes.ATTACK_DAMAGE, 2D);
    }

    public double getPassengersRidingOffset() {
        return (double)(this.getBbHeight() * 0.5F);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public void tick() {
        super.tick();
        if(this.random.nextFloat() < STRING_CHANCE && this.stringTimer < MAX_STRING_TIMER){
            this.stringTimer++;
        }
    }

    public int getStringTimer(){
        return this.stringTimer;
    }

    public void decrementStringTimer(){
        this.stringTimer -= STRING_REPLENISH_TIME;
    }

    public boolean wantsToPickUp(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return ((weaveMap.containsKey(item) && this.inventory.canAddItem(itemStack)) || item == Items.STRING) && !this.isBaby();
    }

    protected void pickUpItem(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        if (this.wantsToPickUp(itemstack)) {
            this.onItemPickup(itemEntity);
            if(itemstack.is(Items.STRING)){
                itemstack.shrink(1);
                this.stringTimer += STRING_REPLENISH_TIME;
                if (itemstack.isEmpty()) {
                    itemEntity.discard();
                }
            } else {
                SimpleContainer simplecontainer = this.inventory;
                this.take(itemEntity, itemstack.getCount());
                ItemStack itemstack1 = simplecontainer.addItem(itemstack);
                if (itemstack1.isEmpty()) {
                    itemEntity.discard();
                } else {
                    itemstack.setCount(itemstack1.getCount());
                }
            }
        }

    }

    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (this.wantsToPickUp(itemStack)) {
            if(itemStack.is(Items.STRING)){
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                this.stringTimer += STRING_REPLENISH_TIME;
                this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
                return InteractionResult.SUCCESS;
            }
            ItemStack itemStack1;
            if (!player.getAbilities().instabuild) {
                itemStack1 = itemStack.split(1);
            } else {
                itemStack1 = itemStack.getItem().getDefaultInstance();
            }
            SimpleContainer simplecontainer = this.inventory;
            simplecontainer.addItem(itemStack1);
            this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, interactionHand);
        }
    }

    public void makeStuckInBlock(BlockState pState, Vec3 pMotionMultiplier) {
        if (!(pState.getBlock() instanceof WebBlock)) {
            super.makeStuckInBlock(pState, pMotionMultiplier);
        }
    }

    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 0.65F * this.getScale();
    }

    public float getScale() {
        return this.isBaby() ? 0.3F : 1.0F;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SPIDER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    public PassiveWeaver getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return EntityTypeRegistry.PASSIVE_WEAVER.get().create(serverLevel);
    }

    public boolean isFood(ItemStack itemStack) {
        return FOOD_ITEMS.test(itemStack);
    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, (double)(0.6F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.put("Inventory", this.inventory.createTag());
        compoundTag.putInt("stringTimer", this.stringTimer);
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.inventory.fromTag(compoundTag.getList("Inventory", 10));
        this.stringTimer = compoundTag.getInt("stringTimer");
    }

    static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(PassiveWeaver p_i46676_1_) {
            super(p_i46676_1_, 1.0D, true);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            if (this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget((LivingEntity)null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }

        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            double d0 = this.getAttackReachSqr(pEnemy);
            if (pDistToEnemySqr <= d0 && this.getTicksUntilNextAttack() <= 0) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(pEnemy);
                if(this.mob.getRandom().nextFloat() < 0.1f){
                    BlockPos blockPos = new BlockPos(pEnemy.getPosition(1f));
                    if(this.mob.level.getBlockState(blockPos).getBlock() == Blocks.AIR){
                        this.mob.level.setBlock(blockPos, Blocks.COBWEB.defaultBlockState(), 3);
                    }
                }
            }

        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return (double)(4.0F + pAttackTarget.getBbWidth());
        }
    }

    static class WeaveGoal extends Goal {
        private final PassiveWeaver passiveWeaver;
        private static final int MAX_WEAVE_TIME = 200;
        private int weaveTime;
        public WeaveGoal(PassiveWeaver passiveWeaver) {
            this.passiveWeaver = passiveWeaver;
        }

        public boolean canUse() {
            int stringTimer = this.passiveWeaver.getStringTimer();
            return (!this.passiveWeaver.inventory.isEmpty() && stringTimer >= PassiveWeaver.STRING_REPLENISH_TIME) || stringTimer >= MAX_STRING_TIMER;
        }

        public void start() {
            this.weaveTime = 0;
        }

        public void tick() {
            if(this.weaveTime < MAX_WEAVE_TIME){
                this.weaveTime++;
            } else {
                BlockPos blockPos = this.passiveWeaver.blockPosition();
                if(this.passiveWeaver.level.getBlockState(blockPos).isAir()){
                    if(this.passiveWeaver.inventory.isEmpty()){
                        this.passiveWeaver.level.setBlock(blockPos, Blocks.COBWEB.defaultBlockState(), 3);
                    } else {
                        ItemStack itemStack = this.passiveWeaver.inventory.getItem(0);
                        this.passiveWeaver.level.setBlock(blockPos, this.passiveWeaver.weaveMap.get(itemStack.getItem()).defaultBlockState(), 3);
                        itemStack.shrink(1);
                    }
                    this.passiveWeaver.decrementStringTimer();
                    this.weaveTime = 0;
                }
            }
        }
    }

    static class PassiveWeaverBreedGoal extends BreedGoal{
        public PassiveWeaverBreedGoal(Animal animal) {
            super(animal, 1.0d);
        }

        protected void breed() {
            BlockPos blockPos = new BlockPos(this.animal.position().add(this.partner.position()).scale(0.5d));
            BlockState blockState = this.level.getBlockState(blockPos);
            if(!blockState.is(BlockTags.WITHER_IMMUNE)){
                this.level.setBlock(blockPos, BlockRegistry.WEAVER_EGG_COBWEB.get().defaultBlockState(), 3);
                ServerPlayer serverplayer = this.animal.getLoveCause();
                if (serverplayer != null) {
                    serverplayer.awardStat(Stats.ANIMALS_BRED);
                    CriteriaTriggers.BRED_ANIMALS.trigger(serverplayer, this.animal, this.partner, null);
                    this.animal.setAge(6000);
                    this.partner.setAge(6000);
                    this.animal.resetLove();
                    this.partner.resetLove();
                    if (this.level instanceof ServerLevel serverLevel && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                        serverLevel.addFreshEntity(new ExperienceOrb(serverLevel, blockPos.getX()+0.5d, blockPos.getY()+0.5d, blockPos.getZ()+0.5d, this.animal.getRandom().nextInt(7) + 1));
                    }
                }

            }
        }
    }
}
