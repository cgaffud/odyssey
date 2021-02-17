package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.NewShieldItem;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity extends Entity{

    public MixinMobEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    private void func_233655_a_(PlayerEntity player, ItemStack mainhand, ItemStack activePlayerStack) {
        if (!mainhand.isEmpty() && !activePlayerStack.isEmpty() && (mainhand.getItem() instanceof AxeItem) && (activePlayerStack.getItem() instanceof NewShieldItem)){
            float f = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(getMobEntity(this)) * 0.05F;
            if (this.rand.nextFloat() < f) {
                if(activePlayerStack.getItem() instanceof NewShieldItem || activePlayerStack.getItem() instanceof ShieldItem){
                    int ticks = EnchantmentUtil.getRecovery(player);
                    player.getCooldownTracker().setCooldown(Items.SHIELD, ticks);
                    player.getCooldownTracker().setCooldown(ItemRegistry.SERPENT_SHIELD.get(), ticks);
                }
                this.world.setEntityState(player, (byte)30);
            }
        }
    }

    private MobEntity getMobEntity(Object o){
        return (MobEntity)o;
    }

}
