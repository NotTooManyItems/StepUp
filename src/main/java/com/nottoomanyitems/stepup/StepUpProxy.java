package com.nottoomanyitems.stepup;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public abstract class StepUpProxy {
	public abstract void onConstructed(ModLoadingContext ctx);
	public abstract void onLoaded(FMLLoadCompleteEvent e);
}
