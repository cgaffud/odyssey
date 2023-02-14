package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.tier.OdysseyTier;
import com.bedmen.odyssey.tier.OdysseyTiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.util.Lazy;

import java.util.List;

public enum ArrowType{
    FLINT(ItemRegistry.ARROW::get, OdysseyTiers.FLINT, 5.0d, new ResourceLocation("textures/entity/projectiles/arrow.png")),
    SPIDER_FANG("spider_fang", ItemRegistry.SPIDER_FANG_ARROW::get, OdysseyTiers.SPIDER_FANG, 5d, List.of(new AspectInstance(Aspects.PROJECTILE_POISON_DAMAGE, 2))),
    WEAVER_FANG("weaver_fang", ItemRegistry.WEAVER_FANG_ARROW::get, OdysseyTiers.WEAVER_FANG, 5.5, List.of(new AspectInstance(Aspects.PROJECTILE_COBWEB_CHANCE, 0.2f))),
    AMETHYST("amethyst", ItemRegistry.AMETHYST_ARROW::get, OdysseyTiers.AMETHYST, 6.0d, List.of()),
    CLOVER_STONE("clover_stone", ItemRegistry.CLOVER_STONE_ARROW::get, OdysseyTiers.CLOVER_STONE, 6.0d, List.of(new AspectInstance(Aspects.PROJECTILE_LOOTING_LUCK, 1))),
    HEXED_EARTH("hexed_earth", ItemRegistry.HEXED_EARTH_ARROW::get, OdysseyTiers.HEXED_EARTH, 2.0d, List.of(new AspectInstance(Aspects.PROJECTILE_HEXED_EARTH, 0.2f)));

    private final Lazy<Item> lazyItem;
    public final Tier tier;
    public final double damage;
    public final InnateAspectHolder innateAspectHolder;
    public final ResourceLocation resourceLocation;

    ArrowType(Lazy<Item> lazyItem, OdysseyTier odysseyTier, double damage, ResourceLocation resourceLocation){
        this.lazyItem = lazyItem;
        this.tier = odysseyTier;
        this.damage = damage;
        this.innateAspectHolder = new InnateAspectHolder(List.of(), List.of());
        this.resourceLocation = resourceLocation;
    }

    ArrowType(String id, Lazy<Item> lazyItem, OdysseyTier odysseyTier, double damage, List<AspectInstance> innateAspectList){
        this.lazyItem = lazyItem;
        this.tier = odysseyTier;
        this.damage = damage;
        this.innateAspectHolder = new InnateAspectHolder(List.of(), innateAspectList);
        this.resourceLocation = new ResourceLocation(Odyssey.MOD_ID, String.format("textures/entity/projectiles/%s_arrow.png", id));
    }

    public Item getItem(){
        return this.lazyItem.get();
    }
}