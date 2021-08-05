package com.bedmen.odyssey;

import com.bedmen.odyssey.client.gui.*;
import com.bedmen.odyssey.client.renderer.OdysseyEnchantmentTableTileEntityRenderer;
import com.bedmen.odyssey.client.renderer.entity.ArctihornRenderer;
import com.bedmen.odyssey.client.renderer.entity.NewTridentRenderer;
import com.bedmen.odyssey.client.renderer.entity.WerewolfRenderer;
import com.bedmen.odyssey.entity.attributes.OdysseyAttributes;
import com.bedmen.odyssey.entity.monster.ArctihornEntity;
import com.bedmen.odyssey.entity.monster.WerewolfEntity;
import com.bedmen.odyssey.items.*;
import com.bedmen.odyssey.items.equipment.EquipmentArmorItem;
import com.bedmen.odyssey.items.equipment.EquipmentItem;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.util.*;
import com.bedmen.odyssey.client.renderer.OdysseyBeaconTileEntityRenderer;
import com.bedmen.odyssey.potions.OdysseyPotions;
import com.bedmen.odyssey.trades.OdysseyTrades;
import com.bedmen.odyssey.world.gen.OdysseyFeatureGen;
import com.bedmen.odyssey.world.gen.OdysseyOreGen;
import com.bedmen.odyssey.world.spawn.OdysseyBiomeEntitySpawn;
import com.bedmen.odyssey.world.spawn.OdysseyStructureEntitySpawn;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Mod("oddc")
@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Odyssey
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "oddc";
    public static final RenderMaterial SERPENT_SHIELD_BASE = (new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(Odyssey.MOD_ID, "entity/serpent_shield_base")));
    public static final RenderMaterial SERPENT_SHIELD_BASE_NOPATTERN = (new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(Odyssey.MOD_ID,"entity/serpent_shield_base_nopattern")));
    public static final ItemGroup BUILDING_BLOCKS = new OdysseyItemGroup("oddc_building_blocks", Lazy.of(ItemRegistry.RUBY_ORE::get));
    public static final ItemGroup DECORATION_BLOCKS = new OdysseyItemGroup("oddc_decoration_blocks", Lazy.of(ItemRegistry.RESEARCH_TABLE::get));
    public static final ItemGroup MATERIALS = new OdysseyItemGroup("oddc_materials", Lazy.of(ItemRegistry.RUBY::get));
    public static final ItemGroup TOOLS = new OdysseyItemGroup("oddc_tools", Lazy.of(ItemRegistry.STERLING_SILVER_AXE::get));
    public static final ItemGroup COMBAT = new OdysseyItemGroup("oddc_combat", Lazy.of(ItemRegistry.STERLING_SILVER_SWORD::get));
    public static final ItemGroup SPAWN_EGGS = new OdysseyItemGroup("oddc_spawn_eggs", Lazy.of(ItemRegistry.ARCTIHORN_SPAWN_EGG::get));
    public static Tags.IOptionalNamedTag<Item> QUILL_TAG;


    public Odyssey() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        AttributeRegistry.init();
        BlockRegistry.init();
        ContainerRegistry.init();
        EffectRegistry.init();
        EnchantmentRegistry.init();
        EntityTypeRegistry.init();
        FeatureRegistry.init();
        ItemRegistry.init();
        PotionRegistry.init();
        RecipeRegistry.init();
        TileEntityTypeRegistry.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        OdysseyOreGen.registerOres();
        OdysseyFeatureGen.registerFeatures();
        OdysseyBiomeEntitySpawn.registerSpawners();
        OdysseyStructureEntitySpawn.registerSpawners();
        OdysseyPotions.addBrewingRecipes();
        OdysseyAttributes.fixArmor();
        OdysseyTrades.addTrades();
        EnchantmentUtil.init();
        OdysseyNetwork.init();

        Set<RenderMaterial> LOCATIONS_BUILTIN_TEXTURES = ObfuscationReflectionHelper.getPrivateValue(ModelBakery.class, null, "UNREFERENCED_TEXTURES");
        LOCATIONS_BUILTIN_TEXTURES.add(SERPENT_SHIELD_BASE);
        LOCATIONS_BUILTIN_TEXTURES.add(SERPENT_SHIELD_BASE_NOPATTERN);

        OdysseyPotionItem.RegisterBaseProperties(ItemRegistry.POTION.get());
        OdysseyPotionItem.RegisterBaseProperties(ItemRegistry.SPLASH_POTION.get());
        OdysseyPotionItem.RegisterBaseProperties(ItemRegistry.LINGERING_POTION.get());

        OdysseyBowItem.registerBaseProperties(ItemRegistry.BOW.get());
        OdysseyBowItem.registerBaseProperties(ItemRegistry.NETHERITE_BOW.get());

        OdysseyCrossbowItem.registerBaseProperties(ItemRegistry.CROSSBOW.get());
        OdysseyCrossbowItem.registerBaseProperties(ItemRegistry.NETHERITE_CROSSBOW.get());

        OdysseyTridentItem.registerBaseProperties(ItemRegistry.TRIDENT.get());
        OdysseyTridentItem.registerBaseProperties(ItemRegistry.SERPENT_TRIDENT.get());

        OdysseyShieldItem.registerBaseProperties(ItemRegistry.SHIELD.get());
        OdysseyShieldItem.registerBaseProperties(ItemRegistry.SERPENT_SHIELD.get());

        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.WEREWOLF.get(),EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WerewolfEntity::predicate);
        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.ARCTIHORN.get(),EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ArctihornEntity::predicate);



        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(EntityTypeRegistry.WEREWOLF.get(), WerewolfEntity.attributes().build());
            GlobalEntityTypeAttributes.put(EntityTypeRegistry.ARCTIHORN.get(), ArctihornEntity.attributes().build());
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.BEACON.get(), OdysseyBeaconTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.ENCHANTING_TABLE.get(), OdysseyEnchantmentTableTileEntityRenderer::new);

        ScreenManager.register(ContainerRegistry.BEACON.get(), OdysseyBeaconScreen::new);
        ScreenManager.register(ContainerRegistry.SMITHING_TABLE.get(), OdysseySmithingTableScreen::new);
        ScreenManager.register(ContainerRegistry.ALLOY_FURNACE.get(), AlloyFurnaceScreen::new);
        ScreenManager.register(ContainerRegistry.ENCHANTMENT.get(), OdysseyEnchantmentScreen::new);
        ScreenManager.register(ContainerRegistry.BOOKSHELF.get(), BookshelfScreen::new);
        ScreenManager.register(ContainerRegistry.RECYCLE_FURNACE.get(), RecycleFurnaceScreen::new);
        ScreenManager.register(ContainerRegistry.RESEARCH_TABLE.get(), ResearchTableScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER3.get(), QuiverScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER5.get(), QuiverScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER7.get(), QuiverScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER9.get(), QuiverScreen::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.WEREWOLF.get(), WerewolfRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.ARCTIHORN.get(), ArctihornRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.NEW_TRIDENT.get(), NewTridentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.SERPENT_TRIDENT.get(), NewTridentRenderer::new);

        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG1.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG2.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG3.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG4.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG5.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG6.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG7.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG8.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.RESEARCH_TABLE.get(), RenderType.cutout());
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        OdysseySpawnEggItem.initSpawnEggs();
    }

    @SubscribeEvent
    public static void onRegisterEnchantments(final RegistryEvent.Register<Enchantment> event){
        EquipmentArmorItem.initEquipment();
        EquipmentItem.initEquipment();
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event){
        Set<Supplier<Item>> set = new HashSet<>();
        set.add(ItemRegistry.BEWITCHED_QUILL);
        set.add(ItemRegistry.MALEVOLENT_QUILL);
        QUILL_TAG = ItemTags.createOptional(new ResourceLocation(MOD_ID, "quills"), set);
    }
}