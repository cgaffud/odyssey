package com.bedmen.odyssey.network.play;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.projectile.BoomerangEntity;
import com.bedmen.odyssey.entity.projectile.OdysseyTridentEntity;
import com.bedmen.odyssey.entity.projectile.UpgradedArrowEntity;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MinecartTickableSound;
import net.minecraft.client.gui.screen.Screen;
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
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class OdysseyClientPlayNetHandler extends ClientPlayNetHandler {
    private final Minecraft minecraft;
    public OdysseyClientPlayNetHandler(Minecraft minecraft, Screen screen, NetworkManager networkManager, GameProfile gameProfile) {
        super(minecraft, screen, networkManager, gameProfile);
        this.minecraft = minecraft;
    }

    public void handleAddEntity(SSpawnObjectPacket p_147235_1_) {
        PacketThreadUtil.ensureRunningOnSameThread(p_147235_1_, this, this.minecraft);
        ClientWorld clientWorld = this.getLevel();
        double d0 = p_147235_1_.getX();
        double d1 = p_147235_1_.getY();
        double d2 = p_147235_1_.getZ();
        EntityType<?> entitytype = p_147235_1_.getType();
        Entity entity;
        if (entitytype == EntityType.CHEST_MINECART) {
            entity = new ChestMinecartEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.FURNACE_MINECART) {
            entity = new FurnaceMinecartEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.TNT_MINECART) {
            entity = new TNTMinecartEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.SPAWNER_MINECART) {
            entity = new SpawnerMinecartEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.HOPPER_MINECART) {
            entity = new HopperMinecartEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.COMMAND_BLOCK_MINECART) {
            entity = new CommandBlockMinecartEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.MINECART) {
            entity = new MinecartEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.FISHING_BOBBER) {
            Entity entity1 = clientWorld.getEntity(p_147235_1_.getData());
            if (entity1 instanceof PlayerEntity) {
                entity = new FishingBobberEntity(clientWorld, (PlayerEntity)entity1, d0, d1, d2);
            } else {
                entity = null;
            }
        } else if (entitytype == EntityType.ARROW) {
            entity = new ArrowEntity(clientWorld, d0, d1, d2);
            Entity entity2 = clientWorld.getEntity(p_147235_1_.getData());
            if (entity2 != null) {
                ((AbstractArrowEntity)entity).setOwner(entity2);
            }
        } else if (entitytype == EntityType.SPECTRAL_ARROW) {
            entity = new SpectralArrowEntity(clientWorld, d0, d1, d2);
            Entity entity3 = clientWorld.getEntity(p_147235_1_.getData());
            if (entity3 != null) {
                ((AbstractArrowEntity)entity).setOwner(entity3);
            }
        } else if (entitytype == EntityTypeRegistry.UPGRADED_ARROW.get()) {
            entity = new UpgradedArrowEntity(clientWorld, d0, d1, d2);
            Entity entity3 = clientWorld.getEntity(p_147235_1_.getData());
            if (entity3 != null) {
                ((AbstractArrowEntity)entity).setOwner(entity3);
            }
        } else if (entitytype == EntityType.TRIDENT) {
            entity = new TridentEntity(clientWorld, d0, d1, d2);
            Entity entity4 = clientWorld.getEntity(p_147235_1_.getData());
            if (entity4 != null) {
                ((AbstractArrowEntity)entity).setOwner(entity4);
            }
        } else if (entitytype == EntityTypeRegistry.TRIDENT.get()) {
            entity = new OdysseyTridentEntity(clientWorld, d0, d1, d2);
            Entity entity4 = clientWorld.getEntity(p_147235_1_.getData());
            if (entity4 != null) {
                ((AbstractArrowEntity)entity).setOwner(entity4);
            }
        } else if (entitytype == EntityTypeRegistry.BOOMERANG.get()) {
            entity = new BoomerangEntity(clientWorld, d0, d1, d2);
            Entity entity4 = clientWorld.getEntity(p_147235_1_.getData());
            if (entity4 != null) {
                ((AbstractArrowEntity)entity).setOwner(entity4);
            }
        } else if (entitytype == EntityType.SNOWBALL) {
            entity = new SnowballEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.LLAMA_SPIT) {
            entity = new LlamaSpitEntity(clientWorld, d0, d1, d2, p_147235_1_.getXa(), p_147235_1_.getYa(), p_147235_1_.getZa());
        } else if (entitytype == EntityType.ITEM_FRAME) {
            entity = new ItemFrameEntity(clientWorld, new BlockPos(d0, d1, d2), Direction.from3DDataValue(p_147235_1_.getData()));
        } else if (entitytype == EntityType.LEASH_KNOT) {
            entity = new LeashKnotEntity(clientWorld, new BlockPos(d0, d1, d2));
        } else if (entitytype == EntityType.ENDER_PEARL) {
            entity = new EnderPearlEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.EYE_OF_ENDER) {
            entity = new EyeOfEnderEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.FIREWORK_ROCKET) {
            entity = new FireworkRocketEntity(clientWorld, d0, d1, d2, ItemStack.EMPTY);
        } else if (entitytype == EntityType.FIREBALL) {
            entity = new FireballEntity(clientWorld, d0, d1, d2, p_147235_1_.getXa(), p_147235_1_.getYa(), p_147235_1_.getZa());
        } else if (entitytype == EntityType.DRAGON_FIREBALL) {
            entity = new DragonFireballEntity(clientWorld, d0, d1, d2, p_147235_1_.getXa(), p_147235_1_.getYa(), p_147235_1_.getZa());
        } else if (entitytype == EntityType.SMALL_FIREBALL) {
            entity = new SmallFireballEntity(clientWorld, d0, d1, d2, p_147235_1_.getXa(), p_147235_1_.getYa(), p_147235_1_.getZa());
        } else if (entitytype == EntityType.WITHER_SKULL) {
            entity = new WitherSkullEntity(clientWorld, d0, d1, d2, p_147235_1_.getXa(), p_147235_1_.getYa(), p_147235_1_.getZa());
        } else if (entitytype == EntityType.SHULKER_BULLET) {
            entity = new ShulkerBulletEntity(clientWorld, d0, d1, d2, p_147235_1_.getXa(), p_147235_1_.getYa(), p_147235_1_.getZa());
        } else if (entitytype == EntityType.EGG) {
            entity = new EggEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.EVOKER_FANGS) {
            entity = new EvokerFangsEntity(clientWorld, d0, d1, d2, 0.0F, 0, (LivingEntity)null);
        } else if (entitytype == EntityType.POTION) {
            entity = new PotionEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.EXPERIENCE_BOTTLE) {
            entity = new ExperienceBottleEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.BOAT) {
            entity = new BoatEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.TNT) {
            entity = new TNTEntity(clientWorld, d0, d1, d2, (LivingEntity)null);
        } else if (entitytype == EntityType.ARMOR_STAND) {
            entity = new ArmorStandEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.END_CRYSTAL) {
            entity = new EnderCrystalEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.ITEM) {
            entity = new ItemEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.FALLING_BLOCK) {
            entity = new FallingBlockEntity(clientWorld, d0, d1, d2, Block.stateById(p_147235_1_.getData()));
        } else if (entitytype == EntityType.AREA_EFFECT_CLOUD) {
            entity = new AreaEffectCloudEntity(clientWorld, d0, d1, d2);
        } else if (entitytype == EntityType.LIGHTNING_BOLT) {
            entity = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, clientWorld);
        } else {
            entity = null;
        }

        if (entity != null) {
            int i = p_147235_1_.getId();
            entity.setPacketCoordinates(d0, d1, d2);
            entity.moveTo(d0, d1, d2);
            entity.xRot = (float)(p_147235_1_.getxRot() * 360) / 256.0F;
            entity.yRot = (float)(p_147235_1_.getyRot() * 360) / 256.0F;
            entity.setId(i);
            entity.setUUID(p_147235_1_.getUUID());
            clientWorld.putNonPlayerEntity(i, entity);
            if (entity instanceof AbstractMinecartEntity) {
                this.minecraft.getSoundManager().play(new MinecartTickableSound((AbstractMinecartEntity)entity));
            }
        }

    }
}
