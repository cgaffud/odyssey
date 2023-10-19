package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.AspectStrengthMap;
import com.bedmen.odyssey.aspect.object.*;
import com.bedmen.odyssey.aspect.query.AspectQuery;
import com.bedmen.odyssey.aspect.query.FunctionQuery;
import com.bedmen.odyssey.aspect.query.SingleQuery;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipDisplaySetting;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.items.OdysseyTierItem;
import com.bedmen.odyssey.items.aspect_items.AspectArmorItem;
import com.bedmen.odyssey.items.aspect_items.InnateAspectItem;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.SwungWithVolatilePacket;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.bedmen.odyssey.util.StringUtil;
import com.google.common.collect.Multimap;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AspectUtil {

    private static final MutableComponent ADDED_MODIFIER_HEADER = Component.translatable("aspect_tooltip.oddc.added_modifiers");
    private static final ChatFormatting ADDED_MODIFIER_COLOR = ChatFormatting.GRAY;
    private static final String ADDED_MODIFIERS_TAG = Odyssey.MOD_ID + ":AddedModifiers";
    private static final String SET_BONUS_STRING = "SET_BONUS";
    private static final List<EquipmentSlot> ARMOR_EQUIPMENT_SLOT_LIST = Arrays.stream(EquipmentSlot.values()).filter(equipmentSlot -> equipmentSlot.getType() == EquipmentSlot.Type.ARMOR).collect(Collectors.toList());
    private static final MutableComponent OBFUSCATED_TOOLTIP = Component.literal("AAAAAAAAAA").withStyle(ChatFormatting.OBFUSCATED).withStyle(ChatFormatting.DARK_RED);

    // Tags specific to aspect behavior
    public static final String STORED_BOOST_TAG = Odyssey.MOD_ID + ":AspectStoredBoost";
    public static final String DAMAGE_GROWTH_TAG = Odyssey.MOD_ID + ":AspectDamageGrowth";

    private static ListTag getAddedModifierListTag(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        return compoundTag != null ? compoundTag.getList(ADDED_MODIFIERS_TAG, Tag.TAG_COMPOUND) : new ListTag();
    }

    private static AspectStrengthMap tagToMap(ListTag listTag){
        AspectStrengthMap map = new AspectStrengthMap();
        for(Tag tag: listTag){
            if(tag instanceof CompoundTag aspectTag){
                AspectInstance aspectInstance = AspectInstance.fromCompoundTag(aspectTag);
                if(aspectInstance != null){
                    map.put(aspectInstance.aspect, aspectInstance.strength);
                }
            }
        }
        return map;
    }

    private static List<AspectInstance> tagToAspectInstanceList(ListTag listTag){
        return listTag.stream()
                .filter(tag -> tag instanceof CompoundTag)
                .map(tag -> AspectInstance.fromCompoundTag((CompoundTag)tag))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static AspectStrengthMap getAddedModifierMap(ItemStack itemStack){
        return tagToMap(getAddedModifierListTag(itemStack));
    }

    private static float queryItemStackAspectStrength(ItemStack itemStack, AspectQuery aspectQuery){
        if(itemStack.isEmpty()){
            return 0.0f;
        }
        float total = aspectQuery.queryStrengthMap(getAddedModifierMap(itemStack));
        if(itemStack.getItem() instanceof InnateAspectItem innateAspectItem){
            total += aspectQuery.queryStrengthMap(innateAspectItem.getInnateAspectHolder().allAspectMap);
        }
        return total;
    }

    private static float getBonusDamageAspectStrength(LivingEntity livingEntity, Function<Aspect, Float> strengthFunction){
        FunctionQuery functionQuery = new FunctionQuery(aspect -> {
            if(aspect instanceof BonusDamageAspect){
                return strengthFunction.apply(aspect) * BonusDamageAspect.getStrengthAmplifier(livingEntity.getMainHandItem().getItem());
            }
            return 0.0f;
        });
        return queryOneHandedTotalAspectStrength(livingEntity, InteractionHand.MAIN_HAND, functionQuery);
    }

    private static float queryArmorAspectStrength(LivingEntity livingEntity, AspectQuery aspectQuery){
        float total = 0.0f;
        for(ItemStack armorPiece: livingEntity.getArmorSlots()){
            total += queryItemStackAspectStrength(armorPiece, aspectQuery);
        }
        if(isFullArmorSet(livingEntity.getArmorSlots()) && livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof AspectArmorItem aspectArmorItem){
            total += aspectQuery.queryStrengthMap(aspectArmorItem.getSetBonusAbilityHolder().map);
        }
        return total;
    }

    private static float queryBuffs(LivingEntity livingEntity, AspectQuery aspectQuery){
        float strength = 0f;
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            strength = aspectQuery.queryStrengthMap(odysseyLivingEntity.getPermaBuffHolder().aspectStrengthMap);
            strength += aspectQuery.queryStrengthMap(odysseyLivingEntity.getTempBuffHolder().aspectStrengthMap);
        }
        return strength;
    }

    private static float queryArmorAndEntityAspectStrength(LivingEntity livingEntity, AspectQuery aspectQuery){
        return queryArmorAspectStrength(livingEntity, aspectQuery)
                + queryBuffs(livingEntity, aspectQuery);
    }

    private static float queryOneHandedTotalAspectStrength(LivingEntity livingEntity, InteractionHand interactionHand, AspectQuery aspectQuery){
        return queryArmorAspectStrength(livingEntity, aspectQuery)
                + queryItemStackAspectStrength(livingEntity.getItemInHand(interactionHand), aspectQuery)
                + queryBuffs(livingEntity, aspectQuery);
    }

    private static float queryTotalAspectStrength(LivingEntity livingEntity, AspectQuery aspectQuery){
        return queryArmorAspectStrength(livingEntity, aspectQuery)
                + queryItemStackAspectStrength(livingEntity.getMainHandItem(), aspectQuery)
                + queryItemStackAspectStrength(livingEntity.getOffhandItem(), aspectQuery)
                + queryBuffs(livingEntity, aspectQuery);
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

    // Get AspectStrengthMap

    public static List<AspectInstance> getAddedModifiersAsAspectInstanceList(ItemStack itemStack){
        return tagToAspectInstanceList(getAddedModifierListTag(itemStack));
    }

    public static AspectStrengthMap getAspectStrengthMap(ItemStack itemStack){
        AspectStrengthMap addedModifierMap = getAddedModifierMap(itemStack);
        if(itemStack.getItem() instanceof InnateAspectItem innateAspectItem){
            return addedModifierMap.combine(innateAspectItem.getInnateAspectHolder().allAspectMap);
        }
        return addedModifierMap;
    }

    public static boolean hasAddedModifiers(ItemStack itemStack){
        AspectStrengthMap addedModifierMap = getAddedModifierMap(itemStack);
        return !addedModifierMap.isEmpty();
    }

    // Get aspect strength from single itemStack

    public static <T> T getItemStackAspectStrength(ItemStack itemStack, Aspect<T> aspect){
        float strength = queryItemStackAspectStrength(itemStack, new SingleQuery(aspect));
        return aspect.castStrength(strength);
    }

    public static boolean itemStackHasAspect(ItemStack itemStack, Aspect<?> aspect){
        float strength = queryItemStackAspectStrength(itemStack, new SingleQuery(aspect));
        return strength > 0f;
    }

    public static boolean itemStackHasEqualOrStrongerInstance(ItemStack itemStack, AspectInstance aspectInstance){
        float itemStackStrength = queryItemStackAspectStrength(itemStack, new SingleQuery(aspectInstance.aspect));
        return itemStackStrength >= aspectInstance.strength;
    }

    public static <T> T getBuffAspectStrength(Player player, Aspect<T> aspect){
        float strength = queryBuffs(player, new SingleQuery(aspect));
        return aspect.castStrength(strength);
    }

    // Special totals over multiple aspects on single item
    public static float getTargetConditionalAspectStrength(LivingEntity attacker, LivingEntity target){
        return getBonusDamageAspectStrength(attacker, aspect -> {
            if (aspect.equals(Aspects.SOLAR_STRENGTH) || aspect.equals(Aspects.LUNAR_STRENGTH)) {
                CompoundTag tag = attacker.getMainHandItem().getOrCreateTag();
                int charge = tag.getInt(AspectUtil.STORED_BOOST_TAG);
                if (charge >= 10) tag.putInt(AspectUtil.STORED_BOOST_TAG, charge-10);
            }

            if(aspect instanceof TargetConditionalMeleeAspect targetConditionalMeleeAspect){
                return targetConditionalMeleeAspect.livingEntityPredicate.test(target) ? 1.0f : 0.0f;
            }
            return 0.0f;
        });
    }

    public static float getEnvironmentalAspectStrength(LivingEntity attacker, BlockPos blockPos, Level level){
        return getBonusDamageAspectStrength(attacker, aspect -> {
            if(aspect instanceof EnvironmentConditionalAspect environmentConditionalAspect){
                return environmentConditionalAspect.attackBoostFactorFunction.getBoostFactor(blockPos, level);
            }
            return 0.0f;
        });
    }

    public static float getConditionalAspectStrength(LivingEntity attacker, BlockPos blockPos, Level level){
        return getBonusDamageAspectStrength(attacker, aspect -> {
            if(aspect instanceof ConditionalAmpAspect conditionalAmpAspect){
                return conditionalAmpAspect.attackBoostFactorFunction.getBoostFactor(attacker.getMainHandItem(), blockPos, level);
            }
            return 0.0f;
        });
    }

    public static float getShieldDamageBlockAspectStrength(ItemStack itemStack, DamageSource damageSource){
        return queryItemStackAspectStrength(itemStack, new FunctionQuery(aspect -> {
            if(aspect instanceof ShieldDamageBlockAspect shieldDamageBlockAspect){
                return shieldDamageBlockAspect.damageSourcePredicate.test(damageSource) ? 1.0f : 0.0f;
            }
            return 0.0f;
        }));
    }

    // Get total value from armor
    public static <T> T getArmorAspectStrength(LivingEntity livingEntity, Aspect<T> aspect){
        float strength = queryArmorAspectStrength(livingEntity, new SingleQuery(aspect));
        return aspect.castStrength(strength);
    }

    // Get total value from armor and entity
    public static <T> T getArmorAndEntityAspectStrength(LivingEntity livingEntity, Aspect<T> aspect){
        float strength = queryArmorAndEntityAspectStrength(livingEntity, new SingleQuery(aspect));
        return aspect.castStrength(strength);
    }

    // Special totals over multiple aspects on armor
    public static float getProtectionAspectStrength(LivingEntity livingEntity, DamageSource damageSource){
        return queryArmorAndEntityAspectStrength(livingEntity, new FunctionQuery(aspect -> {
            if(aspect instanceof DamageSourcePredicateAspect damageSourcePredicateAspect){
                return damageSourcePredicateAspect.damageSourcePredicate.test(damageSource) ? 1.0f : 0.0f;
            }
            return 0.0f;
        }));
    }

    // Aspect total over one hand, armor, and player
    public static <T> T getOneHandedTotalAspectStrength(LivingEntity livingEntity, InteractionHand interactionHand, Aspect<T> aspect){
        float strength = queryOneHandedTotalAspectStrength(livingEntity, interactionHand, new SingleQuery(aspect));
        return aspect.castStrength(strength);
    }

    // Aspect total over all EquipmentSlots and player
    public static <T> T getTotalAspectStrength(LivingEntity livingEntity, Aspect<T> aspect){
        float strength = queryTotalAspectStrength(livingEntity, new SingleQuery(aspect));
        return aspect.castStrength(strength);
    }

    // Add AspectInstance to a list of AspectInstances
    public static void addInstance(List<AspectInstance> aspectInstanceList, AspectInstance aspectInstance){
        AspectInstance match = null;
        for(AspectInstance aspectInstance1: aspectInstanceList){
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
            AspectInstance newAspectInstance = match.withAddedStrength(aspectInstance.strength);
            aspectInstanceList.add(index, newAspectInstance);
        }
    }

    // Tooltips

    public static void addAddedModifierTooltip(ItemStack itemStack, List<Component> tooltip, TooltipFlag tooltipFlag, AspectTooltipContext aspectTooltipContext){
        List<AspectInstance> addedModifiers = getAddedModifiersAsAspectInstanceList(itemStack);
        Optional<MutableComponent> optionalHeader = tooltipFlag.isAdvanced() ? Optional.of(ADDED_MODIFIER_HEADER) : Optional.empty();
        // The isAdvanced flag for getTooltip is for filtering aspectInstances based on their display properties
        // Here we want there to be a header or not based on the isAdvanced flag
        tooltip.addAll(getTooltip(aspectTooltipContext.withOtherContextVariables(addedModifiers, tooltipFlag.isAdvanced(), optionalHeader, ADDED_MODIFIER_COLOR)));
        int totalModifiability =  getTotalModifiability(itemStack);
        if(tooltipFlag.isAdvanced() && totalModifiability > 0){
            tooltip.add(Component.translatable("aspect_tooltip.oddc.modifiability_remaining", StringUtil.floatFormat(getModifiabilityRemaining(itemStack)), totalModifiability).withStyle(ADDED_MODIFIER_COLOR));
        }
    }

    public static List<Component> getTooltip(AspectTooltipContext context){
        List<Component> componentList = new ArrayList<>();
        componentList.addAll(context.aspectInstanceList.stream()
                .filter(aspectInstance -> context.isAdvanced ? aspectInstance.aspectTooltipDisplaySetting != AspectTooltipDisplaySetting.NEVER : aspectInstance.aspectTooltipDisplaySetting == AspectTooltipDisplaySetting.ALWAYS)
                .map(aspectInstance ->
                        Component.literal(context.optionalHeader.isPresent() ? " " : "")
                                .append(aspectInstance.obfuscated ? OBFUSCATED_TOOLTIP : aspectInstance.getMutableComponent(context)).withStyle(context.chatFormatting))
                .collect(Collectors.toList()));
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
        fillAttributeMultimap(getAspectStrengthMap(oldItemStack), uuidStart, oldMultimap);
        fillAttributeMultimap(getAspectStrengthMap(newItemStack), uuidStart, newMultimap);
        if(equipmentSlot.getType() == EquipmentSlot.Type.ARMOR){
            List<ItemStack> oldArmorPieces = ARMOR_EQUIPMENT_SLOT_LIST.stream().map(equipmentSlot1 -> livingEntity.getLastArmorItem(equipmentSlot1)).collect(Collectors.toList());
            List<ItemStack> newArmorPieces = ARMOR_EQUIPMENT_SLOT_LIST.stream().map(equipmentSlot1 -> livingEntity.getItemBySlot(equipmentSlot1)).collect(Collectors.toList());
            if(isFullArmorSet(oldArmorPieces) && oldArmorPieces.get(0).getItem() instanceof AspectArmorItem aspectArmorItem){
                aspectArmorItem.getSetBonusAbilityHolder().map.forEach((key, value) -> {
                    if (key instanceof AttributeAspect attributeAspect) {
                        oldMultimap.put(attributeAspect.getAttribute(), new AttributeModifier(new UUID (SET_BONUS_STRING.hashCode(), attributeAspect.getAttribute().getDescriptionId().hashCode()), attributeAspect.id, value, attributeAspect.operation));
                    }
                });
            }
            livingEntity.setItemSlot(equipmentSlot, newItemStack);
            if(isFullArmorSet(newArmorPieces) && newArmorPieces.get(0).getItem() instanceof AspectArmorItem aspectArmorItem){
                aspectArmorItem.getSetBonusAbilityHolder().map.forEach((key, value) -> {
                    if (key instanceof AttributeAspect attributeAspect) {
                        newMultimap.put(attributeAspect.getAttribute(), new AttributeModifier(new UUID (SET_BONUS_STRING.hashCode(), attributeAspect.getAttribute().getDescriptionId().hashCode()), attributeAspect.id, value, attributeAspect.operation));
                    }
                });
            }
        }
    }

    public static void fillAttributeMultimap(AspectStrengthMap aspectStrengthMap, String uuidStart, Multimap<Attribute, AttributeModifier> multimap){
        aspectStrengthMap.forEach((key, value) -> {
            if (key instanceof AttributeAspect attributeAspect) {
                multimap.put(attributeAspect.getAttribute(), new AttributeModifier(new UUID (uuidStart.hashCode(), attributeAspect.getAttribute().getDescriptionId().hashCode()), attributeAspect.id, value, attributeAspect.operation));
            }
        });
    }

    // Add/Remove added modifiers

    public static float getUsedModifiability(ItemStack itemStack){
        AspectStrengthMap addedModifierMap = getAddedModifierMap(itemStack);
        FunctionQuery functionQuery = new FunctionQuery(aspect -> aspect.weight);
        return functionQuery.queryStrengthMap(addedModifierMap);
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

    public static boolean canAddModifier(ItemStack itemStack, AspectInstance aspectInstance){
        boolean passesItemPredicate = aspectInstance.aspect.itemPredicate.test(itemStack.getItem());
        boolean passesModifiabilityCheck = aspectInstance.getModifiability() <= getModifiabilityRemaining(itemStack);
        return passesItemPredicate && passesModifiabilityCheck;
    }

    public static void addModifier(ItemStack itemStack, AspectInstance aspectInstance){
        float strength = AspectUtil.getAddedModifierMap(itemStack).get(aspectInstance.aspect);
        AspectInstance newAspectInstance = aspectInstance.withAddedStrength(strength);
        replaceModifier(itemStack, newAspectInstance);
    }

    // Replaces added modifier with same aspect as aspectInstance with aspectInstance,
    // or removes the modifier altogether if aspectInstance.strength is 0
    public static void replaceModifier(ItemStack itemStack, AspectInstance aspectInstance){
        removeAddedModifier(itemStack, aspectInstance.aspect);
        // Just removes old modifier if the strength is set to 0
        if(aspectInstance.strength > 0.0f){
            ListTag aspectListTag = getAddedModifierListTag(itemStack);
            aspectListTag.add(aspectInstance.toCompoundTag());
            itemStack.getOrCreateTag().put(ADDED_MODIFIERS_TAG, aspectListTag);
        }
    }

    public static void removeAddedModifier(ItemStack itemStack, Aspect aspect){
        ListTag aspectListTag = getAddedModifierListTag(itemStack);
        Tag oldAspectTag = null;
        for(Tag tag: aspectListTag){
            if(tag instanceof CompoundTag compoundTag && compoundTag.getString(AspectInstance.ID_TAG).equals(aspect.id)){
                oldAspectTag = compoundTag;
                break;
            }
        }
        if(oldAspectTag != null){
            aspectListTag.remove(oldAspectTag);
            itemStack.getOrCreateTag().put(ADDED_MODIFIERS_TAG, aspectListTag);
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
