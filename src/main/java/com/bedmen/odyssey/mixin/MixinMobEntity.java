package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity extends LivingEntity {

    public MixinMobEntity(EntityType<? extends LivingEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    //Disables all kinds of shields
    private void maybeDisableShield(PlayerEntity player, ItemStack mainhand, ItemStack activePlayerStack) {
        if (!mainhand.isEmpty() && !activePlayerStack.isEmpty() && (mainhand.getItem() instanceof AxeItem)){
            float f = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(getMobEntity()) * 0.05F;
            Item item = activePlayerStack.getItem();
            if (this.random.nextFloat() < f) {
                if(item instanceof OdysseyShieldItem || item instanceof ShieldItem){
                    int ticks = item instanceof OdysseyShieldItem ? ((OdysseyShieldItem)item).getRecoveryTime() : 100;
                    for(Item item1 : OdysseyItemTags.SHIELD_TAG.getValues()){
                        player.getCooldowns().addCooldown(item1, ticks);
                    }
                    this.level.broadcastEntityEvent(player, (byte)30);
                }
            }
        }
    }

    private MobEntity getMobEntity(){
        return (MobEntity)(Object)this;
    }
}
