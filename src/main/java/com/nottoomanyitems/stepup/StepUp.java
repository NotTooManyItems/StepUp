package com.nottoomanyitems.stepup;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nottoomanyitems.stepup.config.ConfigIO;
import com.nottoomanyitems.stepup.worker.StepChanger;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(StepUp.MODID)
public class StepUp {
    public static final String MODID = "stepup";
	public static final String MOD_VERSION = "2.0.0";
	public static final String MOD_NAME = "StepUp";
	public static final String CONFIG_FILE = MODID+".cfg";
	public static boolean firstRun = false;
	public static boolean init = true;
	public static String MC_VERSION;
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static Minecraft mc = Minecraft.getInstance();

    public StepUp() {
    	if (Files.notExists(Paths.get("config", CONFIG_FILE))){
    		ConfigIO.createCFG();
        }
    	firstRun = true;
        //ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, StepUpConfig.CLIENTSPEC,CONFIG_FILE);
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
    }
	
    /*private void loadComplete(FMLLoadCompleteEvent event) {
		finishedLoading = true;
		//StepChanger.init();
		LOGGER.info("loadComplete");
	}*/
    
    @EventBusSubscriber(value = Dist.CLIENT)
    public static class ClientEventHandler {

        @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
        public static void clientTickEvent(final PlayerTickEvent event) {
        	if (event.player != null) {
        		StepChanger.TickEvent(event);
        	}
        	if (Minecraft.getInstance().isGameFocused() && init == true) {
        		init=false;
        		StepChanger.init();
        	}
        }
        
        @SubscribeEvent
        public static void onKeyInput(KeyInputEvent event) {
        	StepChanger.onKeyInput(event);
        }
        
        
        @SubscribeEvent
        public static void joinWorld(final EntityJoinWorldEvent event) {
        	//StepChanger.init();
        }
        
        /*@SubscribeEvent
        public static void loadComplete(FMLLoadCompleteEvent event) {
        	LOGGER.debug("loadComplete");
        }
        */
        
        @SubscribeEvent
        public static void unload (WorldEvent.Unload event) {
        	LOGGER.info("WorldEvent.Unload");
        	init=true;
        }
        
        @SubscribeEvent
        public static void load (WorldEvent.Load event) {
        	LOGGER.info("WorldEvent.Load");
        	//StepUpConfig.CLIENT.autoJumpState.get();
        	//StepChanger.init();
        }
    }
}
