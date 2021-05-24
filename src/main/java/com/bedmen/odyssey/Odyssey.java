package com.bedmen.odyssey;

import com.bedmen.odyssey.client.gui.*;
import com.bedmen.odyssey.client.renderer.NewEnchantmentTableTileEntityRenderer;
import com.bedmen.odyssey.client.renderer.entity.NewTridentRenderer;
import com.bedmen.odyssey.client.renderer.entity.WerewolfRenderer;
import com.bedmen.odyssey.entity.monster.WerewolfEntity;
import com.bedmen.odyssey.items.*;
import com.bedmen.odyssey.util.*;
import com.bedmen.odyssey.client.renderer.NewBeaconTileEntityRenderer;
import com.bedmen.odyssey.potions.ModPotions;
import com.bedmen.odyssey.trades.ModTrades;
import com.bedmen.odyssey.world.gen.ModOreGen;
import com.bedmen.odyssey.world.spawn.ModBiomeEntitySpawn;
import com.bedmen.odyssey.world.spawn.ModStructureEntitySpawn;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
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
    public static final RenderMaterial SERPENT_SHIELD_BASE = (new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(Odyssey.MOD_ID, "entity/serpent_shield_base")));
    public static final RenderMaterial SERPENT_SHIELD_BASE_NOPATTERN = (new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(Odyssey.MOD_ID,"entity/serpent_shield_base_nopattern")));

    public Odyssey() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        AttributeRegistry.init();
        BlockRegistry.init();
        ContainerRegistry.init();
        EffectRegistry.init();
        EnchantmentRegistry.init();
        EntityTypeRegistry.init();
        ItemRegistry.init();
        PotionRegistry.init();
        RecipeRegistry.init();
        TileEntityTypeRegistry.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        ModOreGen.registerOres();
        ModBiomeEntitySpawn.registerSpawners();
        ModStructureEntitySpawn.registerSpawners();
        ModPotions.addBrewingRecipes();
        ModTrades.addTrades();
        EnchantmentUtil.init();

        Set<RenderMaterial> LOCATIONS_BUILTIN_TEXTURES = ObfuscationReflectionHelper.getPrivateValue(ModelBakery.class, null, "LOCATIONS_BUILTIN_TEXTURES");
        LOCATIONS_BUILTIN_TEXTURES.add(SERPENT_SHIELD_BASE);
        LOCATIONS_BUILTIN_TEXTURES.add(SERPENT_SHIELD_BASE_NOPATTERN);

        NewPotionItem.RegisterBaseProperties(ItemRegistry.POTION.get());
        NewPotionItem.RegisterBaseProperties(ItemRegistry.SPLASH_POTION.get());
        NewPotionItem.RegisterBaseProperties(ItemRegistry.LINGERING_POTION.get());

        NewBowItem.registerBaseProperties(ItemRegistry.BOW.get());
        NewBowItem.registerStringTypeProperty(ItemRegistry.BOW.get());
        NewBowItem.registerBaseProperties(ItemRegistry.NETHERITE_BOW.get());
        NewBowItem.registerStringTypeProperty(ItemRegistry.NETHERITE_BOW.get());

        NewCrossbowItem.registerBaseProperties(ItemRegistry.CROSSBOW.get());
        NewCrossbowItem.registerStringTypeProperty(ItemRegistry.CROSSBOW.get());
        NewCrossbowItem.registerBaseProperties(ItemRegistry.NETHERITE_CROSSBOW.get());
        NewCrossbowItem.registerStringTypeProperty(ItemRegistry.NETHERITE_CROSSBOW.get());

        NewTridentItem.registerBaseProperties(ItemRegistry.TRIDENT.get());
        NewTridentItem.registerBaseProperties(ItemRegistry.SERPENT_TRIDENT.get());

        NewShieldItem.registerBaseProperties(ItemRegistry.SHIELD.get());
        NewShieldItem.registerBaseProperties(ItemRegistry.SERPENT_SHIELD.get());

        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.WEREWOLF.get(),EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WerewolfEntity::predicate);

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(EntityTypeRegistry.WEREWOLF.get(), WerewolfEntity.func_234233_eS_().create());
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.BEACON.get(), NewBeaconTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.ENCHANTING_TABLE.get(), NewEnchantmentTableTileEntityRenderer::new);

        ScreenManager.registerFactory(ContainerRegistry.BEACON.get(), NewBeaconScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.SMITHING_TABLE.get(), NewSmithingTableScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.ALLOY_FURNACE.get(), AlloyFurnaceScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.INFUSER.get(), InfuserScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.ENCHANTMENT.get(), NewEnchantmentScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.BOOKSHELF.get(), BookshelfScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.FLETCHING_TABLE.get(), FletchingTableScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.RECYCLE_FURNACE.get(), RecycleFurnaceScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.QUIVER3.get(), QuiverScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.QUIVER5.get(), QuiverScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.QUIVER7.get(), QuiverScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.QUIVER9.get(), QuiverScreen::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.WEREWOLF.get(), WerewolfRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.NEW_TRIDENT.get(), NewTridentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.SERPENT_TRIDENT.get(), NewTridentRenderer::new);

        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG1.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG2.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG3.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG4.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG5.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG6.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG7.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlockRegistry.FOG8.get(), RenderType.getTranslucent());
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        ModSpawnEggItem.initSpawnEggs();
    }
}