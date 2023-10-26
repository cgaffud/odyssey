package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolderType;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.AspectOwner;
import com.bedmen.odyssey.aspect.object.*;
import com.bedmen.odyssey.aspect.query.AspectQuery;
import com.bedmen.odyssey.aspect.query.FunctionQuery;
import com.bedmen.odyssey.aspect.query.SingleQuery;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipDisplaySetting;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.items.OdysseyTierItem;
import com.bedmen.odyssey.items.aspect_items.AspectArmorItem;
import com.bedmen.odyssey.util.StringUtil;
import com.google.common.collect.Multimap;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AspectUtil {

    private static final String ADDED_MODIFIERS_TAG = Odyssey.MOD_ID + ":AddedModifiers";
    private static final String SET_BONUS_STRING = "SET_BONUS";
    private static final List<EquipmentSlot> ARMOR_EQUIPMENT_SLOT_LIST = Arrays.stream(EquipmentSlot.values()).filter(equipmentSlot -> equipmentSlot.getType() == EquipmentSlot.Type.ARMOR).collect(Collectors.toList());
    private static final MutableComponent OBFUSCATED_TOOLTIP = Component.literal("AAAAAAAAAA").withStyle(ChatFormatting.OBFUSCATED).withStyle(ChatFormatting.DARK_RED);

    // Tags specific to aspect behavior
    public static final String STORED_BOOST_TAG = Odyssey.MOD_ID + ":AspectStoredBoost";
    public static final String DAMAGE_GROWTH_TAG = Odyssey.MOD_ID + ":AspectDamageGrowth";

    public static AspectHolder getAddedModifierHolder(ItemStack itemStack){
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if(!compoundTag.contains(ADDED_MODIFIERS_TAG)){
            compoundTag.put(ADDED_MODIFIERS_TAG, new AspectHolder(new ArrayList<>()).toCompoundTag());
        }
        compoundTag = compoundTag.getCompound(ADDED_MODIFIERS_TAG);
        return AspectHolder.fromCompoundTag(compoundTag);
    }

    public static List<AspectInstance<?>> getAddedModifierList(ItemStack itemStack){
        return new ArrayList<>(getAddedModifierHolder(itemStack).map.values());
    }

    private static <T> T queryItemStack(ItemStack itemStack, AspectQuery<T> aspectQuery){
        if(itemStack.isEmpty()){
            return aspectQuery.base;
        }
        T value = aspectQuery.query(getAddedModifierHolder(itemStack));
        if(itemStack.getItem() instanceof AspectOwner aspectOwner){
            aspectQuery.addition.apply(value, queryOwnedAspects(aspectOwner, aspectQuery));
        }
        return value;
    }

    private static float queryBonusDamageAspect(Optional<LivingEntity> optionalLivingEntity, ItemStack itemStack, Function<AspectInstance<?>, Float> valueFunction){
        FunctionQuery<Float> functionQuery = FunctionQuery.floatQuery.apply(aspectInstance -> {
            if(aspectInstance.aspect instanceof BonusDamageAspect){
                return valueFunction.apply(aspectInstance) * BonusDamageAspect.getStrengthAmplifier(itemStack.getItem());
            }
            return 0.0f;
        });
        if(optionalLivingEntity.isPresent()){
            return queryOneHandedEntityTotal(optionalLivingEntity.get(), InteractionHand.MAIN_HAND, functionQuery);
        } else {
            return queryItemStack(itemStack, functionQuery);
        }
    }

    private static <T> T queryArmor(LivingEntity livingEntity, AspectQuery<T> aspectQuery){
        T value = aspectQuery.base;
        for(ItemStack armorPiece: livingEntity.getArmorSlots()){
            value = aspectQuery.addition.apply(value, queryItemStack(armorPiece, aspectQuery));
        }
        if(isFullArmorSet(livingEntity.getArmorSlots()) && livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof AspectArmorItem aspectArmorItem){
            value = aspectQuery.addition.apply(value, aspectQuery.query(aspectArmorItem.getSetBonusAbilityHolder()));
        }
        return value;
    }

    private static <T> T queryOwnedAspects(AspectOwner aspectOwner, AspectQuery<T> aspectQuery){
        T value = aspectQuery.base;
        for(AspectHolder aspectHolder : aspectOwner.getAspectHolderList()){
            value = aspectQuery.addition.apply(value, aspectQuery.query(aspectHolder));
        }
        return value;
    }

    private static <T> T queryArmorAndBuffs(LivingEntity livingEntity, AspectQuery<T> aspectQuery){
        T value = queryArmor(livingEntity, aspectQuery);
        if(livingEntity instanceof AspectOwner aspectOwner){
            value = aspectQuery.addition.apply(value, queryOwnedAspects(aspectOwner, aspectQuery));
        }
        return value;
    }

    private static <T> T queryOneHandedEntityTotal(LivingEntity livingEntity, InteractionHand interactionHand, AspectQuery<T> aspectQuery){
        return aspectQuery.addition.apply(queryArmorAndBuffs(livingEntity, aspectQuery), queryItemStack(livingEntity.getItemInHand(interactionHand), aspectQuery));
    }

    private static <T> T queryEntityTotal(LivingEntity livingEntity, AspectQuery<T> aspectQuery){
        return aspectQuery.addition.apply(
                aspectQuery.addition.apply(
                        queryItemStack(livingEntity.getItemInHand(InteractionHand.MAIN_HAND), aspectQuery),
                        queryItemStack(livingEntity.getItemInHand(InteractionHand.OFF_HAND), aspectQuery)
                ),
                queryArmorAndBuffs(livingEntity, aspectQuery)
        );
    }

    private static boolean isFullArmorSet(Iterable<ItemStack> armorPieces){
        int count = 0;
        ArmorMaterial firstArmorMaterial = null;
        for(ItemStack itemStack: armorPieces){
            if(itemStack.getItem() instanceof AspectArmorItem aspectArmorItem){
                ArmorMaterial armorMaterial = aspectArmorItem.getMaterial();
                if(firstArmorMaterial == null){
                    firstArmorMaterial = armorMaterial;
                }
                if(firstArmorMaterial == armorMaterial){
                    count++;
                }
            } else {
                return false;
            }
        }
        return count == 4;
    }

    // -- Public endpoints -----------------------------------------------------

    // Get added modifiers, abilities, and innate modifiers all in one aspect holder

    public static AspectHolder getCombinedAspectHolder(ItemStack itemStack){
        AspectHolder aspectHolder = getAddedModifierHolder(itemStack);
        if(itemStack.getItem() instanceof AspectOwner aspectOwner){
            for(AspectHolder aspectHolder1 : aspectOwner.getAspectHolderList()) {
                aspectHolder = AspectHolder.combine(aspectHolder, aspectHolder1);
            }
        }
        return aspectHolder;
    }

    public static boolean hasAddedModifiers(ItemStack itemStack){
        AspectHolder aspectHolder = getAddedModifierHolder(itemStack);
        return !aspectHolder.map.isEmpty();
    }

    // Get aspect value from single itemStack

    public static <T> T getItemStackAspectValue(ItemStack itemStack, Aspect<T> aspect){
        return queryItemStack(itemStack, new SingleQuery<>(aspect));
    }

    public static boolean itemStackHasAspect(ItemStack itemStack, Aspect<?> aspect){
        return getCombinedAspectHolder(itemStack).hasAspect(aspect);
    }

    public static <T> T getOwnedAspectValue(AspectOwner aspectOwner, Aspect<T> aspect){
        return queryOwnedAspects(aspectOwner, new SingleQuery<>(aspect));
    }

    // Special totals over multiple aspects on single item
    public static float getTargetConditionalAspectStrength(LivingEntity attacker, LivingEntity target){
        return queryBonusDamageAspect(Optional.of(attacker), attacker.getMainHandItem(), aspectInstance -> {
            if (aspectInstance.aspect.equals(Aspects.SOLAR_STRENGTH) || aspectInstance.aspect.equals(Aspects.LUNAR_STRENGTH)) {
                CompoundTag tag = attacker.getMainHandItem().getOrCreateTag();
                int charge = tag.getInt(AspectUtil.STORED_BOOST_TAG);
                if (charge >= 10) tag.putInt(AspectUtil.STORED_BOOST_TAG, charge-10);
            }

            if(aspectInstance.aspect instanceof TargetConditionalMeleeAspect targetConditionalMeleeAspect){
                return targetConditionalMeleeAspect.livingEntityPredicate.test(target) ? (Float)aspectInstance.value : 0.0f;
            }
            return 0.0f;
        });
    }


    public static float getConditionalAspectValue(ItemStack itemStack, BlockPos blockPos, Level level){
        return queryBonusDamageAspect(Optional.empty(), itemStack, aspectInstance -> {
            if(aspectInstance.aspect instanceof ConditionalAmpAspect conditionalAmpAspect){
                return conditionalAmpAspect.attackBoostFactorFunction.getBoostFactor(itemStack, blockPos, level)
                        * BonusDamageAspect.getStrengthAmplifier(itemStack.getItem());
            }
            return 0.0f;
        });
    }

    public static float getShieldDamageBlockAspectValue(ItemStack itemStack, DamageSource damageSource){
        return queryItemStack(itemStack, FunctionQuery.floatQuery.apply(aspectInstance -> {
            if(aspectInstance.aspect instanceof ShieldDamageBlockAspect shieldDamageBlockAspect){
                return shieldDamageBlockAspect.damageSourcePredicate.test(damageSource) ? (Float)aspectInstance.value : 0.0f;
            }
            return 0.0f;
        }));
    }

    // Get total value from armor
    public static <T> T getArmorAspectValue(LivingEntity livingEntity, Aspect<T> aspect){
        return queryArmor(livingEntity, new SingleQuery<>(aspect));
    }

    // Get total value from armor and entity
    public static <T> T getArmorAndBuffsAspectValue(LivingEntity livingEntity, Aspect<T> aspect){
        return queryArmorAndBuffs(livingEntity, new SingleQuery<>(aspect));
    }

    // Special totals over multiple aspects on armor
    public static float getProtectionAspectValue(LivingEntity livingEntity, DamageSource damageSource){
        return queryArmorAndBuffs(livingEntity, FunctionQuery.floatQuery.apply(aspectInstance -> {
            if(aspectInstance.aspect instanceof DamageSourcePredicateAspect damageSourcePredicateAspect){
                return damageSourcePredicateAspect.damageSourcePredicate.test(damageSource) ? (Float)aspectInstance.value : 0.0f;
            }
            return 0.0f;
        }));
    }

    // Aspect total over one hand, armor, and player
    public static <T> T getOneHandedEntityTotalAspectValue(LivingEntity livingEntity, InteractionHand interactionHand, Aspect<T> aspect){
        return queryOneHandedEntityTotal(livingEntity, interactionHand, new SingleQuery<>(aspect));
    }

    // Aspect total over all EquipmentSlots and player
    public static <T> T getTotalAspectValue(LivingEntity livingEntity, Aspect<T> aspect){
        return queryEntityTotal(livingEntity, new SingleQuery<>(aspect));
    }

    // Add AspectInstance to a list of AspectInstances
    public static void addInstance(List<AspectInstance<?>> aspectInstanceList, AspectInstance<?> aspectInstance){
        AspectInstance<?> match = null;
        for(AspectInstance<?> aspectInstance1: aspectInstanceList){
            if(aspectInstance1.aspect == aspectInstance.aspect){
                match = aspectInstance1;
                break;
            }
        }
        if(match == null){
            aspectInstanceList.add(aspectInstance);
        } else {
            int index = aspectInstanceList.indexOf(match);
            aspectInstanceList.remove(match);
            AspectInstance<?> newAspectInstance = match.withAddedValue(match.aspect.getValueClass().cast(aspectInstance.value));
            aspectInstanceList.add(index, newAspectInstance);
        }

    }

    // Tooltips

    public static void addAddedModifierTooltip(ItemStack itemStack, List<Component> tooltip, TooltipFlag tooltipFlag, AspectTooltipContext aspectTooltipContext){
        AspectHolder addedModifierHolder = AspectUtil.getAddedModifierHolder(itemStack);
        addedModifierHolder.addTooltip(tooltip, tooltipFlag, aspectTooltipContext);
        int totalModifiability =  getTotalModifiability(itemStack);
        if(tooltipFlag.isAdvanced() && totalModifiability > 0){
            tooltip.add(Component.translatable("aspect_tooltip.oddc.modifiability_remaining", StringUtil.floatFormat(getModifiabilityRemaining(itemStack)), totalModifiability).withStyle(AspectHolderType.ADDED_MODIFIER.color));
        }
    }

    public static List<Component> getTooltip(AspectTooltipContext context){
        List<Component> componentList = new ArrayList<>(context.aspectInstanceMap.values().stream()
                .filter(aspectInstance -> context.isAdvanced ? aspectInstance.aspectTooltipDisplaySetting != AspectTooltipDisplaySetting.NEVER : aspectInstance.aspectTooltipDisplaySetting == AspectTooltipDisplaySetting.ALWAYS)
                .map(aspectInstance ->
                        Component.literal(context.optionalHeader.isPresent() ? " " : "")
                                .append(aspectInstance.obfuscated ? OBFUSCATED_TOOLTIP : aspectInstance.getMutableComponent(context)).withStyle(context.chatFormatting))
                .toList());
        if(componentList.isEmpty()){
            return componentList;
        }
        context.optionalHeader.ifPresent(header -> componentList.add(0, header.withStyle(context.chatFormatting)));
        return componentList;
    }

    public static List<Component> getBuffTooltip(Player player){
        if(player instanceof OdysseyLivingEntity odysseyLivingEntity){
            AspectTooltipContext aspectTooltipContext = new AspectTooltipContext(Optional.empty());
            List<Component> componentList = new ArrayList<>();
            odysseyLivingEntity.getPermaBuffHolder().addTooltip(componentList, TooltipFlag.Default.ADVANCED, aspectTooltipContext);
            odysseyLivingEntity.getTempBuffHolder().addTooltip(componentList, TooltipFlag.Default.ADVANCED, aspectTooltipContext);
            return componentList;
        } else {
            return List.of();
        }
    }

    // Attributes
    public static void fillAttributeMultimaps(LivingEntity livingEntity, ItemStack oldItemStack, ItemStack newItemStack, EquipmentSlot equipmentSlot, Multimap<Attribute, AttributeModifier> oldMultimap, Multimap<Attribute, AttributeModifier> newMultimap){
        String uuidStart = equipmentSlot.getName();
        fillAttributeMultimap(getCombinedAspectHolder(oldItemStack), uuidStart, oldMultimap);
        fillAttributeMultimap(getCombinedAspectHolder(newItemStack), uuidStart, newMultimap);
        if(equipmentSlot.getType() == EquipmentSlot.Type.ARMOR){
            List<ItemStack> oldArmorPieces = ARMOR_EQUIPMENT_SLOT_LIST.stream().map(livingEntity::getLastArmorItem).collect(Collectors.toList());
            List<ItemStack> newArmorPieces = ARMOR_EQUIPMENT_SLOT_LIST.stream().map(livingEntity::getItemBySlot).collect(Collectors.toList());
            if(isFullArmorSet(oldArmorPieces) && oldArmorPieces.get(0).getItem() instanceof AspectArmorItem aspectArmorItem){
                aspectArmorItem.getSetBonusAbilityHolder().map.forEach((aspect, aspectInstance) -> {
                    if (aspect instanceof AttributeAspect attributeAspect) {
                        oldMultimap.put(attributeAspect.getAttribute(), new AttributeModifier(new UUID (SET_BONUS_STRING.hashCode(), attributeAspect.getAttribute().getDescriptionId().hashCode()), attributeAspect.id, (Float)aspectInstance.value, attributeAspect.operation));
                    }
                });
            }
            livingEntity.setItemSlot(equipmentSlot, newItemStack);
            if(isFullArmorSet(newArmorPieces) && newArmorPieces.get(0).getItem() instanceof AspectArmorItem aspectArmorItem){
                aspectArmorItem.getSetBonusAbilityHolder().map.forEach((aspect, aspectInstance) -> {
                    if (aspect instanceof AttributeAspect attributeAspect) {
                        newMultimap.put(attributeAspect.getAttribute(), new AttributeModifier(new UUID (SET_BONUS_STRING.hashCode(), attributeAspect.getAttribute().getDescriptionId().hashCode()), attributeAspect.id, (Float)aspectInstance.value, attributeAspect.operation));
                    }
                });
            }
        }
    }

    public static void fillAttributeMultimap(AspectHolder aspectHolder, String uuidStart, Multimap<Attribute, AttributeModifier> multimap){
        aspectHolder.map.forEach((aspect, aspectInstance) -> {
            if (aspect instanceof AttributeAspect attributeAspect) {
                multimap.put(attributeAspect.getAttribute(), new AttributeModifier(new UUID (uuidStart.hashCode(), attributeAspect.getAttribute().getDescriptionId().hashCode()), attributeAspect.id, (Float)aspectInstance.value, attributeAspect.operation));
            }
        });
    }

    // Add/Remove added modifiers

    public static float getUsedModifiability(ItemStack itemStack){
        AspectHolder aspectHolder = getAddedModifierHolder(itemStack);
        FunctionQuery<Float> functionQuery = FunctionQuery.floatQuery.apply(AspectInstance::getModifiability);
        return functionQuery.query(aspectHolder);
    }

    public static int getTotalModifiability(ItemStack itemStack){
        if(itemStack.getItem() instanceof OdysseyTierItem odysseyTierItem){
            return odysseyTierItem.getTier().getEnchantmentValue();
        }
        return 0;
    }

    public static float getModifiabilityRemaining(ItemStack itemStack){
        return getTotalModifiability(itemStack) - getUsedModifiability(itemStack);
    }

    public static boolean canAddModifier(ItemStack itemStack, AspectInstance<?> aspectInstance){
        if(!aspectInstance.aspect.itemPredicate.test(itemStack.getItem())){
            return false;
        }
        ItemStack itemStackCopy = itemStack.copy();
        AspectUtil.replaceOrAddModifier(itemStackCopy, aspectInstance);
        return AspectUtil.getModifiabilityRemaining(itemStackCopy) >= 0f;
    }

    public static void addModifier(ItemStack itemStack, AspectInstance<?> aspectInstance){
        AspectInstance<?> existingAspectInstance = AspectUtil.getAddedModifierHolder(itemStack).map.get(aspectInstance.aspect);
        AspectInstance<?> newAspectInstance = aspectInstance.withAddedValue(existingAspectInstance.value);
        replaceOrAddModifier(itemStack, newAspectInstance);
    }

    // Replaces added modifier with same aspect as aspectInstance with aspectInstance,
    // or removes the modifier altogether if aspectInstance.strength is 0
    public static void replaceOrAddModifier(ItemStack itemStack, AspectInstance<?> aspectInstance){
        removeAddedModifier(itemStack, aspectInstance.aspect);
        // Just removes old modifier if the value is 0
        if(!aspectInstance.aspect.getBase().equals(aspectInstance.value)){
            AspectHolder aspectHolder = getAddedModifierHolder(itemStack);
            aspectHolder.map.put(aspectInstance.aspect, aspectInstance);
            itemStack.getOrCreateTag().put(ADDED_MODIFIERS_TAG, aspectHolder.toCompoundTag());
        }
    }

    public static void removeAddedModifier(ItemStack itemStack, Aspect<?> aspect){
        AspectHolder aspectHolder = getAddedModifierHolder(itemStack);
        if(aspectHolder.hasAspect(aspect)){
            aspectHolder.map.remove(aspect);
            itemStack.getOrCreateTag().put(ADDED_MODIFIERS_TAG, aspectHolder.toCompoundTag());
        }
    }

    public static void resetAddedModifiers(ItemStack itemStack){
        itemStack.getOrCreateTag().put(ADDED_MODIFIERS_TAG, new ListTag());
    }

    // Apply poison damage
    public static void applyPoisonDamage(LivingEntity target, int poisonStrength){
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 10 + (25 * poisonStrength), 0));
    }

    // Do frost snow particles
    public static void doFrostAspectParticles(Entity entity, int count){
        RandomSource randomSource = entity.level.random;
        double x = entity.getX();
        double y = entity.getY() + 0.5f * entity.getBbHeight();
        double z = entity.getZ();
        for(int i = 0; i < count; i++){
            for(int xi = -1; xi <= 1; xi++){
                for(int yi = -1; yi <= 1; yi++){
                    for(int zi = -1; zi <= 1; zi++){
                        if(!(xi == 0 && yi == 0 && zi == 0) && randomSource.nextBoolean()){
                            Vec3 velocity  = new Vec3(xi, yi, zi).add(getRandomSnowflakeVector(randomSource)).normalize().scale(0.3d);
                            entity.level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, velocity.x, velocity.y, velocity.z);
                        }
                    }
                }
            }
        }
    }

    private static Vec3 getRandomSnowflakeVector(RandomSource randomSource){
        return new Vec3(getRandomSnowflakeSpeed(randomSource), getRandomSnowflakeSpeed(randomSource), getRandomSnowflakeSpeed(randomSource));
    }

    private static double getRandomSnowflakeSpeed(RandomSource randomSource){
        return randomSource.nextDouble() * 0.4d - 0.2d;
    }

    // Do frost snow particles
    public static void doAirAspectParticles(Entity entity, int count){
        RandomSource randomSource = entity.level.random;
        double x = entity.getX();
        double y = entity.getY() + 0.5f * entity.getBbHeight();
        double z = entity.getZ();
        for(int i = 0; i < count; i++){
            for(int xi = -1; xi <= 1; xi++){
                for(int yi = -1; yi <= 1; yi++){
                    for(int zi = -1; zi <= 1; zi++){
                        if(!(xi == 0 && yi == 0 && zi == 0) && randomSource.nextBoolean()){
                            Vec3 velocity  = new Vec3(xi, yi, zi).add(getRandomSnowflakeVector(randomSource)).normalize().scale(0.3d);
                            entity.level.addParticle(new DustParticleOptions(new Vector3f(0.85f, 0.85f, 0.85f), 1.0F),
                                    x + (randomSource.nextFloat()-0.5f)*entity.getBbWidth()/2,
                                    y+ (randomSource.nextFloat()-0.5f)*entity.getBbHeight()/2,
                                    z+ (randomSource.nextFloat()-0.5f)*entity.getBbWidth()/2,
                                    velocity.x, velocity.y, velocity.z);
                        }
                    }
                }
            }
        }
    }

    // Volatility
    public static void doVolatileExplosion(ServerLevel serverLevel, LivingEntity livingEntity, float strength){
        if(livingEntity.isAlive()){
            Explosion.BlockInteraction explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(serverLevel, livingEntity) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
            float f = livingEntity.yHeadRot * (float)Math.PI / 180.0f;
            serverLevel.explode(null, livingEntity.getX() - Mth.sin(f)*0.2f, livingEntity.getEyeY(), livingEntity.getZ() + Mth.cos(f)*0.2f, strength, false, explosion$mode);
        }
    }
}
