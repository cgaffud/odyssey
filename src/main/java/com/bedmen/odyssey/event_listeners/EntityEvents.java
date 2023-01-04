package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AdditiveConditionalMeleeAspect;
import com.bedmen.odyssey.entity.IOdysseyLivingEntity;
import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.items.innate_aspect_items.InnateAspectItem;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.JumpKeyPressedPacket;
import com.bedmen.odyssey.registry.BiomeRegistry;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.tags.OdysseyEntityTags;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {

    @SubscribeEvent
    public static void onLivingUpdateEvent(final LivingEvent.LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        //For Gliding Armor
        if(livingEntity instanceof IOdysseyLivingEntity odysseyLivingEntity){
            odysseyLivingEntity.setFlightLevels(EnchantmentUtil.hasSlowFalling(livingEntity), EnchantmentUtil.getGliding(livingEntity));
            if(livingEntity.level.isClientSide && livingEntity.jumping && odysseyLivingEntity.hasSlowFalling() && livingEntity.getDeltaMovement().y < -0.1d && odysseyLivingEntity.getFlightTicks() <= odysseyLivingEntity.getMaxFlightTicks()){
                odysseyLivingEntity.incrementFlightTicks(20);
                OdysseyNetwork.CHANNEL.sendToServer(new JumpKeyPressedPacket());
            } else if(livingEntity.isOnGround()) {
                odysseyLivingEntity.decrementFlightTicks();
            }
        }

        if (!livingEntity.level.isClientSide && livingEntity.isAlive()) {
            int bleedLvl = EnchantmentUtil.getBleeding(livingEntity);
            int heavyLvl = EnchantmentUtil.getHeavy(livingEntity);
            int drowningLvl = EnchantmentUtil.getDrowning(livingEntity);

            if (bleedLvl > 0)
                livingEntity.addEffect(new MobEffectInstance(EffectRegistry.BLEEDING.get(), 2,
                        bleedLvl-1,false, false, false));
            if (heavyLvl > 0)
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, heavyLvl-1
                ,false, false, false));
            if (drowningLvl > 0)
                livingEntity.addEffect(new MobEffectInstance(EffectRegistry.DROWNING.get(), 2, drowningLvl-1
                        ,false,false,false));
        }

        //For Drowning
        if(!livingEntity.level.isClientSide && livingEntity.hasEffect(EffectRegistry.DROWNING.get())){
            MobEffectInstance mobEffectInstance = livingEntity.getEffect(EffectRegistry.DROWNING.get());
            int air = livingEntity.getAirSupply();
            int reduction = mobEffectInstance.getAmplifier();
            //If the entity is taking drown damage, if can only take damage once per half second anyways.
            //Odd multiples of drown amounts actually cause drown damage less frequently than even, which hit every half second
            //Therefore if the entity is drowning we just max the drown amount at 2
            //Note that the 1st drown amount is dealt through the LivingEntity baseTick code, so we max reduction here at 1.
            if(air <= 0){
                reduction = Integer.min(reduction, 1);
            }
            for(int i = 0; i < reduction; i++){
                air = livingEntity.decreaseAirSupply(air);
            }
            livingEntity.setAirSupply(air);
        }
    }

    @SubscribeEvent
    public static void onLivingHurtEvent(final LivingHurtEvent event){
        float amount = event.getAmount();
        LivingEntity hurtLivingEntity = event.getEntityLiving();
        DamageSource damageSource = event.getSource();

        if (damageSource.getEntity() instanceof LivingEntity ) {
            LivingEntity attackingEntity = (LivingEntity) damageSource.getEntity();
            ItemStack mainHandItemStack = attackingEntity.getMainHandItem();
            Item mainHandItem = mainHandItemStack.getItem();

            // Innate Aspect Damage
            if(mainHandItem instanceof InnateAspectItem innateAspectItem){
                amount += innateAspectItem.getInnateAspectInstanceList().stream()
                        .filter(aspectInstance ->
                                aspectInstance.aspect instanceof AdditiveConditionalMeleeAspect additiveConditionalMeleeAspect
                                        && additiveConditionalMeleeAspect.livingEntityPredicate.test(hurtLivingEntity))
                        .map(aspectInstance -> aspectInstance.strength)
                        .reduce(Float::sum)
                        .orElse(0.0f);
            }

            // downpour damage booster
            int downpourLevel = EnchantmentUtil.getDownpour(attackingEntity);
            if (downpourLevel > 0 && hurtLivingEntity.getType().is(OdysseyEntityTags.HYDROPHOBIC));
                amount += (float)downpourLevel * 3f;
        }

        if(amount >= 10.0f && hurtLivingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemRegistry.HOLLOW_COCONUT.get() && damageSource != DamageSource.FALL){
            Consumer<LivingEntity> consumer = (p_233653_0_) -> {
                p_233653_0_.broadcastBreakEvent(EquipmentSlot.HEAD);
            };
            ItemStack itemStack = hurtLivingEntity.getItemBySlot(EquipmentSlot.HEAD);
            Item item = itemStack.getItem();
            item.damageItem(itemStack, 100, hurtLivingEntity, consumer);
            consumer.accept(hurtLivingEntity);
            itemStack.shrink(1);
            if(hurtLivingEntity instanceof Player player){
                player.awardStat(Stats.ITEM_BROKEN.get(item));
            }
        }
        event.setAmount(amount);
    }

    public static final Set<Integer> IDS = ConcurrentHashMap.newKeySet();

    @SubscribeEvent
    public static void onEntityJoinWorldEvent(final EntityJoinWorldEvent event){
        if(!event.getWorld().isClientSide){
            Entity entity = event.getEntity();
            if(!event.loadedFromDisk()){
                if(entity.getType() == EntityType.SKELETON){
                    IDS.add(entity.getId());
                }
            }

            //Todo lifefruits
            //Update Max Health Attribute for Player
//            if(entity instanceof IOdysseyPlayer){
//                IOdysseyPlayer playerPermanentBuffs = (IOdysseyPlayer)entity;
//                Player playerEntity = (Player)entity;
//                PacketDistributor.PacketTarget packetTarget = PacketDistributor.PLAYER.with(() -> (ServerPlayer) entity);
//                int lifeFruits = playerPermanentBuffs.getLifeFruits();
//                //Sync Client
//                OdysseyNetwork.CHANNEL.send(packetTarget, new PermanentBuffsPacket(lifeFruits));
//                //Increase max health
//                AttributeInstance modifiableattributeinstance = playerEntity.getAttributes().getInstance(Attributes.MAX_HEALTH);
//                if (modifiableattributeinstance != null) {
//                    modifiableattributeinstance.setBaseValue(20.0d + 2.0d*lifeFruits);
//                }
//            }
        }
    }

    private interface EntityReplacementFunction {
        Optional<EntityType<?>> call(Mob mob, Random random);
    }

    private static Map<EntityType<?>, EntityReplacementFunction> ENTITY_REPLACEMENT_MAP;

    public static void initEntityMap(){
        ENTITY_REPLACEMENT_MAP = Map.of(
                EntityType.SKELETON, EntityEvents::skeletonReplace,
                EntityType.CREEPER, EntityEvents::creeperReplace,
                EntityType.ZOMBIE, EntityEvents::zombieReplace,
                EntityType.SPIDER, EntityEvents::spiderReplace,
                EntityType.DROWNED, EntityEvents::zombieReplace,
                EntityType.POLAR_BEAR, EntityEvents::polarBearReplace);
    }

    @SubscribeEvent
    public static void onLivingSpawnEvent$CheckSpawn(final LivingSpawnEvent.CheckSpawn event){
        Entity entity = event.getEntity();
        Random random = entity.level.random;
        EntityType<?> entityType = entity.getType();
        MobSpawnType mobSpawnType = event.getSpawnReason();

        if(mobSpawnType == MobSpawnType.SPAWNER){
            return;
        }

        if(ENTITY_REPLACEMENT_MAP.containsKey(entityType) && entity instanceof Mob mob){
            EntityReplacementFunction entityReplacementFunction = ENTITY_REPLACEMENT_MAP.get(entityType);
            Optional<EntityType<?>> oEntityType = entityReplacementFunction.call(mob, random);
            if(oEntityType.isPresent()){
                EntityType<?> entityType1 = oEntityType.get();
                if(entityType1 != entityType){
                    entityType1.spawn((ServerLevel)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), mobSpawnType, true, true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    private static Optional<EntityType<?>> skeletonReplace(Mob mob, Random random){
        return Optional.of(EntityTypeRegistry.SKELETON.get());
    }

    private static Optional<EntityType<?>> creeperReplace(Mob mob, Random random){
        if(isCamo(random, mob.level.getDifficulty())){
            return Optional.of(EntityTypeRegistry.CAMO_CREEPER.get());
        }
        else if(isBaby(mob)){
            return Optional.of(EntityTypeRegistry.BABY_CREEPER.get());
        }
        return Optional.empty();
    }

    private static Optional<EntityType<?>> zombieReplace(Mob mob, Random random){
        if(inPrairieBiome(mob)){
            mob.setBaby(true);
            return Optional.empty();
        }
        if(random.nextFloat() < 0.05f){
            return Optional.of(EntityTypeRegistry.ZOMBIE_BRUTE.get());
        }
        return Optional.empty();
    }

    private static Optional<EntityType<?>> spiderReplace(Mob mob, Random random){
        if(inPrairieBiome(mob)){
            return Optional.of(EntityTypeRegistry.BARN_SPIDER.get());
        }
        return Optional.empty();
    }

    private static Optional<EntityType<?>> polarBearReplace(Mob mob, Random random){
        return Optional.of(EntityTypeRegistry.POLAR_BEAR.get());
    }

    public static boolean isBaby(Entity entity){
        return inPrairieBiome(entity) || entity.level.random.nextFloat() < ForgeConfig.SERVER.zombieBabyChance.get();
    }

    public static boolean inPrairieBiome(Entity entity){
        return entity.level.getBiome(entity.blockPosition()).is(BiomeRegistry.PRAIRIE_RESOURCE_KEY);
    }

    public static final Map<Difficulty, Float> DIFFICULTY_MAP = Map.of(
            Difficulty.HARD, 1f,
            Difficulty.NORMAL, 0.5f,
            Difficulty.EASY, 0.25f,
            Difficulty.PEACEFUL, 0.0f);

    public static boolean isCamo(Random random, Difficulty difficulty){
        return random.nextFloat() < DIFFICULTY_MAP.get(difficulty);
    }

    @SubscribeEvent
    public static void onLivingSpawnEvent$SpecialSpawn(final LivingSpawnEvent.SpecialSpawn event){
        Entity entity = event.getEntity();
        Random random = entity.level.random;
        EntityType<?> entityType = entity.getType();
        if(entity instanceof Zombie zombie && entityType == EntityType.ZOMBIE && zombie.getMainHandItem().isEmpty() && random.nextFloat() < 0.01){
            zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.SLEDGEHAMMER.get()));
            zombie.setDropChance(EquipmentSlot.MAINHAND, 0.5f);
        }
    }

    @SubscribeEvent
    public static void onLootingLevelEvent(final LootingLevelEvent event){
        DamageSource damageSource = event.getDamageSource();
        if(damageSource != null){
            Entity directEntity = damageSource.getDirectEntity();
            if(directEntity instanceof OdysseyAbstractArrow){
                event.setLootingLevel(((OdysseyAbstractArrow) directEntity).getLootingLevel());
            }
        }
    }

    @SubscribeEvent
    public static void onShieldBlockEvent(final ShieldBlockEvent event){
        LivingEntity livingEntity = event.getEntityLiving();
        ItemStack shield = livingEntity.getUseItem();
        if(shield.getItem() instanceof OdysseyShieldItem odysseyShieldItem){
            DamageSource damageSource = event.getDamageSource();
            event.setBlockedDamage(odysseyShieldItem.getDamageBlock(livingEntity.level.getDifficulty(), damageSource));
        }
    }
}
