package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class OvergrownZombie extends Zombie {

    public OvergrownZombie(EntityType<? extends Zombie> p_34271_, Level p_34272_) {
        super(p_34271_, p_34272_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 40.0D).add(Attributes.MOVEMENT_SPEED, (double)0.18F).add(Attributes.ATTACK_DAMAGE, 4.5D).add(Attributes.ARMOR, 2.0D).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE).add(Attributes.MAX_HEALTH, 25);
    }

    // tbf we could interface for all this code but idk
    @Override
    public void tick() {
        super.tick();
        if (GeneralUtil.isHashTick(this, this.level, 50) && (this.getHealth() < this.getMaxHealth())) {
            this.heal(1.0f);
            if (!this.level.isClientSide()) {
                RandomSource randomSource = this.getRandom();
                for(int i = 0; i < 4; ++i) {
                    ((ServerLevel)this.level).sendParticles(ParticleTypes.HEART,this.getX() + randomSource.nextFloat()-0.5f, this.getY() + randomSource.nextFloat()*2, this.getZ() + randomSource.nextFloat()-0.5f, 2, 0.2D, 0.2D, 0.2D, 0.0D);
                }
            }

        }
    }

    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }

    protected boolean convertsInWater() {
        return false;
    }
}
