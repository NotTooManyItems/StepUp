package com.nottoomanyitems.stepup.Client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nottoomanyitems.stepup.StepUp;
import com.nottoomanyitems.stepup.worker.StepChanger;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT,  modid = StepUp.MODID)
public class ClientEvents {
	public static boolean init = true;
	private static final Logger LOGGER = LogManager.getLogger("StepUp");
	
	@SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public static void clientTickEvent(final PlayerTickEvent event) {
    	if (event.player != null) {
    		StepChanger.TickEvent(event);
    	}
    	if (Minecraft.getInstance().isWindowActive() && init == true) {
    		init=false;
    		StepChanger.init();
    	}
    }
    
	
    @SubscribeEvent
    public static void onKeyInput(KeyInputEvent event) {
    	if(StepChanger.firstRun == false) {
    		StepChanger.onKeyInput(event);
    	}
    }
    
    
    @SubscribeEvent
    public static void joinWorld(final EntityJoinWorldEvent event) {
    }
    
    @SubscribeEvent
    public static void unload (WorldEvent.Unload event) {
    	LOGGER.info("WorldEvent.Unload");
    	init=true;
    }
    
    @SubscribeEvent
    public static void load (WorldEvent.Load event) {
    	LOGGER.info("WorldEvent.Load");
    }
}
