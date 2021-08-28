package com.bedmen.odyssey;

import com.bedmen.odyssey.client.renderer.entity.*;
import com.bedmen.odyssey.entity.boss.PermafrostEntity;
import com.bedmen.odyssey.util.CompostUtil;
import com.bedmen.odyssey.client.gui.*;
import com.bedmen.odyssey.client.renderer.tileentity.OdysseyEnchantmentTableTileEntityRenderer;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.entity.attributes.OdysseyAttributes;
import com.bedmen.odyssey.entity.monster.ArctihornEntity;
import com.bedmen.odyssey.entity.monster.LupineEntity;
import com.bedmen.odyssey.items.*;
import com.bedmen.odyssey.items.equipment.EquipmentArmorItem;
import com.bedmen.odyssey.items.equipment.EquipmentTieredItem;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.registry.*;
import com.bedmen.odyssey.tags.OdysseyBlockTags;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.bedmen.odyssey.client.renderer.tileentity.OdysseyBeaconTileEntityRenderer;
import com.bedmen.odyssey.potions.OdysseyPotions;
import com.bedmen.odyssey.trades.OdysseyTrades;
import com.bedmen.odyssey.world.gen.OdysseyFeatureGen;
import com.bedmen.odyssey.world.gen.OdysseyOreGen;
import com.bedmen.odyssey.world.spawn.OdysseyBiomeEntitySpawn;
import com.bedmen.odyssey.world.spawn.OdysseyStructureEntitySpawn;
import net.minecraft.block.Block;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
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

import java.util.Set;

@Mod("oddc")
@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Odyssey
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "oddc";
    public static final RenderMaterial LEVIATHAN_SHIELD_BASE = (new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(Odyssey.MOD_ID, "entity/leviathan_shield_base")));
    public static final RenderMaterial LEVIATHAN_SHIELD_BASE_NOPATTERN = (new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(Odyssey.MOD_ID,"entity/leviathan_shield_base_nopattern")));

    public Odyssey() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        AttributeRegistry.init();
        BiomeRegistry.init();
        BlockRegistry.init();
        ContainerRegistry.init();
        EffectRegistry.init();
        EnchantmentRegistry.init();
        EntityTypeRegistry.init();
        FeatureRegistry.init();
        ItemRegistry.init();
        PotionRegistry.init();
        RecipeRegistry.init();
        SoundEventRegistry.init();
        TileEntityTypeRegistry.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        OdysseyOreGen.registerOres();
        OdysseyFeatureGen.registerFeatures();
        OdysseyBiomeEntitySpawn.registerSpawners();
        OdysseyStructureEntitySpawn.registerSpawners();
        OdysseyPotions.addBrewingRecipes();
        CompostUtil.addCompostingRecipes();
        OdysseyAttributes.fixArmor();
        OdysseyTrades.addTrades();
        EnchantmentUtil.init();
        OdysseyNetwork.init();
        OdysseyItemTags.init();
        OdysseyBlockTags.init();
        BiomeRegistry.register();

        Set<RenderMaterial> LOCATIONS_BUILTIN_TEXTURES = ObfuscationReflectionHelper.getPrivateValue(ModelBakery.class, null, "UNREFERENCED_TEXTURES");
        LOCATIONS_BUILTIN_TEXTURES.add(LEVIATHAN_SHIELD_BASE);
        LOCATIONS_BUILTIN_TEXTURES.add(LEVIATHAN_SHIELD_BASE_NOPATTERN);

        OdysseyPotionItem.RegisterBaseProperties(ItemRegistry.POTION.get());
        OdysseyPotionItem.RegisterBaseProperties(ItemRegistry.SPLASH_POTION.get());
        OdysseyPotionItem.RegisterBaseProperties(ItemRegistry.LINGERING_POTION.get());

        for(Item item : OdysseyItemTags.BOW_TAG)
            OdysseyBowItem.registerBaseProperties(item);
        for(Item item : OdysseyItemTags.CROSSBOW_TAG)
            OdysseyCrossbowItem.registerBaseProperties(item);
        for(Item item : OdysseyItemTags.TRIDENT_TAG)
            OdysseyTridentItem.registerBaseProperties(item);
        for(Item item : OdysseyItemTags.SHIELD_TAG)
            OdysseyShieldItem.registerBaseProperties(item);

        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.LUPINE.get(),EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, LupineEntity::predicate);
        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.ARCTIHORN.get(),EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ArctihornEntity::predicate);

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(EntityTypeRegistry.LUPINE.get(), LupineEntity.createAttributes().build());
            GlobalEntityTypeAttributes.put(EntityTypeRegistry.ARCTIHORN.get(), ArctihornEntity.createAttributes().build());
            GlobalEntityTypeAttributes.put(EntityTypeRegistry.PERMAFROST.get(), PermafrostEntity.createAttributes().build());
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        //Tile Entity Renderings
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.BEACON.get(), OdysseyBeaconTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.ENCHANTING_TABLE.get(), OdysseyEnchantmentTableTileEntityRenderer::new);

        //Container Screens
        ScreenManager.register(ContainerRegistry.BEACON.get(), OdysseyBeaconScreen::new);
        ScreenManager.register(ContainerRegistry.SMITHING_TABLE.get(), OdysseySmithingTableScreen::new);
        ScreenManager.register(ContainerRegistry.ALLOY_FURNACE.get(), AlloyFurnaceScreen::new);
        ScreenManager.register(ContainerRegistry.ENCHANTMENT.get(), OdysseyEnchantmentScreen::new);
        ScreenManager.register(ContainerRegistry.BOOKSHELF.get(), BookshelfScreen::new);
        ScreenManager.register(ContainerRegistry.RECYCLE_FURNACE.get(), RecycleFurnaceScreen::new);
        ScreenManager.register(ContainerRegistry.RESEARCH_TABLE.get(), ResearchTableScreen::new);
        ScreenManager.register(ContainerRegistry.GRINDSTONE.get(), OdysseyGrindstoneScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER3.get(), QuiverScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER5.get(), QuiverScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER7.get(), QuiverScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER9.get(), QuiverScreen::new);

        //Mob Renderings
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.LUPINE.get(), LupineRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.ARCTIHORN.get(), ArctihornRenderer::new);

        //Boss Renderings
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST.get(), PermafrostRenderer::new);

        //Projectile Renderings
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.TRIDENT.get(), OdysseyTridentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.LEVIATHAN_TRIDENT.get(), OdysseyTridentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST_ICICLE.get(), PermafrostIcicleRenderer::new);

        //Block Render Types
        RenderTypeLookup.setRenderLayer(BlockRegistry.RESEARCH_TABLE.get(), RenderType.cutout());
        for(Block block : OdysseyBlockTags.FOG_TAG)
            RenderTypeLookup.setRenderLayer(block, RenderType.translucent());

        RenderTypeLookup.setRenderLayer(BlockRegistry.PERMAFROST_ICE2.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.PERMAFROST_ICE4.get(), RenderType.translucent());
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        OdysseySpawnEggItem.initSpawnEggs();
    }

    @SubscribeEvent
    public static void onRegisterEnchantments(final RegistryEvent.Register<Enchantment> event){
        EquipmentArmorItem.initEquipment();
        EquipmentTieredItem.initEquipment();
    }
}