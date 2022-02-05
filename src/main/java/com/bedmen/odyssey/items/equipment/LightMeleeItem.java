package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

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

    /**TODO: add new moon check*/
    public boolean isActive(Level level, LivingEntity livingEntity) {
        BlockPos eyeLevel = new BlockPos(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
        boolean correctTime = switch (this.time) {
            case DAY -> ((level.getDayTime() % 24000L) < 12000L);
            case NIGHT -> ((level.getDayTime() % 24000L) >= 12000L);
            case BOTH -> true;
        };
        return correctTime && level.canSeeSky(eyeLevel) && !level.isThundering() && !level.isRaining() && (level.dimension() == Level.OVERWORLD);
    }

    public static boolean isActive(ItemStack itemStack, Level level, LivingEntity livingEntity){
        if (itemStack.getItem() instanceof LightMeleeItem lightMeleeItem) {
            return lightMeleeItem.isActive(level, livingEntity);
        }
        return false;
    }

    public void registerItemModelProperties() {
        ItemProperties.register(this, new ResourceLocation("active"), (itemStack, clientLevel, livingEntity, i) -> {
            if ((livingEntity != null)  && this.isActive(itemStack, livingEntity.getLevel(), livingEntity)) {
                return 1.0F;
            }
            return 0.0F;
        });
    }

    private String getTimeHoverText(){
        return switch (this.time) {
            case DAY -> "Sunlight";
            case NIGHT -> "Moonlight";
            case BOTH -> "Light";
        };
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("item.oddc.light_melee_item.damage_modifier", StringUtil.doubleFormat(this.attackBoost), this.getTimeHoverText()).withStyle(ChatFormatting.BLUE));
    }
}
