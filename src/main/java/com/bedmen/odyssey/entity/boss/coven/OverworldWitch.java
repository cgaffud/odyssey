package com.bedmen.odyssey.entity.boss.coven;

import com.bedmen.odyssey.entity.ai.CovenReturnToMasterGoal;
import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OverworldWitch extends CovenWitch {

    Phase phase;

    public OverworldWitch(EntityType<? extends CovenWitch> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CovenReturnToMasterGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    private boolean isValidTarget(LivingEntity target, CovenMaster covenMaster) { return ((target != null) && (covenMaster.validTargetPredicate((ServerPlayer) target))); }

    public void aiStep() {

        Optional<CovenMaster> master = this.getMaster();
        if(!this.isNoAi() && master.isPresent()) {
            CovenMaster covenMaster = master.get();

            if (!this.level.isClientSide) {
                // Target
                switch (this.phase) {
                    case CHASING:
                        if (!this.isValidTarget(this.getTarget(), covenMaster))
                            this.phase = Phase.IDLE;
                    case IDLE:
                        if (GeneralUtil.isHashTick(this, this.level, 50)) {
                            Collection<ServerPlayer> serverPlayerEntities = covenMaster.bossEvent.getPlayers();
                            List<ServerPlayer> serverPlayerEntityList = serverPlayerEntities.stream().filter(covenMaster::validTargetPredicate).collect(Collectors.toList());

                            if (serverPlayerEntityList.isEmpty()) {
                                this.setTarget(null);
                            } else {
                                this.phase = Phase.CHASING;
                                this.setTarget(serverPlayerEntityList.get(this.random.nextInt(serverPlayerEntityList.size())));
                            }
                        }
                }
            }
        }

        super.aiStep();
    }

    @Override
    void doWhenReturnToMaster() {
        this.phase = Phase.IDLE;
    }

    enum Phase {
        IDLE,
        CHASING;
    }
}
