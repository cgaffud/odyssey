package com.bedmen.odyssey.items;

import com.bedmen.odyssey.items.equipment.base.IEquipment;
import com.bedmen.odyssey.tools.OdysseyTiers;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.bedmen.odyssey.weapon.MeleeWeaponAbility;
import com.bedmen.odyssey.weapon.MeleeWeaponClass;
import com.bedmen.odyssey.weapon.WeaponUtil;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OdysseyMeleeItem extends TieredItem implements Vanishable {
    /** Modifiers applied when the item is in the mainhand of a user. */
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers;
    public final MeleeWeaponClass meleeWeaponClass;

    public OdysseyMeleeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage) {
        super(tier, properties);
        this.meleeWeaponClass = meleeWeaponClass;
        float attackDamage = damage + tier.getAttackDamageBonus();

        Multimap<Attribute, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();
        attributeModifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        attributeModifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)meleeWeaponClass.attackRate - 4.0d, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = attributeModifiers;
    }

    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        if (this.isCorrectToolForDrops(blockState)) {
            return 15.0F;
        } else {
            Material material = blockState.getMaterial();
            return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && !blockState.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
        }
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(2, entityLiving, (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

    public boolean isCorrectToolForDrops(BlockState blockState) {
        return blockState.is(Blocks.COBWEB) && this.meleeWeaponClass.hasAbility(MeleeWeaponAbility.COBWEB_BREAK);
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return false;
    }

    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType)
    {
        return this.getTier() == OdysseyTiers.WOOD ? 200 : 0;
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if(entity instanceof Player player && WeaponUtil.isDualWielding(player) && player.getRandom().nextBoolean()){
            player.getOffhandItem().hurtAndBreak(amount, entity, (p_41007_) -> {
                p_41007_.broadcastBreakEvent(EquipmentSlot.OFFHAND);
            });
            return 0;
        }
        return super.damageItem(stack, amount, entity, onBroken);
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(1, this.meleeWeaponClass.advancedTooltipAbilityList);
        } else {
            tooltip.addAll(1, this.meleeWeaponClass.tooltipAbilityList);
        }
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack itemStack)
    {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            Multimap<Attribute, AttributeModifier> stackAttributeModifiers = LinkedHashMultimap.create();
            float conditionalAmpBonus = ConditionalAmpUtil.getDamageTag(itemStack);
            for(Map.Entry<Attribute, Collection<AttributeModifier>> entry : this.attributeModifiers.asMap().entrySet()){
                if (entry.getKey() == Attributes.ATTACK_DAMAGE && conditionalAmpBonus > 0.0f) {
                    Collection<AttributeModifier> newDamageModifiers = entry.getValue().stream()
                            .map(attributeModifier -> {
                                if (attributeModifier.getId() == BASE_ATTACK_DAMAGE_UUID) {
                                    return new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attributeModifier.getAmount() + conditionalAmpBonus, AttributeModifier.Operation.ADDITION);
                                }
                                return attributeModifier;
                            })
                            .collect(Collectors.toSet());
                    stackAttributeModifiers.putAll(entry.getKey(), newDamageModifiers);
                } else {
                    stackAttributeModifiers.putAll(entry.getKey(), entry.getValue());
                }
            }
            return stackAttributeModifiers;
        }
        return super.getAttributeModifiers(equipmentSlot, itemStack);
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
        ConditionalAmpUtil.setDamageTag(itemStack, entity, true);
        super.inventoryTick(itemStack, level, entity, compartments, selected);
    }
}
