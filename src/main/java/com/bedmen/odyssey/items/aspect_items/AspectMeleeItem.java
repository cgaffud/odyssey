package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.MeleeWeaponClass;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.bedmen.odyssey.tier.OdysseyTiers;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class AspectMeleeItem extends TieredItem implements Vanishable, InnateAspectItem, OdysseyMeleeItem, ParryableWeaponItem, INeedsToRegisterItemModelProperty {
    /** Modifiers applied when the item is in the mainhand of a user. */
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private final InnateAspectHolder innateAspectHolder;
    protected final MeleeWeaponClass meleeWeaponClass;

    public AspectMeleeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> additionalAbilityList, List<AspectInstance> innateModifierList) {
        super(tier, properties);
        List<AspectInstance> fullAbilityList = new ArrayList<>(meleeWeaponClass.aspectInstanceList);
        fullAbilityList.addAll(additionalAbilityList);
        this.innateAspectHolder = new InnateAspectHolder(fullAbilityList, innateModifierList);
        this.meleeWeaponClass = meleeWeaponClass;
        float attackDamage = damage + tier.getAttackDamageBonus();

        Multimap<Attribute, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();
        attributeModifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        attributeModifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)meleeWeaponClass.attackRate - 4.0d, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = attributeModifiers;
    }

    public InnateAspectHolder getInnateAspectHolder() {
        return this.innateAspectHolder;
    }

    public MeleeWeaponClass getMeleeWeaponClass(){
        return this.meleeWeaponClass;
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
        return (blockState.getBlock() instanceof WebBlock) && AspectUtil.hasBooleanAspect(this.getDefaultInstance(), Aspects.COBWEB_BREAK);
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
        return ConditionalAmpUtil.getAttributeModifiersWithAdjustedAttackDamage(equipmentSlot, itemStack, this.attributeModifiers);
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
        ConditionalAmpUtil.setDamageTag(itemStack, entity);
        super.inventoryTick(itemStack, level, entity, compartments, selected);
    }

    /**  Melee Parry Mechanics */

    public boolean canParry() {
        return this.meleeWeaponClass.recoveryTime >= 0;
    }

    public UseAnim getUseAnimation(ItemStack itemStack) {
        return (this.canParry()) ? UseAnim.CUSTOM : UseAnim.NONE;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
    }

    public float getDamageBlock(ItemStack melee, DamageSource damageSource){
        return this.meleeWeaponClass.damageBlock + AspectUtil.getDamageSourcePredicateAspectStrength(melee, damageSource);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if (this.canParry() && !WeaponUtil.getItemInOtherHand(player, interactionHand).is(OdysseyItemTags.PARRYABLES)) {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemstack);
        } else return super.use(level, player, interactionHand);
    }

    public int getUseDuration(ItemStack p_43107_) {
        return this.canParry() ? 72000 : 0;
    }

    public int getRecoveryTime(ItemStack shield){
        float recoverySpeedMultiplier = 1.0f + AspectUtil.getFloatAspectStrength(shield, Aspects.RECOVERY_SPEED);
        return Mth.ceil((float)this.meleeWeaponClass.recoveryTime / recoverySpeedMultiplier);
    }

    public void registerItemModelProperties(){
        ItemProperties.register(this, new ResourceLocation("blocking"), (itemStack, clientLevel, livingEntity, i) -> (livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack) ? 1.0F : 0.0F);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            // This should be moved to a separate file if we want all poses in a clean location, but somehow all those poses need to be
            // declared before this command runs, otherwise the game shits itself. For now, this is a clean way of doing this
            public static final HumanoidModel.ArmPose MELEE_BLOCK = HumanoidModel.ArmPose.create("melee_block", false, (model, entity, arm) -> {
                if (arm == HumanoidArm.RIGHT) {
                    model.rightArm.xRot = -Mth.HALF_PI*5/6;
                    model.rightArm.yRot = -Mth.HALF_PI*3/8;
                    model.rightArm.zRot = Mth.HALF_PI/4;
                } else {
                    model.leftArm.xRot = -Mth.HALF_PI*5/6;
                    model.leftArm.yRot = Mth.HALF_PI*3/8;
                    model.leftArm.zRot = -Mth.HALF_PI/4;
                }
            });

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (!itemStack.isEmpty()) {
                    if (entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0) {
                        return MELEE_BLOCK;
                    }
                }
                return HumanoidModel.ArmPose.EMPTY;
            }

        });
    }
}
