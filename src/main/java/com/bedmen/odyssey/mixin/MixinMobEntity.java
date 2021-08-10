package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity extends Entity{

    public MixinMobEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    private void maybeDisableShield(PlayerEntity player, ItemStack mainhand, ItemStack activePlayerStack) {
        if (!mainhand.isEmpty() && !activePlayerStack.isEmpty() && (mainhand.getItem() instanceof AxeItem) && (activePlayerStack.getItem() instanceof OdysseyShieldItem)){
            float f = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(getMobEntity(this)) * 0.05F;
            if (this.random.nextFloat() < f) {
                if(activePlayerStack.getItem() instanceof OdysseyShieldItem || activePlayerStack.getItem() instanceof ShieldItem){
                    int ticks = EnchantmentUtil.getRecovery(player);
                    for(Item item : OdysseyItemTags.SHIELD_TAG){
                        player.getCooldowns().addCooldown(item, ticks);
                    }
                }
                this.level.broadcastEntityEvent(player, (byte)30);
            }
        }
    }

    private MobEntity getMobEntity(Object o){
        return (MobEntity)o;
    }

}
