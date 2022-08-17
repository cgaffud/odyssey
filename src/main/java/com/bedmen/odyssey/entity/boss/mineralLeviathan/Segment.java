package com.bedmen.odyssey.entity.boss.mineralLeviathan;

import com.bedmen.odyssey.entity.boss.PuppetEntity;
import com.bedmen.odyssey.util.GeneralUtil;
import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class Segment extends LivingEntity implements PuppetEntity {
    private static final Iterable<ItemStack> ARMOR_SLOTS = ImmutableList.of();
    public float mouthAngle;
    public float mouthAngleO;

    public Segment(EntityType<? extends Segment> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public boolean shouldBeRemoved() {
        return false;
    }

    /**
     * For making the head directly redirect to have the desired velocity
     * @param targetVelocity the desired velocity
     * @param acceleration the size of the vector we can add to our current velocity
     */
    protected void moveTowards(Vec3 targetVelocity, double acceleration){
        Vec3 currentVelocity = this.getDeltaMovement();
        Vec3 difference = targetVelocity.subtract(currentVelocity);
        Vec3 velocity = currentVelocity.add(difference.normalize().scale(acceleration));
        this.setVelocity(velocity);
    }

    /**
     * For making the head travel along a circular arc until pointed in the direction of targetRotationVector
     * @param targetRotationVector the desired direction the head wants to point in after the arc has been travelled (normalized)
     * @param targetSpeed the forward speed of the head while traveling along the arc
     * @param maximumAcceleration the maximum acceleration allowed per tick to get to targetSpeed
     * @param maximumRadians the maximum change in rotation per tick while traveling along the arc
     */
    protected void rotateTowards(Vec3 targetRotationVector, double targetSpeed, double maximumAcceleration, double maximumRadians){
        Vec3 currentVelocity = this.getDeltaMovement();
        double speed = GeneralUtil.toTargetWithMaxChange(currentVelocity.length(), targetSpeed, maximumAcceleration);
        Vec3 velocity = GeneralUtil.toTargetVectorWithMaxChange(currentVelocity.normalize(), targetRotationVector, maximumRadians).scale(speed);
        this.setVelocity(velocity);
    }

    protected void setVelocity(Vec3 velocity) {
        this.setDeltaMovement(velocity);
        this.setRotation(velocity);
    }

    protected void setRotation(Vec3 velocity) {
        float f = Mth.sqrt((float) velocity.horizontalDistanceSqr());
        if (velocity.lengthSqr() != 0.0D) {
            this.setYRot((float)(Mth.atan2(velocity.x, velocity.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(f, velocity.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f));
        }
    }

    public boolean save(CompoundTag compoundTag) {
        return false;
    }

    public Iterable<ItemStack> getArmorSlots() {
        return ARMOR_SLOTS;
    }

    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {}

    public HumanoidArm getMainArm() {
        return null;
    }

    public boolean hasShell(){
        return true;
    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
