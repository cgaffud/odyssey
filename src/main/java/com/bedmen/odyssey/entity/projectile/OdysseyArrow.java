package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.OdysseyArrowItem;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.util.function.Supplier;

public class OdysseyArrow extends OdysseyAbstractArrow implements IEntityAdditionalSpawnData {
    private ArrowType arrowType = ArrowType.AMETHYST;

    public OdysseyArrow(EntityType<? extends OdysseyArrow> p_i50158_1_, Level p_i50158_2_) {
        super(p_i50158_1_, p_i50158_2_);
    }

    public OdysseyArrow(Level p_i46768_1_, LivingEntity p_i46768_2_, ArrowType arrowType) {
        super(EntityTypeRegistry.ARROW.get(), p_i46768_2_, p_i46768_1_);
        this.arrowType = arrowType;
        this.setBaseDamage(arrowType.getDamage());
    }

    public OdysseyArrow(Level p_i46769_1_, double p_i46769_2_, double p_i46769_4_, double p_i46769_6_) {
        super(EntityTypeRegistry.ARROW.get(), p_i46769_2_, p_i46769_4_, p_i46769_6_, p_i46769_1_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(this.getArrowType().getItem());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if (compoundNBT.contains("ArrowType")) {
            this.arrowType = ArrowType.valueOf(compoundNBT.getString("ArrowType"));
        }
        this.setBaseDamage(this.getArrowType().getDamage());
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putString("ArrowType", this.arrowType.name());
    }

    public ArrowType getArrowType(){
        return this.arrowType;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.arrowType.ordinal());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.arrowType = ArrowType.values()[additionalData.readInt()];
    }

    public enum ArrowType{
        FLINT(ItemRegistry.ARROW, 5.0d, new ResourceLocation("textures/entity/projectiles/arrow.png")),
        AMETHYST(ItemRegistry.AMETHYST_ARROW, 6.0d, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/amethyst_arrow.png"));

        private final Lazy<Item> itemSupplier;
        private final double damage;
        private final ResourceLocation resourceLocation;

        ArrowType(Supplier<Item> itemSupplier, double damage, ResourceLocation resourceLocation){
            this.itemSupplier = Lazy.of(itemSupplier);
            this.damage = damage;
            this.resourceLocation = resourceLocation;
        }

        public Item getItem(){
            return this.itemSupplier.get();
        }

        public double getDamage(){
            return this.damage;
        }

        public ResourceLocation getResourceLocation(){
            return this.resourceLocation;
        }
    }
}