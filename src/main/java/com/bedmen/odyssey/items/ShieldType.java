package com.bedmen.odyssey.items;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.Lazy;

import java.util.Locale;
import java.util.function.Predicate;

public enum ShieldType {
    WOODEN(200, 4.0f, 100, new TagItemChecker(ItemTags.PLANKS)),
    COPPER(400, 5.0f, 100, new IndividualItemChecker(() -> Items.COPPER_INGOT)),
    RUSTY(600, 6.0f, 100, new IndividualItemChecker(() -> Items.IRON_INGOT)),
    GOLDEN(300, 6.0f, 40, new IndividualItemChecker(() -> Items.GOLD_INGOT)),
    REINFORCED(800, 7.0f, 100, new IndividualItemChecker(() -> Items.IRON_INGOT), 2.0f, DamageSource::isExplosion, "item.oddc.shield.explosion_damage_block"),
    DIAMOND(1600, 8.0f, 100, new IndividualItemChecker(() -> Items.DIAMOND));

    public final int durability;
    public final float damageBlock;
    public final int recoveryTime;
    public final ItemChecker itemChecker;
    public float bonusMultiplier = 1.0f;
    public Predicate<DamageSource> bonusPredicate = damageSource -> false;
    public String bonusMessage = null;
    public final Material material;
    public final Material material_nopattern;

    ShieldType(int durability, float damageBlock, int recoveryTime, ItemChecker itemChecker, float bonusMultiplier, Predicate<DamageSource> bonusPredicate, String bonusMessage){
        this(durability, damageBlock, recoveryTime, itemChecker);
        this.bonusMultiplier = bonusMultiplier;
        this.bonusPredicate = bonusPredicate;
        this.bonusMessage = bonusMessage;
    }

    ShieldType(int durability, float damageBlock, int recoveryTime, ItemChecker itemChecker){
        this.durability = durability;
        this.damageBlock = damageBlock;
        this.recoveryTime = recoveryTime;
        this.itemChecker = itemChecker;
        this.material = new Material(Sheets.CHEST_SHEET, new ResourceLocation(Odyssey.MOD_ID, "entity/shields/"+this.name().toLowerCase(Locale.ROOT)+"_shield_base"));
        this.material_nopattern = new Material(Sheets.CHEST_SHEET, new ResourceLocation(Odyssey.MOD_ID, "entity/shields/"+this.name().toLowerCase(Locale.ROOT)+"_shield_base_nopattern"));
    }

    public Material getRenderMaterial(Boolean pattern) {
        if (pattern) return material;
        return material_nopattern;
    }

    public interface ItemChecker{
        boolean contains(Item item);
    }

    public static class TagItemChecker implements ItemChecker{

        private final TagKey<Item> itemTag;

        public TagItemChecker(TagKey<Item> itemTag){
            this.itemTag = itemTag;
        }

        @Override
        public boolean contains(Item item) {
            return item.getDefaultInstance().is(this.itemTag);
        }
    }

    public static class IndividualItemChecker implements ItemChecker {

        private final Lazy<Item> lazyItem;

        public IndividualItemChecker(Lazy<Item> lazyItem){
            this.lazyItem = lazyItem;
        }

        @Override
        public boolean contains(Item item) {
            return this.lazyItem.get() == item;
        }
    }
}
