package com.nottoomanyitems.stepup;

import de.guntram.mcmod.rifttools.ConfigurationProvider;
import org.dimdev.riftloader.listener.InitializationListener;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public class Main implements InitializationListener {
    @Override
    public void onInitialization() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.stepup.json");
        Mixins.addConfiguration("mixins.rifttools-de-guntram.json");
        ConfigurationProvider.register("StepUp", new ConfigHandler());
        ConfigHandler.load(ConfigurationProvider.getSuggestedFile("StepUp"));
    }
}
