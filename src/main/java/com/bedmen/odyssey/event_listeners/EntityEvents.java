package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.entity.ai.OdysseyRangedBowAttackGoal;
import com.bedmen.odyssey.entity.player.IOdysseyPlayer;
import com.bedmen.odyssey.items.OdysseyBowItem;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.PermanentBuffsPacket;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.BowUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Random;

public class EntityEvents {

    @SubscribeEvent
    public static void entityJoinWorldEventListener(final EntityJoinWorldEvent event){
        if(!event.getWorld().isClientSide){
            Entity entity = event.getEntity();

            //Update Max Health Attribute for Player
            if(entity instanceof IOdysseyPlayer){
                IOdysseyPlayer playerPermanentBuffs = (IOdysseyPlayer)entity;
                PlayerEntity playerEntity = (PlayerEntity)entity;
                PacketDistributor.PacketTarget packetTarget = PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity);
                int lifeFruits = playerPermanentBuffs.getLifeFruits();
                //Sync Client
                OdysseyNetwork.CHANNEL.send(packetTarget, new PermanentBuffsPacket(lifeFruits));
                //Increase max health
                ModifiableAttributeInstance modifiableattributeinstance = playerEntity.getAttributes().getInstance(Attributes.MAX_HEALTH);
                if (modifiableattributeinstance != null) {
                    modifiableattributeinstance.setBaseValue(20.0d + 2.0d*lifeFruits);
                }
            }

            //Skeleton
            if(entity instanceof SkeletonEntity){
                reassessSkeletonWeaponGoal((SkeletonEntity)entity);
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
        if(entity instanceof SkeletonEntity){
            SkeletonEntity skeletonEntity = (SkeletonEntity)entity;
            Random random = skeletonEntity.getRandom();

            if(random.nextFloat() < ForgeConfig.SERVER.zombieBabyChance.get()){
                EntityTypeRegistry.BABY_SKELETON.get().spawn((ServerWorld)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), event.getSpawnReason(), true, true);
                event.setCanceled(true);
                return;
            }

            if(skeletonEntity.getRandom().nextFloat() < 1f){
                entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ItemRegistry.BOWN.get()));
            }
        }
    }

    public static void reassessSkeletonWeaponGoal(SkeletonEntity skeletonEntity) {
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
                OdysseyRangedBowAttackGoal<AbstractSkeletonEntity> odysseyBowGoal = new OdysseyRangedBowAttackGoal<AbstractSkeletonEntity>(skeletonEntity, 1.0D, i, 15.0F);

                skeletonEntity.goalSelector.addGoal(4, odysseyBowGoal);
            } else {
                skeletonEntity.goalSelector.addGoal(4, skeletonEntity.meleeGoal);
            }
        }
    }
}
