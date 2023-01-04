package com.bedmen.odyssey.items;

import com.bedmen.odyssey.items.equipment.base.IEquipment;
import com.bedmen.odyssey.tools.OdysseyTiers;
import com.bedmen.odyssey.util.OdysseyChatFormatting;
import com.bedmen.odyssey.util.WeaponUtil;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.BlockTags;
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
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class OdysseyMeleeItem extends TieredItem implements Vanishable, IEquipment {
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

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction)
    {
        if(toolAction == ToolActions.SWORD_SWEEP){
            return this.meleeWeaponClass.canSweep;
        }
        return super.canPerformAction(stack, toolAction);
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
        return blockState.is(Blocks.COBWEB) && this.meleeWeaponClass.canSweep;
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

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        if (WeaponUtil.isDualWieldItem(itemStack)) {
            tooltip.add(new TranslatableComponent("item.oddc.dualwield").withStyle(OdysseyChatFormatting.LAVENDER));
        }
        this.appendInnateEnchantments(tooltip, flagIn);
    }
}
