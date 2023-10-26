package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import com.bedmen.odyssey.tags.OdysseyEntityTags;
import com.bedmen.odyssey.util.GeneralUtil;
import com.bedmen.odyssey.world.BiomeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

import java.util.HashMap;
import java.util.Map;

public class Aspects {
    public static final Map<String, Aspect<?>> ASPECT_REGISTER = new HashMap<>();

    // # All Damageables
    public static final FloatAspect DURABILITY = new FloatAspect("durability", 1.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.DAMAGEABLE, false);
    public static final UnitAspect BURN_PROOF = new UnitAspect("burn_proof", AspectItemPredicates.DAMAGEABLE, false);
    public static final IntegerAspect SOULBOUND = new IntegerAspect("soulbound", 1.0f, AspectTooltipFunctions.ROMAN_NUMERAL, AspectItemPredicates.DAMAGEABLE, false, true);

    // ## Attribute
    public static final AttributeAspect MOVEMENT_SPEED = new AttributeAspect("movement_speed", 10.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.LOWER_ARMOR, () -> Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE);
    public static final AttributeAspect ATTACK_DAMAGE = new AttributeAspect("attack_damage", 2.0f, AspectTooltipFunctions.FLOAT_ADDITION, AspectItemPredicates.DAMAGEABLE, () -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION);
    public static final AttributeAspect SWIM_SPEED = new AttributeAspect("swim_speed", 8.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.NECK_DOWN_ARMOR, ForgeMod.SWIM_SPEED::get, AttributeModifier.Operation.ADDITION);

    // # All Weapons
    public static final IntegerAspect LOOTING_LUCK = new IntegerAspect("looting_luck", 1.0f, AspectTooltipFunctions.INTEGER_ADDITION, AspectItemPredicates.MELEE, true, true);
    public static final ConditionalAmpAspect SOLAR_STRENGTH = new ConditionalAmpAspect("solar_strength", Aspects::getSunBoost);
    public static final ConditionalAmpAspect LUNAR_STRENGTH = new ConditionalAmpAspect("lunar_strength", Aspects::getMoonBoost);

    public static final ConditionalAmpAspect SKY_STRENGTH = new ConditionalAmpAspect("sky_strength", Aspects::getSkyBoost);
    public static final ConditionalAmpAspect BOTANICAL_STRENGTH = new ConditionalAmpAspect("botanical_strength", Aspects::getHotHumidBoost);
    public static final ConditionalAmpAspect SCORCHED_STRENGTH = new ConditionalAmpAspect("scorched_strength", Aspects::getHotDryBoost);
    public static final ConditionalAmpAspect WINTERY_STRENGTH = new ConditionalAmpAspect("wintery_strength", Aspects::getColdBoost);
    public static final ConditionalAmpAspect VOID_STRENGTH = new ConditionalAmpAspect("void_strength", Aspects::getBoostFromVoid);
    public static final ConditionalAmpAspect ABSORBENT_GROWTH = new ConditionalAmpAspect("absorbent_growth", Aspects::getBoostFromDamage);

    // # Melee
    public static final TargetConditionalMeleeAspect DAMAGE_ON_ARTHROPOD = new TargetConditionalMeleeAspect("damage_on_arthropod", livingEntity -> livingEntity.getMobType() == MobType.ARTHROPOD);
    public static final TargetConditionalMeleeAspect SMITE_DAMAGE = new TargetConditionalMeleeAspect("smite_damage", livingEntity -> livingEntity.getMobType() == MobType.UNDEAD);
    public static final TargetConditionalMeleeAspect HYDRO_DAMAGE = new TargetConditionalMeleeAspect("hydro_damage", livingEntity -> livingEntity.getType().is(OdysseyEntityTags.HYDROPHOBIC));
  
