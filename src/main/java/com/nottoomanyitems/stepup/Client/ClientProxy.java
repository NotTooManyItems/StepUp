package com.nottoomanyitems.stepup.Client;

import com.nottoomanyitems.stepup.config.VersionChecker;
import com.nottoomanyitems.stepup.StepUpProxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;


public final class ClientProxy extends StepUpProxy{
	public String MC_VERSION;
	
	@Override
	public void onConstructed(ModLoadingContext ctx){
	}

	@Override
	public void onLoaded(FMLLoadCompleteEvent e) {
		MC_VERSION = Minecraft.getInstance().getLaunchedVersion();
		
    	VersionChecker versionChecker = new VersionChecker();
        Thread versionCheckThread = new Thread(versionChecker, "Version Check");
        versionCheckThread.start();
	}
}
