package com.nottoomanyitems.stepup;

import com.nottoomanyitems.stepup.ConfigHandler;
import com.nottoomanyitems.stepup.NetHandler;
import com.nottoomanyitems.stepup.StepChanger;

import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonProxy {
	
    public void preInit(FMLPreInitializationEvent e) {
        FMLCommonHandler.instance().bus().register((Object)new NetHandler());
        StepChanger.createKey();
        ConfigHandler.load(e);
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
        FMLCommonHandler.instance().bus().register((Object)new StepChanger());
    }
}