    public static final FloatAspect KNOCKBACK = new FloatAspect("knockback", 2.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.MELEE, true);
    public static final FloatAspect FATAL_HIT = new FloatAspect("fatal_hit", 0.2f, AspectTooltipFunctions.HP_THRESHHOLD, AspectItemPredicates.MELEE, true);
    public static final FloatAspect ADDITIONAL_SWEEP_DAMAGE = new FloatAspect("additional_sweep_damage", 1.0f, AspectTooltipFunctions.FLOAT_ADDITION, AspectItemPredicates.HAS_SWEEP, true);
    public static final IntegerAspect POISON_DAMAGE = new IntegerAspect("poison_damage", 1.0f, AspectTooltipFunctions.INTEGER_ADDITION, AspectItemPredicates.MELEE, true, true);
    public static final IntegerAspect HEXFLAME_DAMAGE = new IntegerAspect("hexflame_damage", 1.0f, AspectTooltipFunctions.INTEGER_ADDITION, AspectItemPredicates.MELEE, true, true);
    public static final FloatAspect COBWEB_CHANCE = new FloatAspect("cobweb_chance", 10.0f, AspectTooltipFunctions.PERCENTAGE_DELCARATION, AspectItemPredicates.MELEE, true);
    public static final FloatAspect LARCENY_CHANCE = new FloatAspect("larceny_chance", 20.0f, AspectTooltipFunctions.PERCENTAGE_DELCARATION, AspectItemPredicates.MELEE, true);
    public static final UnitAspect COLD_SNAP = new UnitAspect("cold_snap", 2.0f, AspectItemPredicates.MELEE, true);
    public static final FloatAspect BLUDGEONING = new FloatAspect("bludgeoning", 0.5f, AspectTooltipFunctions.FLOAT_ADDITION, AspectItemPredicates.TWO_HANDED, true);
    public static final FloatAspect PRECISION_STRIKE = new FloatAspect("precision_strike", 2.0f, AspectTooltipFunctions.FLOAT_ADDITION, AspectItemPredicates.MELEE, false);
    public static final UnitAspect VAMPIRIC_SPEED = new UnitAspect("vampiric_speed", 10.0f, AspectTooltipFunctions.NAME, AspectItemPredicates.MELEE, true);
    // # Ranged

    // ## Shooting
    public static final FloatAspect ACCURACY = new FloatAspect("accuracy", 0.5f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.THROWABLE_AND_RANGED_AMMO_WEAPON, true);
    public static final MultishotAspect MULTISHOT = new MultishotAspect();

    // ## Throwing
    public static final FloatAspect LOYALTY = new FloatAspect("loyalty", 2.0f, AspectTooltipFunctions.FLOAT_ADDITION, AspectItemPredicates.THROWABLE, false);
    public static final FloatAspect VELOCITY = new FloatAspect("velocity", 2.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.THROWABLE, true);

    // ## Projectile
    public static final FloatAspect PIERCING = new FloatAspect("piercing", 2.0f, AspectTooltipFunctions.FLOAT_ADDITION, AspectItemPredicates.PROJECTILE, false);
    public static final IntegerAspect PROJECTILE_LOOTING_LUCK = new IntegerAspect("projectile_looting_luck", 1.0f, AspectTooltipFunctions.INTEGER_ADDITION, AspectItemPredicates.PROJECTILE, false, false);
    public static final IntegerAspect PROJECTILE_POISON_DAMAGE = new IntegerAspect("projectile_poison_damage", 1.0f, AspectTooltipFunctions.INTEGER_ADDITION, AspectItemPredicates.PROJECTILE, false, true);
    public static final FloatAspect PROJECTILE_HEXED_EARTH = new FloatAspect("projectile_hexed_earth", 20.0f, AspectTooltipFunctions.PERCENTAGE_DELCARATION, AspectItemPredicates.PROJECTILE, false);
    public static final FloatAspect PROJECTILE_COBWEB_CHANCE = new FloatAspect("projectile_cobweb_chance", 10.0f, AspectTooltipFunctions.PERCENTAGE_DELCARATION, AspectItemPredicates.PROJECTILE, false);
    public static final FloatAspect PROJECTILE_LARCENY_CHANCE = new FloatAspect("projectile_larceny_chance", 20.0f, AspectTooltipFunctions.PERCENTAGE_DELCARATION, AspectItemPredicates.PROJECTILE, false);
    public static final FloatAspect PROJECTILE_KNOCKBACK = new FloatAspect("projectile_knockback", 2.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.PROJECTILE, false);
    public static final UnitAspect HYDRODYNAMIC = new UnitAspect("hydrodynamic", AspectItemPredicates.PROJECTILE, false);
    public static final UnitAspect SNOW_STORM = new UnitAspect("snow_storm", 2.0f, AspectItemPredicates.PROJECTILE, false);

