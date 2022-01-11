package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.items.equipment.DualWieldItem;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.bedmen.odyssey.util.WeaponUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {

    @Shadow
    public ItemCooldowns getCooldowns() {return null;}

    protected MixinPlayer(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    public float getCurrentItemAttackStrengthDelay() {
        if(DualWieldItem.isDualWielding(getPlayerEntity())){
            return (float)(1.0D / this.getAttributeValue(Attributes.ATTACK_SPEED) * 10.0D);
        }
        return (float)(1.0D / this.getAttributeValue(Attributes.ATTACK_SPEED) * 20.0D);
    }

    public void disableShield(boolean b) {
        float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
        if (b) {
            f += 0.75F;
        }

        if (this.random.nextFloat() < f) {
            int recoveryTime = this.getUseItem().getItem() instanceof OdysseyShieldItem odysseyShieldItem ? odysseyShieldItem.getRecoveryTime() : WeaponUtil.DEFAULT_RECOVERY_TIME;
            for(Item item : OdysseyItemTags.SHIELDS.getValues()){
                this.getCooldowns().addCooldown(item, recoveryTime);
            }
            this.stopUsingItem();
            this.level.broadcastEntityEvent(this, (byte)30);
        }

    }

    private Player getPlayerEntity(){
        return (Player)(Object)this;
    }
}
