package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.projectile.NewTridentEntity;
import com.bedmen.odyssey.entity.projectile.SerpentTridentEntity;
import com.bedmen.odyssey.util.EntityTypeRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MinecartTickableSound;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.*;
import net.minecraft.entity.item.minecart.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayNetHandler.class)
public abstract class MixinClientPlayNetHandler {

    @Shadow
    private Minecraft client;
    private ClientWorld world;

    private ClientPlayNetHandler getThis(Object o){
        return (ClientPlayNetHandler)o;
    }

    public void handleSpawnObject(SSpawnObjectPacket packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, getThis(this), this.client);
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        EntityType<?> entitytype = packetIn.getType();
        Entity entity;
        if (entitytype == EntityType.CHEST_MINECART) {
            entity = new ChestMinecartEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.FURNACE_MINECART) {
            entity = new FurnaceMinecartEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.TNT_MINECART) {
            entity = new TNTMinecartEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.SPAWNER_MINECART) {
            entity = new SpawnerMinecartEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.HOPPER_MINECART) {
            entity = new HopperMinecartEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.COMMAND_BLOCK_MINECART) {
            entity = new CommandBlockMinecartEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.MINECART) {
            entity = new MinecartEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.FISHING_BOBBER) {
            Entity entity1 = this.world.getEntityByID(packetIn.getData());
            if (entity1 instanceof PlayerEntity) {
                entity = new FishingBobberEntity(this.world, (PlayerEntity)entity1, d0, d1, d2);
            } else {
                entity = null;
            }
        } else if (entitytype == EntityType.ARROW) {
            entity = new ArrowEntity(this.world, d0, d1, d2);
            Entity entity2 = this.world.getEntityByID(packetIn.getData());
            if (entity2 != null) {
                ((AbstractArrowEntity)entity).setShooter(entity2);
            }
        } else if (entitytype == EntityType.SPECTRAL_ARROW) {
            entity = new SpectralArrowEntity(this.world, d0, d1, d2);
            Entity entity3 = this.world.getEntityByID(packetIn.getData());
            if (entity3 != null) {
                ((AbstractArrowEntity)entity).setShooter(entity3);
            }
        } else if (entitytype == EntityTypeRegistry.NEW_TRIDENT.get()) {
            entity = new NewTridentEntity(this.world, d0, d1, d2);
            Entity entity4 = this.world.getEntityByID(packetIn.getData());
            if (entity4 != null) {
                ((AbstractArrowEntity)entity).setShooter(entity4);
            }
        } else if (entitytype == EntityTypeRegistry.SERPENT_TRIDENT.get()) {
            entity = new SerpentTridentEntity(this.world, d0, d1, d2);
            Entity entity4 = this.world.getEntityByID(packetIn.getData());
            if (entity4 != null) {
                ((AbstractArrowEntity)entity).setShooter(entity4);
            }
        } else if (entitytype == EntityType.SNOWBALL) {
            entity = new SnowballEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.LLAMA_SPIT) {
            entity = new LlamaSpitEntity(this.world, d0, d1, d2, packetIn.func_218693_g(), packetIn.func_218695_h(), packetIn.func_218692_i());
        } else if (entitytype == EntityType.ITEM_FRAME) {
            entity = new ItemFrameEntity(this.world, new BlockPos(d0, d1, d2), Direction.byIndex(packetIn.getData()));
        } else if (entitytype == EntityType.LEASH_KNOT) {
            entity = new LeashKnotEntity(this.world, new BlockPos(d0, d1, d2));
        } else if (entitytype == EntityType.ENDER_PEARL) {
            entity = new EnderPearlEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.EYE_OF_ENDER) {
            entity = new EyeOfEnderEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.FIREWORK_ROCKET) {
            entity = new FireworkRocketEntity(this.world, d0, d1, d2, ItemStack.EMPTY);
        } else if (entitytype == EntityType.FIREBALL) {
            entity = new FireballEntity(this.world, d0, d1, d2, packetIn.func_218693_g(), packetIn.func_218695_h(), packetIn.func_218692_i());
        } else if (entitytype == EntityType.DRAGON_FIREBALL) {
            entity = new DragonFireballEntity(this.world, d0, d1, d2, packetIn.func_218693_g(), packetIn.func_218695_h(), packetIn.func_218692_i());
        } else if (entitytype == EntityType.SMALL_FIREBALL) {
            entity = new SmallFireballEntity(this.world, d0, d1, d2, packetIn.func_218693_g(), packetIn.func_218695_h(), packetIn.func_218692_i());
        } else if (entitytype == EntityType.WITHER_SKULL) {
            entity = new WitherSkullEntity(this.world, d0, d1, d2, packetIn.func_218693_g(), packetIn.func_218695_h(), packetIn.func_218692_i());
        } else if (entitytype == EntityType.SHULKER_BULLET) {
            entity = new ShulkerBulletEntity(this.world, d0, d1, d2, packetIn.func_218693_g(), packetIn.func_218695_h(), packetIn.func_218692_i());
        } else if (entitytype == EntityType.EGG) {
            entity = new EggEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.EVOKER_FANGS) {
            entity = new EvokerFangsEntity(this.world, d0, d1, d2, 0.0F, 0, (LivingEntity)null);
        } else if (entitytype == EntityType.POTION) {
            entity = new PotionEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.EXPERIENCE_BOTTLE) {
            entity = new ExperienceBottleEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.BOAT) {
            entity = new BoatEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.TNT) {
            entity = new TNTEntity(this.world, d0, d1, d2, (LivingEntity)null);
        } else if (entitytype == EntityType.ARMOR_STAND) {
            entity = new ArmorStandEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.END_CRYSTAL) {
            entity = new EnderCrystalEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.ITEM) {
            entity = new ItemEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.FALLING_BLOCK) {
            entity = new FallingBlockEntity(this.world, d0, d1, d2, Block.getStateById(packetIn.getData()));
        } else if (entitytype == EntityType.AREA_EFFECT_CLOUD) {
            entity = new AreaEffectCloudEntity(this.world, d0, d1, d2);
        } else if (entitytype == EntityType.LIGHTNING_BOLT) {
            entity = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, this.world);
        } else {
            entity = null;
        }

        if (entity != null) {
            int i = packetIn.getEntityID();
            entity.setPacketCoordinates(d0, d1, d2);
            entity.moveForced(d0, d1, d2);
            entity.rotationPitch = (float)(packetIn.getPitch() * 360) / 256.0F;
            entity.rotationYaw = (float)(packetIn.getYaw() * 360) / 256.0F;
            entity.setEntityId(i);
            entity.setUniqueId(packetIn.getUniqueId());
            this.world.addEntity(i, entity);
            if (entity instanceof AbstractMinecartEntity) {
                this.client.getSoundHandler().play(new MinecartTickableSound((AbstractMinecartEntity)entity));
            }
        }

    }

}
