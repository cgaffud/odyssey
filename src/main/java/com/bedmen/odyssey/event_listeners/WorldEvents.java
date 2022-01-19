package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.ai.OdysseyRangedBowAttackGoal;
import com.bedmen.odyssey.items.OdysseyBowItem;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.WeaponUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldEvents {

    public static final Set<Integer> IDS_FOR_REMOVAL = ConcurrentHashMap.newKeySet();

    /**
     * Replaces Mob Equipment when they spawn
     */
    @SubscribeEvent
    public static void onServerTickEvent(final TickEvent.WorldTickEvent event){
        if(event.phase == TickEvent.Phase.START){
            if(event.side == LogicalSide.SERVER){
                Level level = event.world;
                for(Integer integer : EntityEvents.IDS){
                    Entity entity = level.getEntity(integer);
                    if(entity instanceof Skeleton skeleton){
                        ChunkPos chunkPos = entity.chunkPosition();
                        if(level.hasChunk(chunkPos.x, chunkPos.z) && level.getChunk(chunkPos.x, chunkPos.z).getStatus() == ChunkStatus.FULL){
                            Item item = switch(skeleton.getRandom().nextInt(18)){
                                default -> null;
                                case 0 -> ItemRegistry.BONE_LONG_BOW.get();
                                case 1 -> ItemRegistry.BONE_REPEATER.get();
                                case 2 -> ItemRegistry.BOWN.get();
                            };
                            if(item != null){
                                skeleton.setItemInHand(InteractionHand.MAIN_HAND, item.getDefaultInstance());
                                reassessSkeletonWeaponGoal(skeleton);
                            }
                            IDS_FOR_REMOVAL.add(skeleton.getId());
                        }
                    }
                }
                EntityEvents.IDS.removeAll(IDS_FOR_REMOVAL);
                IDS_FOR_REMOVAL.clear();
            }
        }
    }

    public static void reassessSkeletonWeaponGoal(Skeleton skeletonEntity) {
        if (!skeletonEntity.level.isClientSide) {
            skeletonEntity.goalSelector.removeGoal(skeletonEntity.meleeGoal);
            skeletonEntity.goalSelector.removeGoal(skeletonEntity.bowGoal);
            ItemStack itemstack = skeletonEntity.getItemInHand(WeaponUtil.getHandHoldingBow(skeletonEntity));
            Item item = itemstack.getItem();
            if (item instanceof OdysseyBowItem) {
                int i = ((OdysseyBowItem) item).getChargeTime(itemstack);
                if (itemstack.getItem() == ItemRegistry.BOW.get() && skeletonEntity.level.getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }
                OdysseyRangedBowAttackGoal<AbstractSkeleton> odysseyBowGoal = new OdysseyRangedBowAttackGoal<>(skeletonEntity, 1.0D, i, 15.0F);

                skeletonEntity.goalSelector.addGoal(4, odysseyBowGoal);
            } else {
                skeletonEntity.goalSelector.addGoal(4, skeletonEntity.meleeGoal);
            }
        }
    }
}
