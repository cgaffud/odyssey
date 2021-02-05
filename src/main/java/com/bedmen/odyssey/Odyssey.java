package com.bedmen.odyssey;

import com.bedmen.odyssey.client.gui.*;
import com.bedmen.odyssey.client.renderer.NewEnchantmentTableTileEntityRenderer;
import com.bedmen.odyssey.items.ModSpawnEggItem;
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
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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

        ItemModelsProperties.registerProperty(Items.POTION, new ResourceLocation("type"),  (itemStack, world, entity) -> {
            CompoundNBT compoundnbt = itemStack.getTag();
            if(compoundnbt != null && compoundnbt.contains("Potion")){
                String s = compoundnbt.get("Potion").getString();
                if(s.contains("awkward")) return 3;
                else if(s.contains("long")) return 1;
                else if(s.contains("strong")) return 2;
            }
            return 0;
        });

        ItemModelsProperties.registerProperty(Items.SPLASH_POTION, new ResourceLocation("type"),  (itemStack, world, entity) -> {
            CompoundNBT compoundnbt = itemStack.getTag();
            if(compoundnbt != null && compoundnbt.contains("Potion")){
                String s = compoundnbt.get("Potion").getString();
                if(s.contains("awkward")) return 3;
                else if(s.contains("long")) return 1;
                else if(s.contains("strong")) return 2;
            }
            return 0;
        });

        ItemModelsProperties.registerProperty(Items.LINGERING_POTION, new ResourceLocation("type"),  (itemStack, world, entity) -> {
            CompoundNBT compoundnbt = itemStack.getTag();
            if(compoundnbt != null && compoundnbt.contains("Potion")){
                String s = compoundnbt.get("Potion").getString();
                if(s.contains("awkward")) return 3;
                else if(s.contains("long")) return 1;
                else if(s.contains("strong")) return 2;
            }
            return 0;
        });

        DeferredWorkQueue.runLater(() -> {
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
        RenderTypeLookup.setRenderLayer(BlockRegistry.WARPING_FIRE.get(), RenderType.getCutout());
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        ModSpawnEggItem.initSpawnEggs();
    }
}