package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class JumpKeyPressedPacket {

    public JumpKeyPressedPacket(){
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static void encode(JumpKeyPressedPacket jumpKeyPressedPacket, FriendlyByteBuf buf){
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static JumpKeyPressedPacket decode(FriendlyByteBuf buf){
        return new JumpKeyPressedPacket();
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(JumpKeyPressedPacket jumpKeyPressedPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayerEntity = context.getSender();
            if (serverPlayerEntity != null && EnchantmentUtil.hasSlowFalling(serverPlayerEntity) && serverPlayerEntity.getDeltaMovement().y < -0.1d && !serverPlayerEntity.getCooldowns().isOnCooldown(ItemRegistry.CHICKEN_CHESTPLATE.get())) {
                serverPlayerEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, false, false, true));
                serverPlayerEntity.getCooldowns().addCooldown(ItemRegistry.CHICKEN_HELMET.get(), 200);
                serverPlayerEntity.getCooldowns().addCooldown(ItemRegistry.CHICKEN_CHESTPLATE.get(), 200);
                serverPlayerEntity.getCooldowns().addCooldown(ItemRegistry.CHICKEN_LEGGINGS.get(), 200);
                serverPlayerEntity.getCooldowns().addCooldown(ItemRegistry.CHICKEN_BOOTS.get(), 200);
            }
        });
        context.setPacketHandled(true);
    }
}
