package com.bedmen.odyssey.entity;

import com.bedmen.odyssey.enchantment.EnchantmentUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OdysseyPlayerEntity {

    @SubscribeEvent
    public static void onPreTick(final TickEvent.PlayerTickEvent event){
        PlayerEntity playerEntity =  event.player;
        if(event.phase == TickEvent.Phase.START){
            //Sets player on fire unless they have fire protection
            if(!(playerEntity.abilities.instabuild || playerEntity.isSpectator()) && playerEntity.level.dimensionType().ultraWarm()){
                if(!EnchantmentUtil.hasFireProtectionOrResistance(playerEntity))
                    playerEntity.setSecondsOnFire(1);
            }
        }
    }
}
