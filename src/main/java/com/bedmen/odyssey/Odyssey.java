package com.bedmen.odyssey;

import com.bedmen.odyssey.blocks.OdysseyWoodType;
import com.bedmen.odyssey.client.renderer.entity.renderer.*;
import com.bedmen.odyssey.container.OdysseyPlayerContainer;
import com.bedmen.odyssey.entity.boss.MineralLeviathanBodyEntity;
import com.bedmen.odyssey.entity.boss.MineralLeviathanEntity;
import com.bedmen.odyssey.entity.boss.PermafrostEntity;
import com.bedmen.odyssey.client.renderer.tileentity.SterlingSilverChestTileEntityRenderer;
import com.bedmen.odyssey.items.equipment.*;
import com.bedmen.odyssey.util.CompostUtil;
import com.bedmen.odyssey.client.gui.*;
import com.bedmen.odyssey.client.renderer.tileentity.OdysseyEnchantmentTableTileEntityRenderer;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.entity.attributes.OdysseyAttributes;
import com.bedmen.odyssey.entity.monster.ArctihornEntity;
import com.bedmen.odyssey.entity.monster.LupineEntity;
import com.bedmen.odyssey.items.*;
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
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
        DataSerializerRegistry.init();
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
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        //Tile Entity Renderings
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.BEACON.get(), OdysseyBeaconTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.ENCHANTING_TABLE.get(), OdysseyEnchantmentTableTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.STERLING_SILVER_CHEST.get(), SterlingSilverChestTileEntityRenderer::new);

        //Container Screens
        ScreenManager.register(ContainerRegistry.BEACON.get(), OdysseyBeaconScreen::new);
        ScreenManager.register(ContainerRegistry.SMITHING_TABLE.get(), OdysseySmithingTableScreen::new);
        ScreenManager.register(ContainerRegistry.ALLOY_FURNACE.get(), AlloyFurnaceScreen::new);
        ScreenManager.register(ContainerRegistry.ENCHANTMENT.get(), OdysseyEnchantmentScreen::new);
        ScreenManager.register(ContainerRegistry.BOOKSHELF.get(), BookshelfScreen::new);
        ScreenManager.register(ContainerRegistry.RECYCLE_FURNACE.get(), RecycleFurnaceScreen::new);
        ScreenManager.register(ContainerRegistry.RESEARCH_TABLE.get(), ResearchTableScreen::new);
        ScreenManager.register(ContainerRegistry.GRINDSTONE.get(), OdysseyGrindstoneScreen::new);
        ScreenManager.register(ContainerRegistry.ANVIL.get(), OdysseyAnvilScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER3.get(), QuiverScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER5.get(), QuiverScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER7.get(), QuiverScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER9.get(), QuiverScreen::new);

        //Mob Renderings
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.LUPINE.get(), LupineRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.ARCTIHORN.get(), ArctihornRenderer::new);

        //Boss Renderings
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST.get(), PermafrostRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.MINERAL_LEVIATHAN.get(), MineralLeviathanSegmentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), MineralLeviathanSegmentRenderer::new);

        //Projectile Renderings
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.TRIDENT.get(), OdysseyTridentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.LEVIATHAN_TRIDENT.get(), OdysseyTridentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST_ICICLE.get(), PermafrostIcicleRenderer::new);

        //Boat Renderings
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.BOAT.get(), OdysseyBoatRenderer::new);

        //Block Render Types
        RenderTypeLookup.setRenderLayer(BlockRegistry.RESEARCH_TABLE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockRegistry.PALM_LEAVES.get(), RenderType.cutoutMipped());
        RenderTypeLookup.setRenderLayer(BlockRegistry.COCONUT.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockRegistry.HOLLOW_COCONUT.get(), RenderType.cutout());
        for(Block block : OdysseyBlockTags.FOG_TAG)
            RenderTypeLookup.setRenderLayer(block, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.PERMAFROST_ICE2.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.PERMAFROST_ICE4.get(), RenderType.translucent());
        Atlases.addWoodType(OdysseyWoodType.PALM);
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        OdysseySpawnEggItem.initSpawnEggs();
    }

    @SubscribeEvent
    public static void onRegisterEnchantments(final RegistryEvent.Register<Enchantment> event){
        EquipmentArmorItem.initEquipment();
        EquipmentMeleeItem.initEquipment();
        EquipmentTrinketItem.initEquipment();
        EquipmentPickaxeItem.initEquipment();
        EquipmentHoeItem.initEquipment();
        EquipmentShovelItem.initEquipment();
        EquipmentAxeItem.initEquipment();
    }

    @SubscribeEvent
    public static void onTextureStitch(final TextureStitchEvent.Pre event){
        event.addSprite(OdysseyPlayerContainer.EMPTY_SLOT_TRINKET);
        event.addSprite(SterlingSilverChestTileEntityRenderer.SINGLE_RESOURCE_LOCATION);
        event.addSprite(SterlingSilverChestTileEntityRenderer.SINGLE_LOCKED_RESOURCE_LOCATION);
        event.addSprite(SterlingSilverChestTileEntityRenderer.LEFT_RESOURCE_LOCATION);
        event.addSprite(SterlingSilverChestTileEntityRenderer.LEFT_LOCKED_RESOURCE_LOCATION);
        event.addSprite(SterlingSilverChestTileEntityRenderer.RIGHT_RESOURCE_LOCATION);
        event.addSprite(SterlingSilverChestTileEntityRenderer.RIGHT_LOCKED_RESOURCE_LOCATION);
        event.addSprite(PermafrostRenderer.ACTIVE_SHELL_RESOURCE_LOCATION);
        event.addSprite(PermafrostRenderer.WIND_RESOURCE_LOCATION);
        event.addSprite(PermafrostRenderer.VERTICAL_WIND_RESOURCE_LOCATION);
        event.addSprite(PermafrostRenderer.OPEN_EYE_RESOURCE_LOCATION);
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(final EntityAttributeCreationEvent event){
        event.put(EntityTypeRegistry.LUPINE.get(), LupineEntity.createAttributes().build());
        event.put(EntityTypeRegistry.ARCTIHORN.get(), ArctihornEntity.createAttributes().build());
        event.put(EntityTypeRegistry.PERMAFROST.get(), PermafrostEntity.createAttributes().build());
        event.put(EntityTypeRegistry.MINERAL_LEVIATHAN.get(), MineralLeviathanEntity.createAttributes().build());
        event.put(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), MineralLeviathanBodyEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onColorHandlerEvent(final ColorHandlerEvent.Block event) {
        event.getBlockColors().register((p_228061_0_, p_228061_1_, p_228061_2_, p_228061_3_) -> {
            return p_228061_1_ != null && p_228061_2_ != null ? BiomeColors.getAverageFoliageColor(p_228061_1_, p_228061_2_) : FoliageColors.getDefaultColor();
        }, BlockRegistry.PALM_LEAVES.get());
    }

    @SubscribeEvent
    public static void onColorHandlerEvent(final ColorHandlerEvent.Item event) {
        event.getItemColors().register((p_210235_1_, p_210235_2_) -> {
            BlockState blockstate = ((BlockItem)(p_210235_1_).getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(blockstate, null, null, p_210235_2_);
        }, BlockRegistry.PALM_LEAVES.get());
    }
}