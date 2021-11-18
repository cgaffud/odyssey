package com.bedmen.odyssey.items;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

public class OdysseyCreativeModeTab extends CreativeModeTab {

    private final Lazy<Item> iconLazy;
    public static final CreativeModeTab BUILDING_BLOCKS = new OdysseyCreativeModeTab("oddc_building_blocks", Lazy.of(ItemRegistry.SILVER_ORE));
//    public static final CreativeModeTab DECORATION_BLOCKS = new OdysseyCreativeModeTab("oddc_decoration_blocks", Lazy.of(ItemRegistry.RESEARCH_TABLE));
//    public static final CreativeModeTab REDSTONE = new OdysseyCreativeModeTab("oddc_redstone", Lazy.of(ItemRegistry.PALM_PRESSURE_PLATE));
//    public static final CreativeModeTab TRANSPORTATION = new OdysseyCreativeModeTab("oddc_transportation", Lazy.of(ItemRegistry.PALM_BOAT));
    public static final CreativeModeTab MATERIALS = new OdysseyCreativeModeTab("oddc_materials", Lazy.of(ItemRegistry.RAW_SILVER));
//    public static final CreativeModeTab TOOLS = new OdysseyCreativeModeTab("oddc_tools", Lazy.of(ItemRegistry.STERLING_SILVER_AXE));
//    public static final CreativeModeTab COMBAT = new OdysseyCreativeModeTab("oddc_combat", Lazy.of(ItemRegistry.STERLING_SILVER_SWORD));
    public static final CreativeModeTab ARMOR = new OdysseyCreativeModeTab("oddc_armor", Lazy.of(ItemRegistry.TURTLE_CHESTPLATE));
//    public static final CreativeModeTab MAGIC = new OdysseyCreativeModeTab("oddc_magic", Lazy.of(ItemRegistry.PURGE_TABLET));
//    public static final CreativeModeTab SPAWN_EGGS = new OdysseyCreativeModeTab("oddc_spawn_eggs", Lazy.of(ItemRegistry.ARCTIHORN_SPAWN_EGG));

    public OdysseyCreativeModeTab(String label, Lazy<Item> itemLazy){
        super(label);
        this.iconLazy = itemLazy;
    }

    public ItemStack makeIcon(){
        return this.iconLazy.get().getDefaultInstance();
    }
}