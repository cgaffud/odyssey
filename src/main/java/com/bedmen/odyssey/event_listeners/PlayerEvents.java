package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.player.IOdysseyPlayer;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WebBlock;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {

    /**
     * Sets player on fire unless they are nether immune
     * Increases max health when eating life fruit
     */
    @SubscribeEvent
    public static void onPlayerTickEvent(final TickEvent.PlayerTickEvent event){
        Player player =  event.player;
        //Beginning of Tick
        if(event.phase == TickEvent.Phase.START){
            //Both Sides
            if(player instanceof IOdysseyPlayer odysseyPlayer) {
                odysseyPlayer.updateSniperScoping();
            }
            //Server Side
            if(event.side == LogicalSide.SERVER){
                if(!(player.isCreative() || player.isSpectator()) && player.level.dimensionType().ultraWarm()){
                    if(!EnchantmentUtil.hasFireProtectionOrResistance(player))
                        player.setSecondsOnFire(1);
                }
                //TODO life fruits
//                if(player.hasEffect(EffectRegistry.LIFE_INCREASE.get())){
//                    IOdysseyPlayer playerPermanentBuffs = (IOdysseyPlayer)player;
//                    playerPermanentBuffs.incrementLifeFruits();
//                    AttributeInstance modifiableattributeinstance = player.getAttributes().getInstance(Attributes.MAX_HEALTH);
//                    if (modifiableattributeinstance != null) {
//                        modifiableattributeinstance.setBaseValue(20.0d + 2.0d * playerPermanentBuffs.getLifeFruits());
//                        player.setHealth(player.getHealth()+2.0f);
//                    }
//                }
            } else { //Client Side

            }
        } else { //End of Tick
            //Server Side
            if(event.side == LogicalSide.SERVER){
                if (EnchantmentUtil.hasTurtling(player) && player.isShiftKeyDown()) {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1, 0, false, false, true));
                }
            } else { //Client Side

            }
        }
    }

    /**
     * Sets player health to the appropriate level upon respawn based don life fruits eaten
     */
    @SubscribeEvent
    public static void onPlayerRespawnEvent(final PlayerEvent.PlayerRespawnEvent event){
        //Todo life fruits
//        Player player =  event.getPlayer();
//        if(player instanceof IOdysseyPlayer && !player.level.isClientSide){
//            player.setHealth(20.0f + 2.0f * ((IOdysseyPlayer) player).getLifeFruits());
//        }
    }

    @SubscribeEvent
    public static void onPlayerEvent$HarvestCheck(final PlayerEvent.HarvestCheck event){
        Block block = event.getTargetBlock().getBlock();
        Item item = event.getPlayer().getMainHandItem().getItem();
        if(block instanceof WebBlock && item instanceof SwordItem || item == Items.SHEARS){
            event.setCanHarvest(true);
        }
    }
}
