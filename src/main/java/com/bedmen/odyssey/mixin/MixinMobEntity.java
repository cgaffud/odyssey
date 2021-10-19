package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity extends Entity{

    public MixinMobEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    //Disables all kinds of shields
    private void maybeDisableShield(PlayerEntity player, ItemStack mainhand, ItemStack activePlayerStack) {
        if (!mainhand.isEmpty() && !activePlayerStack.isEmpty() && (mainhand.getItem() instanceof AxeItem)){
            float f = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(getMobEntity(this)) * 0.05F;
            Item item = activePlayerStack.getItem();
            if (this.random.nextFloat() < f) {
                if(item instanceof OdysseyShieldItem){
                    int ticks = ((OdysseyShieldItem)item).getRecoveryTime();
                    for(Item item1 : OdysseyItemTags.SHIELD_TAG){
                        player.getCooldowns().addCooldown(item1, ticks);
                    }
                    this.level.broadcastEntityEvent(player, (byte)30);
                } else if(item instanceof ShieldItem){
                    for(Item item1 : OdysseyItemTags.SHIELD_TAG){
                        player.getCooldowns().addCooldown(item1, 100);
                    }
                    this.level.broadcastEntityEvent(player, (byte)30);
                }
            }
        }
    }

    private MobEntity getMobEntity(MixinMobEntity mixinMobEntity){
        return (MobEntity)(Object)mixinMobEntity;
    }

}
