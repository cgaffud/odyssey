package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.bedmen.odyssey.util.WeaponUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Mob.class)
public abstract class MixinMob extends LivingEntity {

    protected MixinMob(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    //Todo Shield
    private void maybeDisableShield(Player player, ItemStack offensiveStack, ItemStack defensiveStack) {
        if (!offensiveStack.isEmpty() && !defensiveStack.isEmpty() && offensiveStack.getItem() instanceof AxeItem && defensiveStack.is(OdysseyItemTags.SHIELDS)) {
            float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(getMob()) * 0.05F;
            if (this.random.nextFloat() < f) {
                int recoveryTime = defensiveStack.getItem() instanceof OdysseyShieldItem odysseyShieldItem ? odysseyShieldItem.getRecoveryTime() : WeaponUtil.DEFAULT_RECOVERY_TIME;
                for(Item item : OdysseyItemTags.SHIELDS.getValues()){
                    player.getCooldowns().addCooldown(item, recoveryTime);
                }
                this.level.broadcastEntityEvent(player, (byte)30);
            }
        }
    }

    private Mob getMob(){
        return (Mob)(Object)this;
    }
}
