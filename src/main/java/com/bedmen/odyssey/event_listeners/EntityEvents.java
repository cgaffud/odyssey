package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.ai.OdysseyRangedBowAttackGoal;
import com.bedmen.odyssey.entity.animal.OdysseyPolarBear;
import com.bedmen.odyssey.entity.monster.BabySkeleton;
import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.items.OdysseyBowItem;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.BowUtil;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.Random;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {

    @SubscribeEvent
    public static void updateEntityEventListener(final LivingEvent.LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
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
    }

    @SubscribeEvent
    public static void attackEntityEventListener(final AttackEntityEvent event){
        Player player = (Player) event.getEntity();
        int shatteringLevel = EnchantmentUtil.getShattering(player);
        Entity target = event.getTarget();
        if(!player.level.isClientSide && target instanceof LivingEntity livingTarget && player.getAttackStrengthScale(0.5F) > 0.9f && shatteringLevel > 0){
            MobEffectInstance effectInstance = livingTarget.getEffect(EffectRegistry.SHATTERED.get());
            if(effectInstance != null){
                livingTarget.removeEffect(EffectRegistry.SHATTERED.get());
                int amp = effectInstance.getAmplifier();
                effectInstance = new MobEffectInstance(EffectRegistry.SHATTERED.get(), 80 + shatteringLevel * 20, Integer.min(amp+1,1+2*shatteringLevel), false, true, true);
            } else {
                effectInstance = new MobEffectInstance(EffectRegistry.SHATTERED.get(), 80 + shatteringLevel * 20, 0, false, true, true);
            }
            livingTarget.addEffect(effectInstance);
        }
    }

    @SubscribeEvent
    public static void LivingHurtEventListener(final LivingHurtEvent event){
        float amount = event.getAmount();
        LivingEntity livingEntity = event.getEntityLiving();
        DamageSource damageSource = event.getSource();
        if(amount >= 10.0f && livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemRegistry.HOLLOW_COCONUT.get() && damageSource != DamageSource.FALL){
            Consumer<LivingEntity> consumer = (p_233653_0_) -> {
                p_233653_0_.broadcastBreakEvent(EquipmentSlot.HEAD);
            };
            ItemStack itemStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
            Item item = itemStack.getItem();
            item.damageItem(itemStack, 100, livingEntity, consumer);
            consumer.accept(livingEntity);
            itemStack.shrink(1);
            if(livingEntity instanceof Player player){
                player.awardStat(Stats.ITEM_BROKEN.get(item));
            }
        }
    }

    @SubscribeEvent
    public static void entityJoinWorldEventListener(final EntityJoinWorldEvent event){
        if(!event.getWorld().isClientSide){
            Entity entity = event.getEntity();

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

            //Skeleton
            if(entity.getType() == EntityType.SKELETON){
                reassessSkeletonWeaponGoal((Skeleton)entity);
            }
        }
    }

//Attempt at moving Building Fatigue out of Mixins
//    @SubscribeEvent
//    public static void BlockEvent$EntityPlaceEventListener(final BlockEvent.EntityPlaceEvent event){
//        Entity entity = event.getEntity();
//        if(entity instanceof LivingEntity){
//            LivingEntity livingEntity = (LivingEntity)entity;
//            EffectInstance effectInstance = livingEntity.getEffect(EffectRegistry.BUILDING_FATIGUE.get());
//            if(effectInstance != null){
//                if(entity instanceof ServerPlayerEntity){
//                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entity;
//                    Hand hand = serverPlayerEntity.getUsedItemHand();
//                    serverPlayerEntity.setItemInHand(hand, serverPlayerEntity.getItemInHand(hand));
//                    serverPlayerEntity.broadcastCarriedItem();
//                }
//                event.setCanceled(true);
//            }
//        }
//    }

    @SubscribeEvent
    public static void livingSpawnEvent$SpecialSpawnListener(final LivingSpawnEvent.SpecialSpawn event){
        Entity entity = event.getEntity();

        if(entity.getType() == EntityType.SKELETON){
            Random random = entity.level.random;

            if(random.nextFloat() < ForgeConfig.SERVER.zombieBabyChance.get()){
                EntityTypeRegistry.BABY_SKELETON.get().spawn((ServerLevel)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), event.getSpawnReason(), true, true);
                event.setCanceled(true);
                return;
            }

            if(random.nextFloat() < 0.05f){
                entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.BOWN.get()));
            }
        }

        else if(entity instanceof BabySkeleton babySkeleton){
            Random random = babySkeleton.getRandom();
            if(random.nextFloat() < 0.2f){
                entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.BONERANG.get()));
            }
        }

        else if(entity.getType() == EntityType.CREEPER){
            Random random = entity.level.getRandom();

            if(random.nextFloat() < getCamoChance(entity.level.getDifficulty())){
                EntityTypeRegistry.CAMO_CREEPER.get().spawn((ServerLevel)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), event.getSpawnReason(), true, true);
                event.setCanceled(true);
            }

            else if(random.nextFloat() < ForgeConfig.SERVER.zombieBabyChance.get()){
                EntityTypeRegistry.BABY_CREEPER.get().spawn((ServerLevel)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), event.getSpawnReason(), true, true);
                event.setCanceled(true);
            }
        }

        else if(entity.getType() == EntityType.POLAR_BEAR){
            OdysseyPolarBear odysseyPolarBear = (OdysseyPolarBear) EntityTypeRegistry.POLAR_BEAR.get().spawn((ServerLevel)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), event.getSpawnReason(), true, true);
            odysseyPolarBear.setBaby(((PolarBear)entity).isBaby());
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void lootingLevelEventListener(final LootingLevelEvent event){
        DamageSource damageSource = event.getDamageSource();
        if(damageSource != null){
            Entity directEntity = damageSource.getDirectEntity();
            if(directEntity instanceof OdysseyAbstractArrow){
                event.setLootingLevel(((OdysseyAbstractArrow) directEntity).getLootingLevel());
            }
        }
    }


    public static void reassessSkeletonWeaponGoal(Skeleton skeletonEntity) {
        if (skeletonEntity.level != null && !skeletonEntity.level.isClientSide) {
            skeletonEntity.goalSelector.removeGoal(skeletonEntity.meleeGoal);
            skeletonEntity.goalSelector.removeGoal(skeletonEntity.bowGoal);
            ItemStack itemstack = skeletonEntity.getItemInHand(BowUtil.getHandHoldingBow(skeletonEntity));
            Item item = itemstack.getItem();
            if (item instanceof OdysseyBowItem) {
                int i = ((OdysseyBowItem) item).getChargeTime(itemstack);
                if (itemstack.getItem() == ItemRegistry.BOW.get() && skeletonEntity.level.getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }
                OdysseyRangedBowAttackGoal<AbstractSkeleton> odysseyBowGoal = new OdysseyRangedBowAttackGoal<AbstractSkeleton>(skeletonEntity, 1.0D, i, 15.0F);

                skeletonEntity.goalSelector.addGoal(4, odysseyBowGoal);
            } else {
                skeletonEntity.goalSelector.addGoal(4, skeletonEntity.meleeGoal);
            }
        }
    }

    public static float getCamoChance(Difficulty difficulty){
        return switch (difficulty) {
            case HARD -> 1f;
            case NORMAL -> 0.5f;
            default -> 0.25f;
        };
    }
}
