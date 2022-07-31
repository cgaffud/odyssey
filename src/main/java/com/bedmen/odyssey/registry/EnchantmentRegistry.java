package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import com.bedmen.odyssey.enchantment.odyssey.*;
import com.bedmen.odyssey.enchantment.upgrades.*;
import com.bedmen.odyssey.enchantment.vanilla_copies.*;
import com.bedmen.odyssey.util.BiomeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.ArrowDamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentRegistry {

    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS , Odyssey.MOD_ID);
    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public static void init() {
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // ------ Vanilla Copies ------

    //Protections
    public static final RegistryObject<Enchantment> ALL_DAMAGE_PROTECTION = ENCHANTMENTS.register("protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.COMMON, OdysseyProtectionEnchantment.Type.ALL, EnchantmentCategory.ARMOR, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> FIRE_PROTECTION = ENCHANTMENTS.register("fire_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyProtectionEnchantment.Type.FIRE, EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));
    public static final RegistryObject<Enchantment> FALL_PROTECTION = ENCHANTMENTS.register("feather_falling", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyProtectionEnchantment.Type.FALL, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    public static final RegistryObject<Enchantment> BLAST_PROTECTION = ENCHANTMENTS.register("blast_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.RARE, OdysseyProtectionEnchantment.Type.EXPLOSION, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));

    //Melee Damage
    public static final RegistryObject<Enchantment> SHARPNESS = ENCHANTMENTS.register("sharpness", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.COMMON, OdysseyDamageEnchantment.DamageType.ALL, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SMITE = ENCHANTMENTS.register("smite", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyDamageEnchantment.DamageType.UNDEAD, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> BANE_OF_ARTHROPODS = ENCHANTMENTS.register("bane_of_arthropods", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyDamageEnchantment.DamageType.ARTHROPOD, EquipmentSlot.MAINHAND));

    //Water or Trident
    public static final RegistryObject<Enchantment> RESPIRATION = ENCHANTMENTS.register("respiration", () -> new OdysseyRespirationEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> AQUA_AFFINITY = ENCHANTMENTS.register("aqua_affinity", () -> new OdysseyAquaAffinityEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> DEPTH_STRIDER = ENCHANTMENTS.register("depth_strider", () -> new OdysseyDepthStriderEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> FROST_WALKER = ENCHANTMENTS.register("frost_walker", () -> new OdysseyFrostWalkerEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.FEET));
    public static final RegistryObject<Enchantment> RIPTIDE = ENCHANTMENTS.register("riptide", () -> new OdysseyRiptideEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> CHANNELING = ENCHANTMENTS.register("channeling", () -> new OdysseyChannelingOrLoyaltyEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> LOYALTY = ENCHANTMENTS.register("loyalty", () -> new OdysseyChannelingOrLoyaltyEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));

    //Bow
    public static final RegistryObject<Enchantment> POWER_ARROWS = ENCHANTMENTS.register("power", () -> new ArrowDamageEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> PUNCH_ARROWS = ENCHANTMENTS.register("punch", () -> new OdysseyBowEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FLAMING_ARROWS = ENCHANTMENTS.register("flame", () -> new OdysseyBowEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> MULTISHOT = ENCHANTMENTS.register("multishot", () -> new OdysseyBowEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> QUICK_CHARGE = ENCHANTMENTS.register("quick_charge", () -> new OdysseyBowEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> PIERCING = ENCHANTMENTS.register("piercing", () -> new OdysseyBowEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));

    //Other
    public static final RegistryObject<Enchantment> THORNS = ENCHANTMENTS.register("thorns", () -> new OdysseyThornsEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));

    // ------ Odyssey Enchantments ------
    
    // Conditional Enchantments
    public static final RegistryObject<Enchantment> SUN_BLESSING = ENCHANTMENTS.register("sun_blessing",() -> new ConditionalAmpEnchantment(Enchantment.Rarity.RARE, 1, 2.0f, EnchantmentRegistry::getSunBoost, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> MOON_BLESSING = ENCHANTMENTS.register("moon_blessing",() -> new ConditionalAmpEnchantment(Enchantment.Rarity.RARE, 1, 2.0f, EnchantmentRegistry::getMoonBoost, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SKY_BLESSING = ENCHANTMENTS.register("sky_blessing",()-> new ConditionalAmpEnchantment(Enchantment.Rarity.RARE, 1, 2.0f, EnchantmentRegistry::getSkyBoost, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> BOTANICAL = ENCHANTMENTS.register("botanical", ()-> new ConditionalAmpEnchantment(Enchantment.Rarity.RARE, 1, 2.0f, EnchantmentRegistry::getHotHumidBoost, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> XEROPHILIC = ENCHANTMENTS.register("xerophilic", ()-> new ConditionalAmpEnchantment(Enchantment.Rarity.RARE, 1, 2.0f, EnchantmentRegistry::getHotDryBoost, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> CRYOPHILIC = ENCHANTMENTS.register("cryophilic", ()-> new ConditionalAmpEnchantment(Enchantment.Rarity.RARE, 1, 2.0f, EnchantmentRegistry::getColdBoost, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> VOID_AMPLIFICATION = ENCHANTMENTS.register("void_amplification", () -> new ConditionalAmpEnchantment(Enchantment.Rarity.RARE, 1, 4.0f, EnchantmentRegistry::getBoostFromVoid, EquipmentSlot.MAINHAND));
    
    //Misc Enchantments
    public static final RegistryObject<Enchantment> SHATTERING = ENCHANTMENTS.register("shattering", () -> new ShatteringEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SUPER_CHARGE = ENCHANTMENTS.register("super_charge", () -> new SuperChargeEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> ICE_PROTECTION = ENCHANTMENTS.register("ice_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyProtectionEnchantment.Type.ICE, EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));
    public static final RegistryObject<Enchantment> DOWNPOUR = ENCHANTMENTS.register("downpour", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.RARE, OdysseyDamageEnchantment.DamageType.HYDROPHOBIC, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FLING = ENCHANTMENTS.register("fling", () -> new FlingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> KINETIC_PROTECTION = ENCHANTMENTS.register("kinetic_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyProtectionEnchantment.Type.KINETIC, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));

    //Volcanic Enchantments
    public static final RegistryObject<Enchantment> VULCAN_STRIDER = ENCHANTMENTS.register("vulcan_strider", () -> new VulcanStriderEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> OBSIDIAN_WALKER = ENCHANTMENTS.register("obsidian_walker", () -> new ObsidianWalkerEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.FEET));
    public static final RegistryObject<Enchantment> PYROPNEUMATIC = ENCHANTMENTS.register("pyropneumatic", () -> new PyropneumaticEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> MOLTEN_AFFINITY = ENCHANTMENTS.register("molten_affinity", () -> new MoltenAffinityEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> ERUPTION = ENCHANTMENTS.register("eruption", () -> new EruptionEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));

    //Set Bonuses
    public static final RegistryObject<Enchantment> SLOW_FALLING = ENCHANTMENTS.register("slow_falling", () -> new SetBonusEnchantment(Enchantment.Rarity.VERY_RARE, 1, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> TURTLING = ENCHANTMENTS.register("turtling", () -> new SetBonusEnchantment(Enchantment.Rarity.VERY_RARE, 1, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> GLIDING = ENCHANTMENTS.register("gliding", () -> new SetBonusEnchantment(Enchantment.Rarity.VERY_RARE, 2, ARMOR_SLOTS));

    //Curses
    public static final RegistryObject<Enchantment> UNENCHANTABLE = ENCHANTMENTS.register("unenchantable", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 1, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> BLEEDING = ENCHANTMENTS.register("bleeding", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 3, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> HEAVY = ENCHANTMENTS.register("heavy", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 3, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> DROWNING = ENCHANTMENTS.register("drowning", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 1, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> VOLATILE = ENCHANTMENTS.register("volatile", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, OdysseyEnchantmentCategory.ALL_MELEE, 1, EquipmentSlot.values()));

    //Damage Boost Functions
    private static float getSunBoost(BlockPos pos, Level level) {
        return getSkyBoost(pos, level) * (((level.getDayTime() % 24000L) < 12000L) ? 1.0f : 0.0f);
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

    private static float getHotDryBoost(BlockPos pos, Level level) {
        return Mth.sqrt(getHotBoost(pos, level) * getDryBoost(pos, level));
    }

    private static float getHotBoost(BlockPos pos, Level level) {
        return Mth.clamp(BiomeUtil.getClimate(level.getBiome(pos)).temperature, 0.0f, 1.0f);
    }

    private static float getColdBoost(BlockPos pos, Level level) {
        return 1.0f - getHotBoost(pos, level);
    }

    private static float getHumidBoost(BlockPos pos, Level level) {
        Holder<Biome> biomeHolder = level.getBiome(pos);
        Biome biome = level.getBiome(pos).value();
        return level.isRaining() && biome.getPrecipitation() == Biome.Precipitation.RAIN ? 1.0f : Mth.clamp(BiomeUtil.getClimate(biomeHolder).downfall, 0.0f, 1.0f);
    }

    private static float getDryBoost(BlockPos pos, Level level) {
        return 1f - getHumidBoost(pos, level);
    }

    private static float quadraticMagicFunction(float y, float intercept, boolean incr){
        float num  = incr ? (-0.5f * intercept - y) : (1.5f * intercept - y);
        return ((num * num)/(8.0f * intercept * intercept) - 1 / 32.0f);
    }

    private static float getBoostFromVoid(BlockPos pos, Level level) {
        int y = pos.getY();
        if (level.dimension() == Level.OVERWORLD) {
            if (y >= 64) return 0.0f;
            else return 2.0f* quadraticMagicFunction(y+64, 128.0f, false);
        }
        if (level.dimension() == Level.NETHER) {
            if ((y >= 32) && (y <= 96)) return 0.0f;
            else {
                float yAdj = (float) Math.min(Math.abs(32-y), Math.abs(y-96));
                return (yAdj * yAdj)/(32.0f*32.0f) * 0.5f;
            }
        }
        return 1.0f;
    }

}