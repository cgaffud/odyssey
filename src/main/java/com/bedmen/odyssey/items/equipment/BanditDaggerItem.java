package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.items.MeleeWeaponClass;
import com.bedmen.odyssey.items.equipment.base.EquipmentMeleeItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class BanditDaggerItem extends EquipmentMeleeItem {
    private static final float EMERALD_CHANCE = 0.1f;

    public BanditDaggerItem(Properties builderIn, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, LevEnchSup... levEnchSups) {
        super(builderIn, tier, meleeWeaponClass, damage, levEnchSups);
    }

    public static void performKill(ItemStack itemStack, LivingEntity target){
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        String key = target instanceof AbstractVillager ? "VillagerCount" : "IllagerCount";
        compoundTag.putInt(key, compoundTag.getInt(key) + 1);
        if (target.getRandom().nextFloat() < EMERALD_CHANCE) {
            compoundTag.putInt("EmeraldCount", compoundTag.getInt("EmeraldCount") + 1);
            target.spawnAtLocation(Items.EMERALD);
        }
        itemStack.setTag(compoundTag);
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(itemStack, level, tooltip, flagIn);
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        int villagerCount = compoundTag.getInt("VillagerCount");
        int illagerCount = compoundTag.getInt("IllagerCount");
        int emeraldCount = compoundTag.getInt("EmeraldCount");
        tooltip.add(new TranslatableComponent("item.oddc.bandit_dagger.villager_count").append(Integer.toString(villagerCount)).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.bandit_dagger.illager_count").append(Integer.toString(illagerCount)).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.bandit_dagger.emerald_count").append(Integer.toString(emeraldCount)).withStyle(ChatFormatting.BLUE));
    }
}
