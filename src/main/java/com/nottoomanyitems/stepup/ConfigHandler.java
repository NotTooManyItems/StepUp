package com.nottoomanyitems.stepup;

import com.nottoomanyitems.stepup.NetHandler;
import com.nottoomanyitems.stepup.StepChanger;
import java.io.File;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigHandler {
    public static Configuration config;

    public static void load(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register((Object)new ConfigHandler());
    }

    public static void reloadConfig() {
        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void loadConfig() {
    	Property state = config.get(NetHandler.serverIP, "autoJumpState", "0");
    	StepChanger.autoJumpState = state.getInt();
        ConfigHandler.reloadConfig();
    }

    public static void changeConfig() {
    	String autoJumpState = Integer.toString(StepChanger.autoJumpState);
    	Property b = config.get(NetHandler.serverIP, "autoJumpState", autoJumpState);
        b.set(autoJumpState);
        ConfigHandler.reloadConfig();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals("stepup")) {
            ConfigHandler.reloadConfig();
        }
    }
}

