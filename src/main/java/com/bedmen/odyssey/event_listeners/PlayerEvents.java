package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.player.IOdysseyPlayer;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.util.Util;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {

    /**
     * Sets player on fire unless they are nether immune
     * Increases max health when eating life fruit
     */
    @SubscribeEvent
    public static void tickEvent$PlayerTickEventListener(final TickEvent.PlayerTickEvent event){
        PlayerEntity playerEntity =  event.player;
        if(!playerEntity.level.isClientSide && event.phase == TickEvent.Phase.START){
            if(!(playerEntity.abilities.instabuild || playerEntity.isSpectator()) && playerEntity.level.dimensionType().ultraWarm()){
                if(!EnchantmentUtil.hasFireProtectionOrResistance(playerEntity))
                    playerEntity.setSecondsOnFire(1);
            }
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

    /**
     * Sets player health to the appropriate level upon respawn based don life fruits eaten
     */
    @SubscribeEvent
    public static void PlayerEvent$PlayerRespawnEventListener(final PlayerEvent.PlayerRespawnEvent event){
        PlayerEntity playerEntity =  event.getPlayer();
        if(playerEntity instanceof IOdysseyPlayer && !playerEntity.level.isClientSide){
            playerEntity.setHealth(20.0f + 2.0f * ((IOdysseyPlayer) playerEntity).getLifeFruits());
        }
    }

    public static final List<Block> BLOCKS = Util.make(new ArrayList<>(), (list) -> {
        list.add(Blocks.DIAMOND_BLOCK);
        list.add(Blocks.DIAMOND_ORE);
        list.add(Blocks.OBSIDIAN);
        list.add(Blocks.CRYING_OBSIDIAN);
        list.add(Blocks.NETHERITE_BLOCK);
        list.add(Blocks.ANCIENT_DEBRIS);
        list.add(Blocks.RESPAWN_ANCHOR);
    });

    /**
     * Requires the specified blocks to have an additional level of harvest-tier than they normally do in vanilla
     */
    @SubscribeEvent
    public static void PlayerEvent$HarvestCheckListener(final PlayerEvent.HarvestCheck event){
        BlockState blockState = event.getTargetBlock();
        Block block = blockState.getBlock();
        if(BLOCKS.contains(block)){
            ItemStack itemStack = event.getPlayer().getMainHandItem();
            Item item = itemStack.getItem();
            if(item instanceof TieredItem){
                int harvestLevel = ((TieredItem)item).getTier().getLevel();
                Set<ToolType> toolTypesSet = item.getToolTypes(itemStack);
                if(block.getHarvestLevel(blockState) + 1 <= harvestLevel && toolTypesSet.contains(block.getHarvestTool(blockState))){
                    event.setCanHarvest(true);
                } else {
                    event.setCanHarvest(false);
                }
            }
        }
    }
}
