package com.bedmen.odyssey.items;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

public class OdysseyCreativeModeTab extends CreativeModeTab {

    private final Lazy<Item> iconLazy;
    public static final CreativeModeTab BUILDING_BLOCKS = new OdysseyCreativeModeTab("oddc_building_blocks", Lazy.of(ItemRegistry.SILVER_ORE));
    public static final CreativeModeTab WOOD = new OdysseyCreativeModeTab("oddc_wood", Lazy.of(ItemRegistry.PALM_LOG));
    public static final CreativeModeTab MATERIALS = new OdysseyCreativeModeTab("oddc_materials", Lazy.of(ItemRegistry.RAW_SILVER));
//    public static final CreativeModeTab TOOLS = new OdysseyCreativeModeTab("oddc_tools", Lazy.of(ItemRegistry.STERLING_SILVER_AXE));
    public static final CreativeModeTab MELEE = new OdysseyCreativeModeTab("oddc_melee", Lazy.of(ItemRegistry.COPPER_BATTLE_AXE));
    public static final CreativeModeTab RANGED = new OdysseyCreativeModeTab("oddc_ranged", Lazy.of(ItemRegistry.BONE_LONG_BOW));
    public static final CreativeModeTab ARMOR = new OdysseyCreativeModeTab("oddc_armor", Lazy.of(ItemRegistry.TURTLE_CHESTPLATE));
//    public static final CreativeModeTab MAGIC = new OdysseyCreativeModeTab("oddc_magic", Lazy.of(ItemRegistry.PURGE_TABLET));
    public static final CreativeModeTab SPAWNING = new OdysseyCreativeModeTab("oddc_spawning", Lazy.of(ItemRegistry.CAMO_CREEPER_SPAWN_EGG));

    public OdysseyCreativeModeTab(String label, Lazy<Item> itemLazy){
        super(label);
        this.iconLazy = itemLazy;
    }

    public ItemStack makeIcon(){
        return this.iconLazy.get().getDefaultInstance();
    }
}