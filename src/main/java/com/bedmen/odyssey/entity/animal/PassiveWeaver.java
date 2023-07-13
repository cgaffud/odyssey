package com.bedmen.odyssey.entity.animal;

import com.bedmen.odyssey.recipes.object.WeavingRecipe;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
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
import java.util.Random;

public class PassiveWeaver extends Animal {
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CARROT, Items.POTATO, Items.BEETROOT);
    private static final Map<Item, Block> WEAVE_MAP = new HashMap<>();
    private static final int MAX_STRING = 3;
    private static final float STRING_CHANCE = 0.05f;
    private static final int STRING_REPLENISH_TIME = 600;
    private static final int MAX_STRING_TIMER = STRING_REPLENISH_TIME * MAX_STRING;
    private static final String STRING_TIMER_TAG = "StringTimer";
    private int stringTimer;

    public PassiveWeaver(EntityType<? extends PassiveWeaver> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(true);
        if(WEAVE_MAP.isEmpty()){
            Collection<WeavingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.WEAVING.get());
            for(WeavingRecipe recipe : recipes){
                Item item = recipe.getResultItem().getItem();
                if(!(item instanceof BlockItem blockItem)){
                    continue;
                }
                Block block = blockItem.getBlock();
                for(ItemStack itemStack : recipe.getIngredient().getItems()){
                    Item item1 = itemStack.getItem();
                    WEAVE_MAP.put(item1, block);
                }
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
        return this.getBbHeight() * 0.5F;
    }

    public void tick() {
        super.tick();
        if(this.random.nextFloat() < STRING_CHANCE && this.stringTimer < MAX_STRING_TIMER){
            this.stringTimer++;
        }
    }

    private void spawnEatingParticles(ItemStack itemStack, int count) {
        for(int i = 0; i < count; ++i) {
            Vec3 vec3 = new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3 = vec3.xRot(-this.getXRot() * ((float)Math.PI / 180F));
            vec3 = vec3.yRot(-this.getYRot() * ((float)Math.PI / 180F));
            double d0 = ((double)this.random.nextFloat() - 0.5D) * 0.2D;
            Vec3 vec31 = new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.2D, d0, 0);
            vec31 = vec31.add(this.getX(), this.getEyeY(), this.getZ());
            if (this.level instanceof ServerLevel) //Forge: Fix MC-2518 spawnParticle is nooped on server, need to use server specific variant
                ((ServerLevel)this.level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemStack), vec31.x, vec31.y, vec31.z, 1, vec3.x, vec3.y + 0.05D, vec3.z, 0.0D);
            else
                this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemStack), vec31.x, vec31.y, vec31.z, vec3.x, vec3.y + 0.05D, vec3.z);
        }

    }

    public int getStringTimer(){
        return this.stringTimer;
    }

    public void decrementStringTimer(){
        this.stringTimer -= STRING_REPLENISH_TIME;
    }

    public boolean wantsToPickUp(ItemStack itemStack) {
        if(this.isBaby()){
            return false;
        }
        Item item = itemStack.getItem();
        return ((WEAVE_MAP.containsKey(item) && canAddItemStack(itemStack)) || item == Items.STRING);
    }

    protected boolean canAddItemStack(ItemStack itemStack){
        ItemStack currentlyHeldItemStack = this.getMainHandItem();
        if(currentlyHeldItemStack.isEmpty()){
            return true;
        }
        boolean canAddToStack = currentlyHeldItemStack.getCount() < currentlyHeldItemStack.getMaxStackSize();
        return ItemStack.isSameItemSameTags(itemStack, currentlyHeldItemStack) && canAddToStack;
    }

    // Returns what remains of the input stack
    protected ItemStack addItemStackToMainHand(ItemStack inputStack){
        ItemStack mainHandItemStack = this.getMainHandItem();
        if(mainHandItemStack.isEmpty()){
            this.setItemInHand(InteractionHand.MAIN_HAND, inputStack);
            return ItemStack.EMPTY;
        }
        int inputCount = inputStack.getCount();
        int currentCount = mainHandItemStack.getCount();
        int maxCount = mainHandItemStack.getMaxStackSize();
        if(inputCount + currentCount > maxCount){
            mainHandItemStack.setCount(maxCount);
            inputStack.setCount(inputCount - (maxCount - currentCount));
        }
        mainHandItemStack.grow(inputCount);
        return ItemStack.EMPTY;
    }

    protected void pickUpItem(ItemEntity itemEntity) {
        ItemStack itemStack = itemEntity.getItem();
        if (this.wantsToPickUp(itemStack)) {
            this.onItemPickup(itemEntity);
            if(itemStack.is(Items.STRING)){
                itemStack.shrink(1);
                this.stringTimer += STRING_REPLENISH_TIME;
                if (itemStack.isEmpty()) {
                    itemEntity.discard();
                }
            } else {
                itemEntity.setItem(addItemStackToMainHand(itemStack));
            }
        }

    }

    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (this.wantsToPickUp(itemStack)) {
            if(itemStack.is(Items.STRING)){
                this.usePlayerItem(player, interactionHand, itemStack);
                this.stringTimer += STRING_REPLENISH_TIME;
            } else {
                player.setItemInHand(interactionHand, addItemStackToMainHand(itemStack));
            }
        } else if (itemStack.isEmpty() && !this.getMainHandItem().isEmpty()) {
            player.setItemInHand(interactionHand, this.getMainHandItem());
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        } else {
            return super.mobInteract(player, interactionHand);
        }
        return InteractionResult.SUCCESS;
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
        compoundTag.putInt(STRING_TIMER_TAG, this.stringTimer);
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.stringTimer = compoundTag.getInt(STRING_TIMER_TAG);
    }

    protected void dropEquipment() {
        super.dropEquipment();
        ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!itemstack.isEmpty()) {
            this.spawnAtLocation(itemstack);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
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
            return (!this.passiveWeaver.getMainHandItem().isEmpty() && stringTimer >= PassiveWeaver.STRING_REPLENISH_TIME) || stringTimer >= MAX_STRING_TIMER;
        }

        public void start() {
            this.weaveTime = 0;
        }

        public void tick() {
            ItemStack itemStack = this.passiveWeaver.getMainHandItem();
            if(this.weaveTime < MAX_WEAVE_TIME){
                this.weaveTime++;
                if(this.weaveTime % 2 == 0 && !itemStack.isEmpty()){
                    this.passiveWeaver.spawnEatingParticles(itemStack, 5);
                    RandomSource randomSource = this.passiveWeaver.random;
                    this.passiveWeaver.playSound(this.passiveWeaver.getEatingSound(itemStack), 0.25F + 0.25F * (float)randomSource.nextInt(2), (randomSource.nextFloat() - randomSource.nextFloat()) * 0.4F + 2.0F);
                }
            } else {
                BlockPos blockPos = this.passiveWeaver.blockPosition();
                if(this.passiveWeaver.level.getBlockState(blockPos).isAir()){
                    if(itemStack.isEmpty()){
                        this.passiveWeaver.level.setBlock(blockPos, Blocks.COBWEB.defaultBlockState(), 3);
                    } else {
                        this.passiveWeaver.level.setBlock(blockPos, WEAVE_MAP.get(itemStack.getItem()).defaultBlockState(), 3);
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
