package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.tier.OdysseyTiers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;

import java.util.List;
import java.util.function.Predicate;

public enum ShieldType {
    WOODEN("wooden", OdysseyTiers.WOOD, 4.0f, 100, 90f, item -> item.builtInRegistryHolder().is(ItemTags.PLANKS), List.of(), List.of()),
    COPPER("copper", OdysseyTiers.COPPER, 5.0f, 100, 90f, item -> item == Items.COPPER_INGOT, List.of(), List.of()),
    RUSTY("rusty", OdysseyTiers.RUSTY_IRON, 6.0f, 100, 90f, item -> item == Items.IRON_INGOT, List.of(), List.of(new AspectInstance(Aspects.WIDTH, 0.5f))),
    GOLDEN("golden", OdysseyTiers.GOLD, 6.0f, 80, 90f, item -> item == Items.GOLD_INGOT, List.of(), List.of(new AspectInstance(Aspects.RECOVERY_SPEED, 1.0f))),
    REINFORCED("reinforced", OdysseyTiers.IRON, 7.0f, 100, 90f, item -> item == Items.IRON_INGOT, List.of(), List.of(new AspectInstance(Aspects.EXPLOSION_DAMAGE_BLOCK, 10.0f))),
    DIAMOND("diamond", OdysseyTiers.DIAMOND, 8.0f, 100, 90f, item -> item == Items.DIAMOND, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f))),
    FROST("frost", OdysseyTiers.ARCTIC, 8.0f, 100, 90f, item -> item == ItemRegistry.PERMAFROST_SHARD.get(), List.of(), List.of(new AspectInstance(Aspects.COLD_TO_THE_TOUCH)));

    public final Tier tier;
    public final float damageBlock;
    public final int recoveryTime;
    public final float blockingAngleWidth;
    public final Predicate<Item> repairItemPredicate;
    public final Material material;
    public final Material material_nopattern;
    public final InnateAspectHolder innateAspectHolder;

    ShieldType(String id, Tier tier, float damageBlock, int recoveryTime, float blockingAngleWidth, Predicate<Item> repairItemPredicate, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList){
        this.tier = tier;
        this.damageBlock = damageBlock;
        this.recoveryTime = recoveryTime;
        this.blockingAngleWidth = blockingAngleWidth;
        this.repairItemPredicate = repairItemPredicate;
        this.material = new Material(Sheets.CHEST_SHEET, new ResourceLocation(Odyssey.MOD_ID, "entity/shields/"+id+"_shield_base"));
        this.material_nopattern = new Material(Sheets.CHEST_SHEET, new ResourceLocation(Odyssey.MOD_ID, "entity/shields/"+id+"_shield_base_nopattern"));
        this.innateAspectHolder = new InnateAspectHolder(abilityList, innateModifierList);
    }

    public Material getRenderMaterial(Boolean pattern) {
        if (pattern) return material;
        return material_nopattern;
    }
}
