package com.bedmen.odyssey;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import util.RegistryHandler;
import world.gen.ModOreGen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import client.entity.render.RubyGolemRenderer;
import client.gui.AlloyFurnaceScreen;
import container.AlloyFurnaceContainer;
import entities.RubyGolemEntity;

import java.util.stream.Collectors;

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
    	
    	DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(RegistryHandler.RUBY_GOLEM.get(), RubyGolemEntity.setCustomAttributes().create());
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) 
    {
    	ScreenManager.registerFactory(RegistryHandler.ALLOY_FURNACE_CONTAINER.get(), AlloyFurnaceScreen::new);
    	RenderingRegistry.registerEntityRenderingHandler(RegistryHandler.RUBY_GOLEM.get(), RubyGolemRenderer::new);
    }
}