package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.projectile.NewTridentEntity;
import com.bedmen.odyssey.entity.projectile.PermafrostIcicleEntity;
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
    private ClientWorld level;
    @Shadow
    private Minecraft minecraft;

    private ClientPlayNetHandler getThis(Object o){
        return (ClientPlayNetHandler)o;
    }

    public void handleAddEntity(SSpawnObjectPacket packetIn) {
        PacketThreadUtil.ensureRunningOnSameThread(packetIn, getThis(this), this.minecraft);
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        EntityType<?> entitytype = packetIn.getType();
        Entity entity;
        if (entitytype == EntityType.CHEST_MINECART) {
            entity = new ChestMinecartEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.FURNACE_MINECART) {
            entity = new FurnaceMinecartEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.TNT_MINECART) {
            entity = new TNTMinecartEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.SPAWNER_MINECART) {
            entity = new SpawnerMinecartEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.HOPPER_MINECART) {
            entity = new HopperMinecartEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.COMMAND_BLOCK_MINECART) {
            entity = new CommandBlockMinecartEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.MINECART) {
            entity = new MinecartEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.FISHING_BOBBER) {
            Entity entity1 = this.level.getEntity(packetIn.getData());
            if (entity1 instanceof PlayerEntity) {
                entity = new FishingBobberEntity(this.level, (PlayerEntity)entity1, d0, d1, d2);
            } else {
                entity = null;
            }
        } else if (entitytype == EntityType.ARROW) {
            entity = new ArrowEntity(this.level, d0, d1, d2);
            Entity entity2 = this.level.getEntity(packetIn.getData());
            if (entity2 != null) {
                ((AbstractArrowEntity)entity).setOwner(entity2);
            }
        } else if (entitytype == EntityType.SPECTRAL_ARROW) {
            entity = new SpectralArrowEntity(this.level, d0, d1, d2);
            Entity entity3 = this.level.getEntity(packetIn.getData());
            if (entity3 != null) {
                ((AbstractArrowEntity)entity).setOwner(entity3);
            }
        } else if (entitytype == EntityTypeRegistry.NEW_TRIDENT.get()) {
            entity = new NewTridentEntity(this.level, d0, d1, d2);
            Entity entity4 = this.level.getEntity(packetIn.getData());
            if (entity4 != null) {
                ((AbstractArrowEntity)entity).setOwner(entity4);
            }
        } else if (entitytype == EntityTypeRegistry.SERPENT_TRIDENT.get()) {
            entity = new SerpentTridentEntity(this.level, d0, d1, d2);
            Entity entity4 = this.level.getEntity(packetIn.getData());
            if (entity4 != null) {
                ((AbstractArrowEntity)entity).setOwner(entity4);
            }
        } else if (entitytype == EntityType.SNOWBALL) {
            entity = new SnowballEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.LLAMA_SPIT) {
            entity = new LlamaSpitEntity(this.level, d0, d1, d2, packetIn.getXa(), packetIn.getYa(), packetIn.getZa());
        } else if (entitytype == EntityType.ITEM_FRAME) {
            entity = new ItemFrameEntity(this.level, new BlockPos(d0, d1, d2), Direction.from3DDataValue(packetIn.getData()));
        } else if (entitytype == EntityType.LEASH_KNOT) {
            entity = new LeashKnotEntity(this.level, new BlockPos(d0, d1, d2));
        } else if (entitytype == EntityType.ENDER_PEARL) {
            entity = new EnderPearlEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.EYE_OF_ENDER) {
            entity = new EyeOfEnderEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.FIREWORK_ROCKET) {
            entity = new FireworkRocketEntity(this.level, d0, d1, d2, ItemStack.EMPTY);
        } else if (entitytype == EntityType.FIREBALL) {
            entity = new FireballEntity(this.level, d0, d1, d2, packetIn.getXa(), packetIn.getYa(), packetIn.getZa());
        } else if (entitytype == EntityType.DRAGON_FIREBALL) {
            entity = new DragonFireballEntity(this.level, d0, d1, d2, packetIn.getXa(), packetIn.getYa(), packetIn.getZa());
        } else if (entitytype == EntityType.SMALL_FIREBALL) {
            entity = new SmallFireballEntity(this.level, d0, d1, d2, packetIn.getXa(), packetIn.getYa(), packetIn.getZa());
        } else if (entitytype == EntityType.WITHER_SKULL) {
            entity = new WitherSkullEntity(this.level, d0, d1, d2, packetIn.getXa(), packetIn.getYa(), packetIn.getZa());
        } else if (entitytype == EntityTypeRegistry.PERMAFROST_ICICLE.get()) {
            entity = new PermafrostIcicleEntity(this.level, d0, d1, d2, packetIn.getXa(), packetIn.getYa(), packetIn.getZa());
        } else if (entitytype == EntityType.SHULKER_BULLET) {
            entity = new ShulkerBulletEntity(this.level, d0, d1, d2, packetIn.getXa(), packetIn.getYa(), packetIn.getZa());
        } else if (entitytype == EntityType.EGG) {
            entity = new EggEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.EVOKER_FANGS) {
            entity = new EvokerFangsEntity(this.level, d0, d1, d2, 0.0F, 0, (LivingEntity)null);
        } else if (entitytype == EntityType.POTION) {
            entity = new PotionEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.EXPERIENCE_BOTTLE) {
            entity = new ExperienceBottleEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.BOAT) {
            entity = new BoatEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.TNT) {
            entity = new TNTEntity(this.level, d0, d1, d2, (LivingEntity)null);
        } else if (entitytype == EntityType.ARMOR_STAND) {
            entity = new ArmorStandEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.END_CRYSTAL) {
            entity = new EnderCrystalEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.ITEM) {
            entity = new ItemEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.FALLING_BLOCK) {
            entity = new FallingBlockEntity(this.level, d0, d1, d2, Block.stateById(packetIn.getData()));
        } else if (entitytype == EntityType.AREA_EFFECT_CLOUD) {
            entity = new AreaEffectCloudEntity(this.level, d0, d1, d2);
        } else if (entitytype == EntityType.LIGHTNING_BOLT) {
            entity = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, this.level);
        } else {
            entity = null;
        }

        if (entity != null) {
            int i = packetIn.getId();
            entity.setPacketCoordinates(d0, d1, d2);
            entity.moveTo(d0, d1, d2);
            entity.xRot = (float)(packetIn.getxRot() * 360) / 256.0F;
            entity.yRot = (float)(packetIn.getyRot() * 360) / 256.0F;
            entity.setId(i);
            entity.setUUID(packetIn.getUUID());
            this.level.putNonPlayerEntity(i, entity);
            if (entity instanceof AbstractMinecartEntity) {
                this.minecraft.getSoundManager().play(new MinecartTickableSound((AbstractMinecartEntity)entity));
            }
        }
    }
}
