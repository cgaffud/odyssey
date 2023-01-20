package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectHolder;
import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import com.bedmen.odyssey.tools.OdysseyTiers;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.bedmen.odyssey.combat.MeleeWeaponClass;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AspectMeleeItem extends TieredItem implements Vanishable, AspectItem, DyeableLeatherItem {
    /** Modifiers applied when the item is in the mainhand of a user. */
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private final AspectHolder aspectHolder;

    public AspectMeleeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> additionalAbilityList, List<AspectInstance> innateModifierList) {
        super(tier, properties);
        List<AspectInstance> fullAbilityList = new ArrayList<>(meleeWeaponClass.aspectInstanceList);
        fullAbilityList.addAll(additionalAbilityList);
        this.aspectHolder = new AspectHolder(fullAbilityList, innateModifierList);
        float attackDamage = damage + tier.getAttackDamageBonus();

        Multimap<Attribute, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();
        attributeModifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        attributeModifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)meleeWeaponClass.attackRate - 4.0d, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = attributeModifiers;
    }

    public AspectHolder getAspectHolder() {
        return this.aspectHolder;
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
        return blockState.is(Blocks.COBWEB) && AspectUtil.hasBooleanAspect(this.getDefaultInstance(), Aspects.COBWEB_BREAK);
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
        ConditionalAmpUtil.setDamageTag(itemStack, entity);
        super.inventoryTick(itemStack, level, entity, compartments, selected);
    }
}
