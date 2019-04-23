package com.nottoomanyitems.stepup;

import de.guntram.mcmod.fabrictools.ConfigurationProvider;
import de.guntram.mcmod.fabrictools.KeyBindingManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;

public class Main implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigurationProvider.register("StepUp", new ConfigHandler());
        ConfigHandler.load(ConfigurationProvider.getSuggestedFile("stepup"));
        StepChanger stepChanger = new StepChanger();
        stepChanger.setKeyBindings();
        ClientTickCallback.EVENT.register(stepChanger);
        KeyBindingManager.register(stepChanger);
    }
}
