package com.nottoomanyitems.stepup;

import com.nottoomanyitems.stepup.mixins.NetHandler;
import de.guntram.mcmod.rifttools.ConfigChangedEvent;
import de.guntram.mcmod.rifttools.Configuration;
import de.guntram.mcmod.rifttools.ModConfigurationHandler;
import java.io.File;

public class ConfigHandler implements ModConfigurationHandler {
    public static Configuration config;

    public static void load(File file) {
        config = new Configuration(file);
    }

    public static void reloadConfig() {
        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void loadConfig() {
    	StepChanger.autoJumpState = config.getInt(StepChanger.serverIP,
                Configuration.CATEGORY_CLIENT, 0, 0, 2, "autoJump state on this server");
        System.out.println("setting state on "+StepChanger.serverIP+" to "+StepChanger.autoJumpState);
        ConfigHandler.reloadConfig();
    }

    public static void changeConfig() {
        config.setValue(StepChanger.serverIP, StepChanger.autoJumpState);
        ConfigHandler.reloadConfig();
    }

    @Override
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals("stepup")) {
            ConfigHandler.reloadConfig();
        }
    }

    @Override
    public Configuration getConfig() {
        return config;
    }
}

