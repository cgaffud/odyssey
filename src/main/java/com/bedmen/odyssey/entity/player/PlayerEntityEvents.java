package com.bedmen.odyssey.entity.player;

import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.PermanentBuffsPacket;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
                if(!EnchantmentUtil.hasFireProtectionOrResistance(playerEntity))
                    playerEntity.setSecondsOnFire(1);
            }
            //Increases max health
            if(playerEntity.hasEffect(EffectRegistry.LIFE_INCREASE.get())){
                IOdysseyPlayer playerPermanentBuffs = (IOdysseyPlayer)playerEntity;
                playerPermanentBuffs.incrementLifeFruits();
                ModifiableAttributeInstance modifiableattributeinstance = playerEntity.getAttributes().getInstance(Attributes.MAX_HEALTH);
                if (modifiableattributeinstance != null) {
                    modifiableattributeinstance.setBaseValue(20.0d + 2.0d * playerPermanentBuffs.getLifeFruits());
                    playerEntity.setHealth(playerEntity.getHealth()+2.0f);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onJoin(final EntityJoinWorldEvent event){
        Entity entity = event.getEntity();
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
    }

    @SubscribeEvent
    public static void onPlayerRespawnEvent(final PlayerEvent.PlayerRespawnEvent event){
        PlayerEntity playerEntity =  event.getPlayer();
        if(playerEntity instanceof IOdysseyPlayer && !playerEntity.level.isClientSide){
            playerEntity.setHealth(20.0f + 2.0f * ((IOdysseyPlayer) playerEntity).getLifeFruits());
        }
    }
}