    // # Armor
    public static final DamageSourcePredicateAspect FEATHER_FALLING = new DamageSourcePredicateAspect("feather_falling", 1.0f, AspectItemPredicates.LOWER_ARMOR, true, damageSource -> damageSource == DamageSource.FALL || damageSource == DamageSource.FLY_INTO_WALL);
    public static final DamageSourcePredicateAspect ICE_PROTECTION = new DamageSourcePredicateAspect("ice_protection", 2.0f, AspectItemPredicates.ARMOR, true, damageSource -> damageSource == DamageSource.FREEZE);
    public static final DamageSourcePredicateAspect FIRE_PROTECTION = new DamageSourcePredicateAspect("fire_protection", 2.0f, AspectItemPredicates.ARMOR, true, DamageSource::isFire);
    public static final DamageSourcePredicateAspect BLAST_PROTECTION = new DamageSourcePredicateAspect("blast_protection", 2.0f, AspectItemPredicates.ARMOR, true, DamageSource::isExplosion);
    public static final DamageSourcePredicateAspect PROTECTION = new DamageSourcePredicateAspect("protection", 4.0f, AspectItemPredicates.ARMOR, true, damageSource -> !damageSource.isBypassEnchantments());
    public static final FloatAspect RESPIRATION = new FloatAspect("respiration", 2.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.UPPER_ARMOR, true);
    public static final UnitAspect SNOWSHOE = new UnitAspect("snowshoe", AspectItemPredicates.BOOTS, true);
    public static final FloatAspect THORNS = new FloatAspect("thorns", 2.0f, AspectTooltipFunctions.FLOAT_ADDITION, AspectItemPredicates.ARMOR, true);

    // # Shields
    public static final ShieldDamageBlockAspect EXPLOSION_DAMAGE_BLOCK = new ShieldDamageBlockAspect("explosion_damage_block", DamageSource::isExplosion);

    public static final FloatAspect RECOVERY_SPEED = new FloatAspect("recovery_speed", 2.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.PARRYABLE, false);

    public static final FloatAspect WIDTH = new FloatAspect("width", 4.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.SHIELD, false);
    public static final UnitAspect COLD_TO_THE_TOUCH = new UnitAspect("cold_to_the_touch", 2.0f, AspectItemPredicates.SHIELD, false);
    public static final FloatAspect BLOWBACK = new FloatAspect("blowback", 2.0f, AspectTooltipFunctions.FLOAT_ADDITION, AspectItemPredicates.SHIELD, false);
    public static final IntegerAspect ASSISTED_STRIKE = new IntegerAspect("assisted_strike", 2.0f, AspectTooltipFunctions.INTEGER_ADDITION, AspectItemPredicates.SHIELD, true, true);
    public static final FloatAspect PRECISE_BLOCK = new FloatAspect("precise_block", 2.0f, AspectTooltipFunctions.FLOAT_ADDITION, AspectItemPredicates.SHIELD, false);


    // # Tools
    public static final FloatAspect EFFICIENCY = new FloatAspect("efficiency", 4.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.TOOL, true);
    public static final IntegerAspect FORTUNE = new IntegerAspect("fortune", 1.0f, AspectTooltipFunctions.INTEGER_ADDITION, AspectItemPredicates.TOOL, false, false);
    public static final UnitAspect AQUA_AFFINITY = new UnitAspect("aqua_affinity", AspectItemPredicates.TOOL, true);

    // # Abilities

    // ## Melee Abilities
    public static final UnitAspect SHIELD_BASH = new UnitAspect("shield_bash", AspectItemPredicates.MELEE, false);
    public static final UnitAspect SWEEP = new UnitAspect("sweep", AspectItemPredicates.MELEE, false);
    public static final UnitAspect DUAL_WIELD = new UnitAspect("dual_wield", AspectItemPredicates.MELEE, false);
    public static final UnitAspect COBWEB_BREAK = new UnitAspect("cobweb_break", AspectItemPredicates.MELEE, false);
    public static final UnitAspect SMACK = new UnitAspect("smack", AspectItemPredicates.MELEE, false);
    public static final UnitAspect THRUST = new UnitAspect("thrust", AspectItemPredicates.MELEE, false);

