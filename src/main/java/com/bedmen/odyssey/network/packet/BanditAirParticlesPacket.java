package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BanditAirParticlesPacket {
    public int id;

    public BanditAirParticlesPacket(int id){
        this.id = id;
    }

    public BanditAirParticlesPacket(Entity entity){
        this.id = entity.getId();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static void encode(BanditAirParticlesPacket BanditAirParticlesPacket, FriendlyByteBuf buf){
        buf.writeVarInt(BanditAirParticlesPacket.id);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static BanditAirParticlesPacket decode(FriendlyByteBuf buf){
        return new BanditAirParticlesPacket(buf.readVarInt());
    }

    //TODO (cgaffud): Centralized Util for particle creation
    public static void doBanditAirParticles(Entity entity, int count) {
        RandomSource randomSource = entity.level.random;
        double x = entity.getX();
        double y = entity.getY() + 0.5f * entity.getBbHeight();
        double z = entity.getZ();
        for(int i = 0; i < count; i++){
            for(int xi = -1; xi <= 1; xi++){
                for(int yi = -1; yi <= 1; yi++){
                    for(int zi = -1; zi <= 1; zi++){
                        if(!(xi == 0 && yi == 0 && zi == 0) && randomSource.nextBoolean()){
                            Vec3 velocity  = new Vec3(xi, yi, zi).add(new Vec3(randomSource.nextDouble()-0.5, 0.5 + randomSource.nextDouble(),randomSource.nextDouble()-0.5)).normalize().scale(0.3d);
                            entity.level.addParticle(new DustParticleOptions(new Vector3f(0.85f, 0.85f, 0.85f), 1.0F),
                                    x + (randomSource.nextFloat()-0.5f)*entity.getBbWidth()/2,
                                    y+ (randomSource.nextFloat()-0.5f)*entity.getBbHeight()/2,
                                    z+ (randomSource.nextFloat()-0.5f)*entity.getBbWidth()/2,
                                    velocity.x, velocity.y, velocity.z);
                        }
                    }
                }
            }
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(BanditAirParticlesPacket BanditAirParticlesPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if(minecraft.level != null){
                Entity entity = minecraft.level.getEntity(BanditAirParticlesPacket.id);
                if(entity != null){
                    doBanditAirParticles(entity, 2);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
