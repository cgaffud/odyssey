package com.bedmen.odyssey.entity;

import com.bedmen.odyssey.entity.ai.OdysseyRangedBowAttackGoal;
import com.bedmen.odyssey.entity.player.IOdysseyPlayer;
import com.bedmen.odyssey.items.OdysseyBowItem;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.PermanentBuffsPacket;
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
import net.minecraft.world.Difficulty;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class EntityEvents {

    @SubscribeEvent
    public static void onJoin(final EntityJoinWorldEvent event){
        Entity entity = event.getEntity();

        //Update Max Health Attribute for Player
        if(entity instanceof IOdysseyPlayer && !event.getWorld().isClientSide){
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

        else if(entity instanceof SkeletonEntity){
            SkeletonEntity skeletonEntity = (SkeletonEntity)entity;
            if(skeletonEntity.getRandom().nextFloat() < 0.5f){
                entity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ItemRegistry.BOWN.get()));
            }
            reassessSkeletonWeaponGoal(skeletonEntity);
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