    // ## Bow Abilities
    public static final UnitAspect SPYGLASS = new UnitAspect("spyglass", AspectItemPredicates.BOW, false);
    public static final UnitAspect REPEAT = new UnitAspect("repeat", AspectItemPredicates.RANGED_AMMO_WEAPON, false);

    // ## Armor Abilities
    public static final UnitAspect PIGLIN_NEUTRAL = new UnitAspect("piglin_neutral", AspectItemPredicates.ARMOR, false);

    // ## Set Bonus Abilities
    public static final UnitAspect SLOW_FALL = new UnitActivationAspect("slow_fall", "key.sneak");
    public static final GlideAspect GLIDE = new GlideAspect();
    public static final UnitAspect FROST_WALKER = new UnitAspect("frost_walker", AspectItemPredicates.BOOTS, true);
    public static final UnitAspect TURTLE_MASTERY = new UnitActivationAspect("turtle_mastery", "key.sneak");

    // # Curses
    public static final UnitAspect BINDING = new UnitAspect("binding", 1.0f, AspectItemPredicates.ARMOR, false);
    public static final UnitAspect VANISHING = new UnitAspect("vanishing", 1.0f, AspectItemPredicates.DAMAGEABLE, false);
    public static final IntegerAspect BLOOD_LOSS = new IntegerAspect("blood_loss", 1.0f, AspectTooltipFunctions.INTEGER_ADDITION, AspectItemPredicates.DAMAGEABLE, true, false);
    public static final IntegerAspect WEIGHT = new IntegerAspect("weight", 1.0f, AspectTooltipFunctions.INTEGER_ADDITION, AspectItemPredicates.DAMAGEABLE, true, false);
    public static final IntegerAspect OXYGEN_DEPRIVATION = new IntegerAspect("oxygen_deprivation", 1.0f, AspectTooltipFunctions.INTEGER_ADDITION, AspectItemPredicates.DAMAGEABLE, true, false);
    public static final UnitAspect BLANK = new UnitAspect("blank", 1.0f, AspectItemPredicates.DAMAGEABLE, false);
    public static final FloatAspect VOLATILITY = new FloatAspect("volatility", 1.0f, AspectTooltipFunctions.FLOAT_ADDITION, AspectItemPredicates.DAMAGEABLE, false);

    // # PermaBuffs

    // ## Eaten Markers
    public static final UnitAspect HAS_EATEN_ROCK_CANDY = new UnitAspect("has_eaten_rock_candy");

    // ## Buffs
    public static final IntegerAspect ADDITIONAL_MOB_HARVEST_LEVEL = new IntegerAspect("additional_mob_harvest_level", AspectTooltipFunctions.INTEGER_ADDITION);
    public static final FloatAspect APPETITE = new FloatAspect("appetite", AspectTooltipFunctions.PERCENTAGE_ADDITION);

    // # Other
    public static final IntegerAspect TELEPORTATION_IMMUNITY = new IntegerAspect("teleportation_immunity", 0.0f, AspectTooltipFunctions.INTEGER_ADDITION, AspectItemPredicates.NONE, true, false);
    public static final FloatAspect EXPERIENCE_PER_SECOND = new FloatAspect("experience_per_second", 0.0f, AspectTooltipFunctions.FLOAT_PER_SECOND, AspectItemPredicates.NONE, true);
    public static final FloatAspect TEMPERATURE_PER_SECOND = new FloatAspect("temperature_per_second", 0.0f, AspectTooltipFunctions.FLOAT_PER_SECOND, AspectItemPredicates.NONE, true);
    public static final FloatAspect WARMTH = new FloatAspect("warmth", 1.0f, AspectTooltipFunctions.FLOAT_ADDITION, AspectItemPredicates.NONE, true);
    public static final FloatAspect COOLING = new FloatAspect("cooling", 1.0f, AspectTooltipFunctions.FLOAT_ADDITION, AspectItemPredicates.NONE, true);

