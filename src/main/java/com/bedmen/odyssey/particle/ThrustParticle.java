package com.bedmen.odyssey.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ThrustParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    ThrustParticle(ClientLevel clientLevel, double x, double y, double z, double d, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, 0.0D, 0.0D, 0.0D);
        this.lifetime = 6 + this.random.nextInt(4);
        float f = this.random.nextFloat() * 0.6F + 0.4F;
        this.rCol = f;
        this.gCol = f;
        this.bCol = f;
        System.out.println(d);
        this.quadSize = 0.15f * (1.0F - (float)d * 0.5F);
        this.sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);
    }

    public int getLightColor(float partialTicks) {
        return 15728880;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet p_106925_) {
            this.sprites = p_106925_;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double x, double y, double z, double d, double p_106942_, double p_106943_) {
            return new ThrustParticle(clientLevel, x, y, z, d, this.sprites);
        }
    }
}