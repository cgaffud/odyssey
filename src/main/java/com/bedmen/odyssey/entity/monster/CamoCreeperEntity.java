package com.bedmen.odyssey.entity.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CamoCreeperEntity extends OdysseyCreeperEntity {
    private Biome biome;
    public CamoCreeperEntity(EntityType<? extends OdysseyCreeperEntity> p_i50213_1_, World p_i50213_2_) {
        super(p_i50213_1_, p_i50213_2_);
    }

    public void tick(){
        super.tick();
        this.biome = this.level.getBiome(this.blockPosition());
    }

    @OnlyIn(Dist.CLIENT)
    public Biome getBiome(){
        return this.biome;
    }

}