    public static float getTrueSunBoost(BlockPos pos, Level level) {
        long time = level.getDayTime() % 24000L;
        return BiomeUtil.getSkyMultiplier(pos, level) * (time < 12000L ? 1.0f : 0.0f);
    }

    public static float getMoonBoost(ItemStack itemStack, BlockPos pos, Level level) {
        long time = level.getDayTime() % 24000L;
        if (BiomeUtil.getSkyMultiplier(pos, level) == 1.0f && (time >= 12000L)) {
            int charge = itemStack.getOrCreateTag().getInt(AspectUtil.STORED_BOOST_TAG);
            if (charge < 50 && level instanceof ServerLevel)  itemStack.getOrCreateTag().putInt(AspectUtil.STORED_BOOST_TAG, charge+1);
            return 1.0f;
        } return itemStack.getOrCreateTag().getInt(AspectUtil.STORED_BOOST_TAG) > 10 ? 1.0f : 0.0f;
    }

    private static float getSkyBoost(ItemStack itemStack, BlockPos pos, Level level) {
        return BiomeUtil.getSkyMultiplier(pos, level);
    }

    public static float getHotHumidBoost(ItemStack itemStack, BlockPos pos, Level level) {
        float rawBoost = Mth.sqrt(BiomeUtil.getHotMultiplier(pos, level) * BiomeUtil.getHumidMultiplier(pos, level));
        if (GeneralUtil.isHashTick(itemStack, level, 80)) {
            if (rawBoost < 0.5f) itemStack.hurt(1, level.getRandom(), null);
            else if ((rawBoost > 0.75f) && itemStack.getDamageValue() != 0) itemStack.setDamageValue(itemStack.getDamageValue()-1);
        }
        return rawBoost;
    }

    public static float getHotDryBoost(ItemStack itemStack, BlockPos pos, Level level) {
        return Mth.sqrt(BiomeUtil.getHotMultiplier(pos, level) * BiomeUtil.getDryMultiplier(pos, level));
    }

    public static float getColdBoost(ItemStack itemStack, BlockPos pos, Level level) {
        return BiomeUtil.getColdMultiplier(pos, level);
    }

    public static float getSunBoost(ItemStack itemStack, BlockPos pos, Level level) {
        float doBoost = getTrueSunBoost(pos, level);
        if (doBoost == 1.0f) {
            int charge = itemStack.getOrCreateTag().getInt(AspectUtil.STORED_BOOST_TAG);
            if (charge < 50 && level instanceof ServerLevel)  itemStack.getOrCreateTag().putInt(AspectUtil.STORED_BOOST_TAG, charge+1);
            return 1.0f;
        } return itemStack.getOrCreateTag().getInt(AspectUtil.STORED_BOOST_TAG) > 10 ? 1.0f : 0.0f;
    }

    /**
     * Gives a 0 if y is at or beyond badY, a 1 if y is at or beyond goodY, quadratic scale in between
     */
    private static float quadraticDistanceFactorToVoid(float y, float badY, float goodY) {
        float f = Mth.clamp((y - badY) / (goodY - badY), 0.0f, 1.0f);
        return f * f;
    }

    private static float getBoostFromVoid(ItemStack itemStack, BlockPos pos, Level level) {
        int y = pos.getY();
        if (level.dimension() == Level.END) {
            // Usually these boost numbers are only from 0 to 1 but this is an exception
            return 2.0f;
        }
        if (level.dimension() == Level.NETHER) {
            if (y > 64) {
                return quadraticDistanceFactorToVoid(y, 96, 128);
            } else {
                return quadraticDistanceFactorToVoid(y, 32, 0);
            }
        }
        // Going to assume any other custom dimensions are similar to the overworld
        return quadraticDistanceFactorToVoid(y, 64, -64);
    }


    private static float getBoostFromDamage(ItemStack itemStack, BlockPos pos, Level level) {
        return itemStack.getOrCreateTag().getFloat(AspectUtil.DAMAGE_GROWTH_TAG);
    }
}
