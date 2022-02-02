package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class LightMeleeItem extends EquipmentMeleeItem implements INeedsToRegisterItemModelProperty {
    public float attackBoost;
    public enum TIME_ACTIVE{
        DAY, NIGHT, BOTH
    }

    public TIME_ACTIVE time;

    public LightMeleeItem(Tier tier, float attackDamageIn, float attackSpeedIn, float attackBoost, TIME_ACTIVE time, boolean canSweep, Properties builderIn, LevEnchSup... levEnchSups) {
        super(tier, attackDamageIn, attackSpeedIn, canSweep, builderIn, levEnchSups);
        this.attackBoost = attackBoost;
        this.time = time;
    }

    /**TODO: add dimension check, weather check, new moon check after rendering bug fixed*/
    public boolean isActive(Level level, LivingEntity livingEntity) {
        BlockPos eyeLevel = new BlockPos(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
        System.out.println(level.getDayTime());
        switch (this.time) {
            case DAY:
                //TIME_ACTIVE enum works properly
                //System.out.println("DAYFLAG");
                //System.out.println(livingEntity.level.canSeeSky(eyeLevel));
                return ((level.getDayTime() % 24000L) < 12000L) && livingEntity.level.canSeeSky(eyeLevel);
            case NIGHT:
                //System.out.println("NIGHTFLAG");
                return ((level.getDayTime() % 24000L) >= 12000L) && livingEntity.level.canSeeSky(eyeLevel);
            case BOTH :
            default:
                //System.out.println("BOTHFLAG");
                return livingEntity.level.canSeeSky(eyeLevel);
        }
    }

    public static boolean isActive(ItemStack itemStack, Level level, LivingEntity livingEntity){
        if (itemStack.getItem() instanceof LightMeleeItem lightMeleeItem) {
            System.out.println(lightMeleeItem.isActive(level, livingEntity));
            return lightMeleeItem.isActive(level, livingEntity);
        }
        return false;
    }

    public void registerItemModelProperties() {
        ItemProperties.register(this, new ResourceLocation("active"), (itemStack, clientLevel, livingEntity, i) -> {
            if ((livingEntity != null) && (clientLevel != null) && this.isActive(itemStack, clientLevel, livingEntity)) {
                //System.out.println("ACTIVE");
                return 1.0F;
            }
            return 0.0F;
        });
    }
}
