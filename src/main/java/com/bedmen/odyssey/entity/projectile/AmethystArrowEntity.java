package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class AmethystArrowEntity extends AbstractArrowEntity {

    public AmethystArrowEntity(EntityType<? extends AmethystArrowEntity> p_i50158_1_, World p_i50158_2_) {
        super(p_i50158_1_, p_i50158_2_);
        this.setBaseDamage(2.5d);
    }

    public AmethystArrowEntity(World p_i46768_1_, LivingEntity p_i46768_2_) {
        super(EntityTypeRegistry.AMETHYST_ARROW.get(), p_i46768_2_, p_i46768_1_);
        this.setBaseDamage(2.5d);
    }

    public AmethystArrowEntity(World p_i46769_1_, double p_i46769_2_, double p_i46769_4_, double p_i46769_6_) {
        super(EntityTypeRegistry.AMETHYST_ARROW.get(), p_i46769_2_, p_i46769_4_, p_i46769_6_, p_i46769_1_);
        this.setBaseDamage(2.5d);
    }

    public void tick() {
        super.tick();
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(ItemRegistry.AMETHYST_ARROW.get());
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}