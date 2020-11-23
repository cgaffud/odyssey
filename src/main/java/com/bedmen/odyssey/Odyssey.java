package com.bedmen.odyssey;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import potions.ModPotions;
import util.RegistryHandler;
import world.gen.ModOreGen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import client.entity.render.RubyGolemRenderer;
import client.gui.AlloyFurnaceScreen;
import client.gui.NewSmithingTableScreen;
import entities.RubyGolemEntity;

@Mod("oddc")
public class Odyssey
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "oddc";

    public Odyssey() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        
        RegistryHandler.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	ModOreGen.registerOres();
    	ModPotions.addBrewingRecipes();
    	
    	DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(RegistryHandler.RUBY_GOLEM.get(), RubyGolemEntity.setCustomAttributes().create());
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) 
    {
    	ScreenManager.registerFactory(RegistryHandler.ALLOY_FURNACE_CONTAINER.get(), AlloyFurnaceScreen::new);
    	ScreenManager.registerFactory(RegistryHandler.SMITHING_TABLE_CONTAINER.get(), NewSmithingTableScreen::new);
    	RenderingRegistry.registerEntityRenderingHandler(RegistryHandler.RUBY_GOLEM.get(), RubyGolemRenderer::new);
    }
}