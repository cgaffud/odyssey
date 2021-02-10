package com.bedmen.odyssey;

import com.bedmen.odyssey.client.gui.*;
import com.bedmen.odyssey.client.renderer.NewEnchantmentTableTileEntityRenderer;
import com.bedmen.odyssey.client.renderer.entity.TrooperRenderer;
import com.bedmen.odyssey.entity.TrooperEntity;
import com.bedmen.odyssey.items.ModSpawnEggItem;
import com.bedmen.odyssey.items.NewBowItem;
import com.bedmen.odyssey.items.NewCrossbowItem;
import com.bedmen.odyssey.items.NewPotionItem;
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
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("oddc")
@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Odyssey
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "oddc";

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

    private void setup(final FMLCommonSetupEvent event)
    {
        ModOreGen.registerOres();
        ModBiomeEntitySpawn.registerSpawners();
        ModStructureEntitySpawn.registerSpawners();
        ModPotions.addBrewingRecipes();
        ModTrades.addTrades();

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

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(EntityTypeRegistry.TROOPER.get(), TrooperEntity.registerAttributes().create());
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
        ScreenManager.registerFactory(ContainerRegistry.QUIVER3.get(), QuiverScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.QUIVER5.get(), QuiverScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.QUIVER7.get(), QuiverScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.QUIVER9.get(), QuiverScreen::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.TROOPER.get(), TrooperRenderer::new);

        RenderTypeLookup.setRenderLayer(BlockRegistry.WARPING_FIRE.get(), RenderType.getCutout());
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        ModSpawnEggItem.initSpawnEggs();
    }
}