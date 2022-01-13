package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.Odyssey;
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

import java.util.Locale;

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
        FLINT(ItemRegistry.ARROW::get, 5.0d, 0, new ResourceLocation("textures/entity/projectiles/arrow.png")),
        CLOVER_STONE(ItemRegistry.CLOVER_STONE_ARROW::get, 5.5d, 1),
        AMETHYST(ItemRegistry.AMETHYST_ARROW::get, 6.0d, 0);

        private final Lazy<Item> lazyItem;
        private final double damage;
        private final int looting;
        private final ResourceLocation resourceLocation;

        ArrowType(Lazy<Item> lazyItem, double damage, int looting){
            this.lazyItem = lazyItem;
            this.damage = damage;
            this.looting = looting;
            this.resourceLocation = new ResourceLocation(Odyssey.MOD_ID, String.format("textures/entity/projectiles/%s_arrow.png", this.name().toLowerCase(Locale.ROOT)));
        }

        ArrowType(Lazy<Item> lazyItem, double damage, int looting, ResourceLocation resourceLocation){
            this.lazyItem = lazyItem;
            this.damage = damage;
            this.looting = looting;
            this.resourceLocation = resourceLocation;
        }

        public Item getItem(){
            return this.lazyItem.get();
        }

        public double getDamage(){
            return this.damage;
        }

        public int getLooting(){
            return this.looting;
        }

        public ResourceLocation getResourceLocation(){
            return this.resourceLocation;
        }
    }
}