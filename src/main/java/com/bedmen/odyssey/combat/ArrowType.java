package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.modifier.ModifierInstance;
import com.bedmen.odyssey.modifier.ModifierUtil;
import com.bedmen.odyssey.modifier.Modifiers;
import com.bedmen.odyssey.modifier.InnateModifierHolder;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.util.Lazy;

import java.util.List;

public enum ArrowType{
    FLINT(ItemRegistry.ARROW::get, 5.0d, new ResourceLocation("textures/entity/projectiles/arrow.png")),
    SPIDER_FANG("spider_fang", ItemRegistry.SPIDER_FANG_ARROW::get, 5d, List.of(new ModifierInstance(Modifiers.PROJECTILE_POISON_DAMAGE, 2))),
    WEAVER_FANG("weaver_fang", ItemRegistry.WEAVER_FANG_ARROW::get, 5.5, List.of(new ModifierInstance(Modifiers.PROJECTILE_COBWEB_CHANCE, 0.1f))),
    CLOVER_STONE("clover_stone", ItemRegistry.CLOVER_STONE_ARROW::get, 6d, List.of(new ModifierInstance(Modifiers.PROJECTILE_LOOTING_LUCK, 1))),
    AMETHYST("amethyst", ItemRegistry.AMETHYST_ARROW::get, 6.0d, List.of());

    private final Lazy<Item> lazyItem;
    public final double damage;
    public final InnateModifierHolder innateModifierHolder;
    public final ResourceLocation resourceLocation;

    ArrowType(Lazy<Item> lazyItem, double damage, ResourceLocation resourceLocation){
        this.lazyItem = lazyItem;
        this.damage = damage;
        this.innateModifierHolder = new InnateModifierHolder(List.of());
        this.resourceLocation = resourceLocation;
    }

    ArrowType(String id, Lazy<Item> lazyItem, double damage, List<ModifierInstance> modifierInstanceList){
        this.lazyItem = lazyItem;
        this.damage = damage;
        this.innateModifierHolder = new InnateModifierHolder(modifierInstanceList);
        this.resourceLocation = new ResourceLocation(Odyssey.MOD_ID, String.format("textures/entity/projectiles/%s_arrow.png", id));
    }

    public Item getItem(){
        return this.lazyItem.get();
    }

    public void onEntityHit(EntityHitResult entityHitResult, ItemStack itemStack) {
        Entity entity = entityHitResult.getEntity();
        if(entity instanceof LivingEntity livingEntity){
            // Poison Damage
            float poisonStrength = ModifierUtil.getIntegerModifierValue(itemStack, Modifiers.PROJECTILE_POISON_DAMAGE);
            if(!entity.level.isClientSide && poisonStrength > 0) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 10 + 24, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 10 + (int)(12 * poisonStrength), 1));
            }
            // Cobweb Chance
            float cobwebChance = ModifierUtil.getFloatModifierValue(itemStack, Modifiers.PROJECTILE_COBWEB_CHANCE);
            if(cobwebChance > livingEntity.getRandom().nextFloat()){
                BlockPos blockPos = new BlockPos(livingEntity.getPosition(1f));
                if (livingEntity.level.getBlockState(blockPos).getBlock() == Blocks.AIR) {
                    livingEntity.level.setBlock(blockPos, Blocks.COBWEB.defaultBlockState(), 3);
                }
            }
        }
    }
}