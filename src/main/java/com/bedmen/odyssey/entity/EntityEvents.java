package com.bedmen.odyssey.entity;

import com.bedmen.odyssey.entity.player.IPlayerPermanentBuffs;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.PermanentBuffsPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class EntityEvents {
    @SubscribeEvent
    public static void onJoin(final EntityJoinWorldEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof IPlayerPermanentBuffs && !event.getWorld().isClientSide){
            IPlayerPermanentBuffs playerPermanentBuffs = (IPlayerPermanentBuffs)entity;
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
    }
}
