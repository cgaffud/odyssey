package com.bedmen.odyssey.entity;

import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.PermanentBuffsPacket;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber
public class PlayerEntityEvents {

    @SubscribeEvent
    public static void onPreTick(final TickEvent.PlayerTickEvent event){
        PlayerEntity playerEntity =  event.player;
        if(!playerEntity.level.isClientSide && event.phase == TickEvent.Phase.START){
            //Sets player on fire unless they are nether immune
            if(!(playerEntity.abilities.instabuild || playerEntity.isSpectator()) && playerEntity.level.dimensionType().ultraWarm()){
                if(!((IPlayerPermanentBuffs) playerEntity).getNetherImmune())
                    playerEntity.setSecondsOnFire(1);
            }
        }
    }

    @SubscribeEvent
    public static void onJoin(final EntityJoinWorldEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof IPlayerPermanentBuffs && !event.getWorld().isClientSide){
            PacketDistributor.PacketTarget packetTarget = PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity);
            OdysseyNetwork.CHANNEL.send(packetTarget, new PermanentBuffsPacket(((IPlayerPermanentBuffs) entity).getNetherImmune()));
        }
    }
}
