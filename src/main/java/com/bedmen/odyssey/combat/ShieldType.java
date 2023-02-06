package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.tools.OdysseyTiers;
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
    WOODEN("wooden", OdysseyTiers.WOOD, 4.0f, 100, item -> item.builtInRegistryHolder().is(ItemTags.PLANKS), List.of(), List.of()),
    COPPER("copper", OdysseyTiers.COPPER, 5.0f, 100, item -> item == Items.COPPER_INGOT, List.of(), List.of()),
    RUSTY("rusty", OdysseyTiers.RUSTY_IRON, 6.0f, 100, item -> item == Items.IRON_INGOT, List.of(), List.of(new AspectInstance(Aspects.IMPENETRABILITY, 1.0f))),
    GOLDEN("golden", OdysseyTiers.GOLD, 6.0f, 80, item -> item == Items.GOLD_INGOT, List.of(), List.of(new AspectInstance(Aspects.RECOVERY_SPEED, 1.0f))),
    REINFORCED("reinforced", OdysseyTiers.IRON, 7.0f, 100, item -> item == Items.IRON_INGOT, List.of(), List.of(new AspectInstance(Aspects.EXPLOSION_DAMAGE_BLOCK, 7.0f))),
    DIAMOND("diamond", OdysseyTiers.DIAMOND, 8.0f, 100, item -> item == Items.DIAMOND, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f)));

    public final int durability;
    public final float damageBlock;
    public final int recoveryTime;
    public final Predicate<Item> repairItemPredicate;
    public final Material material;
    public final Material material_nopattern;
    public final InnateAspectHolder innateAspectHolder;

    ShieldType(String id, Tier tier, float damageBlock, int recoveryTime, Predicate<Item> repairItemPredicate, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList){
        this.durability = tier.getUses() * 2;
        this.damageBlock = damageBlock;
        this.recoveryTime = recoveryTime;
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
