package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {

    @SubscribeEvent
    public static void entityJoinWorldEventListener(final AttackEntityEvent event){
        Player player = (Player) event.getEntity();
        int shatteringLevel = EnchantmentUtil.getShattering(player);
        Entity target = event.getTarget();
        if(!player.level.isClientSide && target instanceof LivingEntity && player.getAttackStrengthScale(0.5F) > 0.9f && shatteringLevel > 0){
            LivingEntity livingTarget = (LivingEntity)target;
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

//    @SubscribeEvent
//    public static void entityJoinWorldEventListener(final EntityJoinWorldEvent event){
//        if(!event.getWorld().isClientSide){
//            Entity entity = event.getEntity();
//
//            //Update Max Health Attribute for Player
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
//
//            //Skeleton
//            if(entity instanceof Skeleton){
//                reassessSkeletonWeaponGoal((Skeleton)entity);
//            }
//        }
//    }

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

//    @SubscribeEvent
//    public static void livingSpawnEvent$SpecialSpawnListener(final LivingSpawnEvent.SpecialSpawn event){
//        Entity entity = event.getEntity();
//        if(entity instanceof Skeleton){
//            Skeleton skeletonEntity = (Skeleton)entity;
//            Random random = skeletonEntity.getRandom();
//
//            if(random.nextFloat() < ForgeConfig.SERVER.zombieBabyChance.get()){
//                EntityTypeRegistry.BABY_SKELETON.get().spawn((ServerLevel)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), event.getSpawnReason(), true, true);
//                event.setCanceled(true);
//                return;
//            }
//
//            if(skeletonEntity.getRandom().nextFloat() < 0.05f){
//                entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.BOWN.get()));
//            }
//        }
//
//        else if(entity instanceof Creeper){
//            Creeper creeperEntity = (Creeper)entity;
//            Random random = creeperEntity.getRandom();
//
//            if(random.nextFloat() < getCamoChance(creeperEntity.level.getDifficulty())){
//                EntityTypeRegistry.CAMO_CREEPER.get().spawn((ServerLevel)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), event.getSpawnReason(), true, true);
//                event.setCanceled(true);
//                return;
//            }
//
//            if(random.nextFloat() < ForgeConfig.SERVER.zombieBabyChance.get()){
//                EntityTypeRegistry.BABY_CREEPER.get().spawn((ServerLevel)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), event.getSpawnReason(), true, true);
//                event.setCanceled(true);
//                return;
//            }
//        }
//    }
//
//    public static void reassessSkeletonWeaponGoal(Skeleton skeletonEntity) {
//        if (skeletonEntity.level != null && !skeletonEntity.level.isClientSide) {
//            skeletonEntity.goalSelector.removeGoal(skeletonEntity.meleeGoal);
//            skeletonEntity.goalSelector.removeGoal(skeletonEntity.bowGoal);
//            ItemStack itemstack = skeletonEntity.getItemInHand(BowUtil.getHandHoldingBow(skeletonEntity));
//            Item item = itemstack.getItem();
//            if (item instanceof OdysseyBowItem) {
//                int i = ((OdysseyBowItem) item).getChargeTime(itemstack);
//                if (itemstack.getItem() == ItemRegistry.BOW.get() && skeletonEntity.level.getDifficulty() != Difficulty.HARD) {
//                    i = 40;
//                }
//                OdysseyRangedBowAttackGoal<AbstractSkeleton> odysseyBowGoal = new OdysseyRangedBowAttackGoal<AbstractSkeleton>(skeletonEntity, 1.0D, i, 15.0F);
//
//                skeletonEntity.goalSelector.addGoal(4, odysseyBowGoal);
//            } else {
//                skeletonEntity.goalSelector.addGoal(4, skeletonEntity.meleeGoal);
//            }
//        }
//    }
//
//    public static float getCamoChance(Difficulty difficulty){
//        switch(difficulty){
//            case HARD:
//                return 1f;
//            case NORMAL:
//                return 0.5f;
//            default:
//                return 0f;
//        }
//    }
}
