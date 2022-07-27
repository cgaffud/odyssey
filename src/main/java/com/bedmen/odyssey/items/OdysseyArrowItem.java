package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.monster.Weaver;
import com.bedmen.odyssey.entity.projectile.OdysseyArrow;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class OdysseyArrowItem extends ArrowItem {
    private final OdysseyArrow.ArrowType arrowType;
    public OdysseyArrowItem(Item.Properties p_i48464_1_, OdysseyArrow.ArrowType arrowType) {
        super(p_i48464_1_);
        this.arrowType = arrowType;
    }

    public AbstractArrow createArrow(Level world, ItemStack ammo, LivingEntity livingEntity) {
        OdysseyArrow odysseyArrow = new OdysseyArrow(world, livingEntity, arrowType);
        odysseyArrow.setLootingLevel((byte)this.arrowType.getLooting());
        return odysseyArrow;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.oddc.arrow.damage").append(StringUtil.doubleFormat(this.arrowType.getDamage())).withStyle(ChatFormatting.BLUE));
        int looting = this.arrowType.getLooting();
        if(looting > 0){
            tooltip.add(Enchantments.MOB_LOOTING.getFullname(looting));
        }
        if(this.arrowType == OdysseyArrow.ArrowType.WEAVER_FANG){
            tooltip.add(new TranslatableComponent("item.oddc.weaver_fang.web_chance").append(StringUtil.percentFormat(Weaver.WEB_ATTACK_CHANCE * OdysseyArrow.WEAVER_FANG_ARROW_WEB_AMPLIFY)).withStyle(ChatFormatting.BLUE));
        }
    }
}