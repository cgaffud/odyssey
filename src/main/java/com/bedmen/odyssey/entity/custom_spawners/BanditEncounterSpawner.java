package com.bedmen.odyssey.entity.custom_spawners;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.entity.monster.Bandit;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

// Based on PatrolSpawner
public class BanditEncounterSpawner implements CustomSpawner {

    private int nextTick;

    public int tick(ServerLevel serverLevel, boolean p_64571_, boolean p_64572_) {
        if (!p_64571_) {
            return 0;
        } else if (!serverLevel.getGameRules().getBoolean(GameRules.RULE_DO_PATROL_SPAWNING)) {
            return 0;
        } else {
            RandomSource randomsource = serverLevel.random;
            --this.nextTick;
            if (this.nextTick > 0) {
                return 0;
            } else {
                this.nextTick += 12000 + randomsource.nextInt(1200);
                long i = serverLevel.getDayTime() / 240000L;
                // Check if night and its been long enough since start of game
                if (i >= 5L && !serverLevel.isDay()) {
                    if (randomsource.nextInt(5) != 0) {
                        return 0;
                    } else {
                        int j = serverLevel.players().size();
                        if (j < 1) {
                            return 0;
                        } else {
                            Player player = serverLevel.players().get(randomsource.nextInt(j));
                            // Also check that player is at least past mineral leviathan.
                            if (player.isSpectator() || (AspectUtil.getBuffAspectStrength(player, Aspects.ADDITIONAL_MOB_HARVEST_LEVEL) < 1)) {
                                return 0;
                            } else if (serverLevel.isCloseToVillage(player.blockPosition(), 2)) {
                                return 0;
                            } else {
                                int k = (24 + randomsource.nextInt(24)) * (randomsource.nextBoolean() ? -1 : 1);
                                int l = (24 + randomsource.nextInt(24)) * (randomsource.nextBoolean() ? -1 : 1);
                                BlockPos.MutableBlockPos blockpos$mutableblockpos = player.blockPosition().mutable().move(k, 0, l);
                                int i1 = 10;
                                if (!serverLevel.hasChunksAt(blockpos$mutableblockpos.getX() - 10, blockpos$mutableblockpos.getZ() - 10, blockpos$mutableblockpos.getX() + 10, blockpos$mutableblockpos.getZ() + 10)) {
                                    return 0;
                                } else {
                                    Holder<Biome> holder = serverLevel.getBiome(blockpos$mutableblockpos);
                                    if (holder.is(BiomeTags.WITHOUT_PATROL_SPAWNS)) {
                                        return 0;
                                    } else {
                                        System.out.println("WE ARE SPAWNING!");
                                        int j1 = 0;
                                        int k1 = (int)Math.ceil((double)serverLevel.getCurrentDifficultyAt(blockpos$mutableblockpos).getEffectiveDifficulty()/2) + 1;

                                        for(int l1 = 0; l1 < k1; ++l1) {
                                            ++j1;
                                            blockpos$mutableblockpos.setY(serverLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockpos$mutableblockpos).getY());
                                            if (l1 == 0) {
                                                if (!this.spawnPatrolMember(serverLevel, blockpos$mutableblockpos, randomsource, true)) {
                                                    break;
                                                }
                                            } else {
                                                this.spawnPatrolMember(serverLevel, blockpos$mutableblockpos, randomsource, false);
                                            }

                                            blockpos$mutableblockpos.setX(blockpos$mutableblockpos.getX() + randomsource.nextInt(5) - randomsource.nextInt(5));
                                            blockpos$mutableblockpos.setZ(blockpos$mutableblockpos.getZ() + randomsource.nextInt(5) - randomsource.nextInt(5));
                                        }

                                        return j1;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    return 0;
                }
            }
        }
    }

    private boolean spawnPatrolMember(ServerLevel p_224533_, BlockPos p_224534_, RandomSource p_224535_, boolean isLeader) {
        BlockState blockstate = p_224533_.getBlockState(p_224534_);
        if (!NaturalSpawner.isValidEmptySpawnBlock(p_224533_, p_224534_, blockstate, blockstate.getFluidState(), EntityTypeRegistry.BANDIT.get())) {
            return false;
        } else if (!PatrollingMonster.checkPatrollingMonsterSpawnRules( EntityTypeRegistry.BANDIT.get(), p_224533_, MobSpawnType.PATROL, p_224534_, p_224535_)) {
            return false;
        } else {
            Bandit patrollingmonster =  EntityTypeRegistry.BANDIT.get().create(p_224533_);
            if (patrollingmonster != null) {
                if (isLeader) {
                    patrollingmonster.setPatrolLeader(true);
                    patrollingmonster.findPatrolTarget();
                }

                patrollingmonster.setPos((double)p_224534_.getX(), (double)p_224534_.getY(), (double)p_224534_.getZ());
                if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(patrollingmonster, p_224533_, p_224534_.getX(), p_224534_.getY(), p_224534_.getZ(), null, MobSpawnType.PATROL) == -1) return false;
                patrollingmonster.finalizeSpawn(p_224533_, p_224533_.getCurrentDifficultyAt(p_224534_), MobSpawnType.PATROL, (SpawnGroupData)null, (CompoundTag)null, false);
                // lol am lazy. otherwise the bandit leader has the pillager like banner and thats a lil silly.
                if (isLeader)
                    patrollingmonster.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                p_224533_.addFreshEntityWithPassengers(patrollingmonster);
                System.out.println(p_224534_);
                return true;
            } else {
                return false;
            }
        }
    }

}
