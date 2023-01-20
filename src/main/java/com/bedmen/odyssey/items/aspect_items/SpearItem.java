//package com.bedmen.odyssey.items.aspect_items;
//
//import com.bedmen.odyssey.aspect.AspectHolder;
//import com.bedmen.odyssey.combat.OdysseyRangedWeapon;
//import com.bedmen.odyssey.combat.SpearType;
//import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
//import com.google.common.collect.ImmutableMultimap;
//import com.google.common.collect.Multimap;
//import net.minecraft.client.renderer.item.ItemProperties;
//import net.minecraft.core.BlockPos;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.Attribute;
//import net.minecraft.world.entity.ai.attributes.AttributeModifier;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Vanishable;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//
//public class SpearItem extends Item implements Vanishable, INeedsToRegisterItemModelProperty, OdysseyRangedWeapon, AspectItem {
//    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
//    public final SpearType spearType;
//
//    public SpearItem(Item.Properties properties, SpearType spearType) {
//        super(properties);
//        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
//        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", spearType.damage, AttributeModifier.Operation.ADDITION));
//        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.9F, AttributeModifier.Operation.ADDITION));
//        this.defaultModifiers = builder.build();
//        this.spearType = spearType;
//    }
//
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
//        ItemStack itemstack = player.getItemInHand(interactionHand);
//        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
//            return InteractionResultHolder.fail(itemstack);
//        } else {
//            player.startUsingItem(interactionHand);
//            return InteractionResultHolder.consume(itemstack);
//        }
//    }
//
//    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
//        itemStack.hurtAndBreak(1, attacker, (p_43414_) -> {
//            p_43414_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
//        });
//        return true;
//    }
//
//    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
//        if ((double)blockState.getDestroySpeed(level, blockPos) != 0.0D) {
//            itemStack.hurtAndBreak(2, livingEntity, (p_43385_) -> {
//                p_43385_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
//            });
//        }
//
//        return true;
//    }
//
//    public AspectHolder getAspectHolder() {
//        return this.spearType.aspectHolder;
//    }
//
//    public void registerItemModelProperties(){
//        ItemProperties.register(this, new ResourceLocation("throwing"), (itemStack, clientLevel, livingEntity, i) -> {
//            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
//        });
//    }
//
//    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
//        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
//    }
//}
