package com.bedmen.odyssey.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class CamoCreeper extends OdysseyCreeper {
    private Biome biome;

    public CamoCreeper(EntityType<? extends OdysseyCreeper> p_i50213_1_, Level p_i50213_2_) {
        super(p_i50213_1_, p_i50213_2_);
    }

    public void tick(){
        super.tick();
        this.biome = this.level.getBiome(this.blockPosition()).value();
    }

    public Biome getBiome(){
        return this.biome;
    }
}
