package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.entity.monster.Bandit;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.BanditAirParticlesPacket;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Predicate;

public class GetJumpedByBanditsBlockEntity extends BlockEntity {
    public GetJumpedByBanditsBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityTypeRegistry.GET_JUMPED_BY_BANDITS_BLOCK.get(), p_155229_, p_155230_);
        this.shouldSpawnBandits = true;
    }

    public boolean shouldSpawnBandits;

    // Pulled from BaseSpwawner()
    private static final int REQUIRED_PLAYER_RANGE = 5;
    private static final int CONTRIBUTING_PLAYER_RANGE = 15;
    // Only look at players that are alive and in survival or adventure
    private static final Predicate<Entity> isAliveAndNotInvincible = (Entity entity) -> EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(entity) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity);
    // We use getNearestPlayer instead of hasNearbyAlivePlayer
    private static boolean isNearPlayer(Level p_151344_, BlockPos p_151345_) {
        return GeneralUtil.hasNearbyPlayer(p_151344_, (double)p_151345_.getX() + 0.5D, (double)p_151345_.getY() + 0.5D, (double)p_151345_.getZ() + 0.5D, REQUIRED_PLAYER_RANGE, isAliveAndNotInvincible);
    }

    public static void clientTick(Level p_155755_, BlockPos p_155756_, BlockState p_155757_, GetJumpedByBanditsBlockEntity GetJumpedByBanditsBlockEntity) {
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, GetJumpedByBanditsBlockEntity gjbbe) {
        if (gjbbe.shouldSpawnBandits && isNearPlayer(level, blockPos) && level.getDifficulty() != Difficulty.PEACEFUL) {
            ServerLevel serverLevel = (ServerLevel) level;
            int nearbyPlayerNum = GeneralUtil.getNearbyPlayerNum(level, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, CONTRIBUTING_PLAYER_RANGE);
            nearbyPlayerNum = Mth.clamp(nearbyPlayerNum, 0, 8);
            double trialSpawnRangeMax = (nearbyPlayerNum-1)/4.0+3;
            RandomSource randomSource = level.getRandom();


            for (int remainingSpawns = nearbyPlayerNum * 2 + 2; remainingSpawns > 0; remainingSpawns--)
            {
                double trialSpawnRange = trialSpawnRangeMax;
                double x,y,z;
                for (int i = 0; i < 4; i++) {
                    x = (double)blockPos.getX() + (randomSource.nextDouble() - 0.5) * 2 * trialSpawnRange;
                    y = (double)blockPos.getY() + 1f;
                    z = (double)blockPos.getZ() + (randomSource.nextDouble() - 0.5) * 2 * trialSpawnRange;
                    if (level.noCollision(EntityTypeRegistry.BANDIT.get().getAABB(x,y,z))) {
                        Bandit bandit = new Bandit(EntityTypeRegistry.BANDIT.get(), level);
                        if (bandit == null)
                            continue;
                        bandit.moveTo(x,y,z,360*randomSource.nextFloat(), 0);
                        // Unsure if this is needed - keeping here in case
                        //if (bandit.checkSpawnObstruction(level))
                        //    continue;
                        bandit.finalizeHideoutSpawn(serverLevel, level.getCurrentDifficultyAt(new BlockPos(x,y,z)), MobSpawnType.SPAWNER, null, null);
                        serverLevel.addFreshEntityWithPassengers(bandit);
                        OdysseyNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> bandit), new BanditAirParticlesPacket(bandit));
                        break;
                    }
                    trialSpawnRangeMax -= 0.5;
                }
            }

            gjbbe.shouldSpawnBandits = false;
        }
    }


}
