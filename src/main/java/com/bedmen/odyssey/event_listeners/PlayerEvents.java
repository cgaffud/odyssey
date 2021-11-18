package com.bedmen.odyssey.event_listeners;


import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.SneakingPacket;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {

    /**
     * Sets player on fire unless they are nether immune
     * Increases max health when eating life fruit
     */
    @SubscribeEvent
    public static void tickEvent$PlayerTickEventListener(final TickEvent.PlayerTickEvent event){
        Player playerEntity =  event.player;
        if(!playerEntity.level.isClientSide){
            if(event.phase == TickEvent.Phase.START){
                if(!(playerEntity.isCreative() || playerEntity.isSpectator()) && playerEntity.level.dimensionType().ultraWarm()){
                    if(!EnchantmentUtil.hasFireProtectionOrResistance(playerEntity))
                        playerEntity.setSecondsOnFire(1);
                }
                //Todo life fruits
//                if(playerEntity.hasEffect(EffectRegistry.LIFE_INCREASE.get())){
//                    IOdysseyPlayer playerPermanentBuffs = (IOdysseyPlayer)playerEntity;
//                    playerPermanentBuffs.incrementLifeFruits();
//                    AttributeInstance modifiableattributeinstance = playerEntity.getAttributes().getInstance(Attributes.MAX_HEALTH);
//                    if (modifiableattributeinstance != null) {
//                        modifiableattributeinstance.setBaseValue(20.0d + 2.0d * playerPermanentBuffs.getLifeFruits());
//                        playerEntity.setHealth(playerEntity.getHealth()+2.0f);
//                    }
//                }
            } else if(event.phase == TickEvent.Phase.END) {
                if (EnchantmentUtil.hasTurtling(playerEntity)) {
                    if (playerEntity.level.isClientSide)
                        OdysseyNetwork.CHANNEL.sendToServer(new SneakingPacket(playerEntity.isShiftKeyDown()));
                    else if (playerEntity.isShiftKeyDown()) {
                        playerEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1, 1, false, false, true));
                    }
                }
            }
        }
    }

    /**
     * Sets player health to the appropriate level upon respawn based don life fruits eaten
     */
    @SubscribeEvent
    public static void PlayerEvent$PlayerRespawnEventListener(final PlayerEvent.PlayerRespawnEvent event){
        //Todo life fruits
//        Player playerEntity =  event.getPlayer();
//        if(playerEntity instanceof IOdysseyPlayer && !playerEntity.level.isClientSide){
//            playerEntity.setHealth(20.0f + 2.0f * ((IOdysseyPlayer) playerEntity).getLifeFruits());
//        }
    }
}
