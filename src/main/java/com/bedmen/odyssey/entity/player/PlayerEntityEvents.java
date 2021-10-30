package com.bedmen.odyssey.entity.player;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
                IPlayerPermanentBuffs playerPermanentBuffs = (IPlayerPermanentBuffs)playerEntity;
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
    public static void onPlayerRespawnEvent(final PlayerEvent.PlayerRespawnEvent event){
        PlayerEntity playerEntity =  event.getPlayer();
        if(playerEntity instanceof IPlayerPermanentBuffs && !playerEntity.level.isClientSide){
            playerEntity.setHealth(20.0f + 2.0f * ((IPlayerPermanentBuffs) playerEntity).getLifeFruits());
        }
    }
}
