package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.client.renderer.blockentity.OdysseyBlockEntityWithoutLevelRenderer;
import com.bedmen.odyssey.combat.MeleeWeaponClass;
import com.bedmen.odyssey.combat.SpearType;
import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.entity.projectile.ThrownSpear;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class SpearItem extends ThrowableWeaponItem implements OdysseyMeleeItem {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    protected final MeleeWeaponClass meleeWeaponClass;

    public SpearItem(Item.Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, SpearType spearType) {
        super(properties, tier, spearType, meleeWeaponClass.aspectInstanceList);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", spearType.thrownDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", meleeWeaponClass.attackRate-4.0f, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
        this.meleeWeaponClass = meleeWeaponClass;
    }

    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        itemStack.hurtAndBreak(1, attacker, (p_43414_) -> {
            p_43414_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        if ((double)blockState.getDestroySpeed(level, blockPos) != 0.0D) {
            itemStack.hurtAndBreak(2, livingEntity, (p_43385_) -> {
                p_43385_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack itemStack)
    {
        return ConditionalAmpUtil.getAttributeModifiersWithAdjustedAttackDamage(equipmentSlot, itemStack, this.defaultModifiers);
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
        ConditionalAmpUtil.setDamageTag(itemStack, entity);
        super.inventoryTick(itemStack, level, entity, compartments, selected);
    }

    protected OdysseyAbstractArrow getThrownWeaponEntity(Level level, LivingEntity owner, ItemStack thrownWeaponStack, boolean isMultishot) {
        return new ThrownSpear(level, owner, thrownWeaponStack, isMultishot);
    }

    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return OdysseyBlockEntityWithoutLevelRenderer.getInstance();
            }
        });
    }

    public SpearType getSpearType(){
        return (SpearType)this.throwableType;
    }

    public MeleeWeaponClass getMeleeWeaponClass(){
        return this.meleeWeaponClass;
    }
}
