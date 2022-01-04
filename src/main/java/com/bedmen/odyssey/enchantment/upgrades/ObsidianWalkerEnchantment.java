package com.bedmen.odyssey.enchantment.upgrades;

import com.bedmen.odyssey.enchantment.IUpgradedEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractFrostWalkerEnchantment;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;

public class ObsidianWalkerEnchantment extends AbstractFrostWalkerEnchantment implements IUpgradedEnchantment {
    public ObsidianWalkerEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EquipmentSlotTypes);
    }

    public Enchantment getDowngrade(){
        return Enchantments.FROST_WALKER;
    }

    //TODO Implement this
    public static void onEntityMoved(LivingEntity p_45019_, Level p_45020_, BlockPos p_45021_, int p_45022_) {

    }
}
