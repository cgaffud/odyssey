package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.tags.OdysseyEntityTags;
import com.bedmen.odyssey.util.BiomeUtil;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class Aspects {
    public static final Aspect DAMAGE_AGAINST_ARTHROPOD = new TargetConditionalMeleeAspect("damage_against_arthropod", livingEntity -> livingEntity.getMobType() == MobType.ARTHROPOD);
    public static final Aspect SMITE_DAMAGE = new TargetConditionalMeleeAspect("smite_damage", livingEntity -> livingEntity.getMobType() == MobType.UNDEAD);
    public static final Aspect HYDRO_DAMAGE = new TargetConditionalMeleeAspect("hydro_damage", livingEntity -> livingEntity.getType().is(OdysseyEntityTags.HYDROPHOBIC));
    public static final Aspect KNOCKBACK = new Aspect("knockback");
    public static final Aspect FATAL_HIT = new Aspect("fatal_hit");
    public static final Aspect SWEEP_DAMAGE = new Aspect("sweep_damage");
    public static final Aspect POISON_DAMAGE = new Aspect("poison_damage");
    public static final Aspect COBWEB_CHANCE = new Aspect("cobweb_chance", aspectInstance -> new TranslatableComponent("aspect.oddc."+aspectInstance.aspect.id, StringUtil.percentFormat(aspectInstance.strength)));
    public static final Aspect SOLAR_STRENGTH = new EnvironmentConditionalMeleeAspect("solar_strength", Aspects::getSunBoost);
    public static final Aspect LUNAR_STRENGTH = new EnvironmentConditionalMeleeAspect("lunar_strength", Aspects::getMoonBoost);
    public static final Aspect BOTANICAL_STRENGTH = new EnvironmentConditionalMeleeAspect("botanical_strength", Aspects::getHotHumidBoost);
    public static final Aspect SCORCHED_STRENGTH = new EnvironmentConditionalMeleeAspect("scorched_strength", Aspects::getHotHumidBoost);
    public static final Aspect WINTERY_STRENGTH = new EnvironmentConditionalMeleeAspect("wintery_strength", Aspects::getHotHumidBoost);
    public static final Aspect LOOTING_LUCK = new Aspect("looting_luck");
    public static final Aspect FORTUNE = new Aspect("fortune");
    public static final Aspect AQUA_AFFINITY = new Aspect("aqua_affinity");

    private static float getSunBoost(BlockPos pos, Level level) {
        long time = level.getDayTime() % 24000L;
        return getSkyBoost(pos, level) * (time < 12000L ? 1.0f : 0.0f);
    }

    private static float getMoonBoost(BlockPos pos, Level level) {
        return 1.0f - getSunBoost(pos, level);
    }

    private static float getSkyBoost(BlockPos pos, Level level) {
        return (level.canSeeSky(pos) && !level.isThundering() && !level.isRaining() && (level.dimension() == Level.OVERWORLD)) ? 1.0f : 0.0f;
    }

    public static float getHotHumidBoost(BlockPos pos, Level level) {
        return Mth.sqrt(getHotBoost(pos, level) * getHumidBoost(pos, level));
    }

    public static float getHotDryBoost(BlockPos pos, Level level) {
        return Mth.sqrt(getHotBoost(pos, level) * getDryBoost(pos, level));
    }

    public static float getHotBoost(BlockPos pos, Level level) {
        return Mth.clamp(BiomeUtil.getClimate(level.getBiome(pos)).temperature, 0.0f, 1.0f);
    }

    public static float getColdBoost(BlockPos pos, Level level) {
        return 1.0f - getHotBoost(pos, level);
    }

    public static float getHumidBoost(BlockPos pos, Level level) {
        Holder<Biome> biomeHolder = level.getBiome(pos);
        Biome biome = level.getBiome(pos).value();
        return level.isRaining() && biome.getPrecipitation() == Biome.Precipitation.RAIN ? 1.0f : Mth.clamp(BiomeUtil.getClimate(biomeHolder).downfall, 0.0f, 1.0f);
    }

    public static float getDryBoost(BlockPos pos, Level level) {
        return 1f - getHumidBoost(pos, level);
    }
}
