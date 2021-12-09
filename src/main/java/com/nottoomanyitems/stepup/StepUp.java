package com.nottoomanyitems.stepup;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import com.nottoomanyitems.stepup.Client.ClientProxy;
import com.nottoomanyitems.stepup.Server.ServerProxy;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod(StepUp.MODID)
@EventBusSubscriber(modid = StepUp.MODID, bus = Bus.MOD)
public final class StepUp {
    public static final String MODID = "stepup";
  
	public static final String MOD_VERSION = "1.18.0-0.1.1";
	public static final String MOD_NAME = "StepUp";
	public static String MC_VERSION;
	
	public static final StepUpProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
	
	public StepUp() {
    }
	
	@SubscribeEvent
	public static void onLoadComplete(final FMLLoadCompleteEvent e){
		proxy.onLoaded(e);
	}
}
