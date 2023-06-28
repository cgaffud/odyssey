package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OvergrownSkeleton extends OdysseySkeleton implements Overgrown{

    public OvergrownSkeleton(EntityType<? extends OdysseySkeleton> p_34271_, Level p_34272_) {
        super(p_34271_, p_34272_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 40.0D).add(Attributes.MOVEMENT_SPEED, (double)0.18F).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.ARMOR, 2.0D).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE).add(Attributes.MAX_HEALTH, 25);
    }

    // tbf we could interface for all this code but idk
    @Override
    public void tick() {
        super.tick();
        this.regenerate(this, 1.0f);
    }
}
