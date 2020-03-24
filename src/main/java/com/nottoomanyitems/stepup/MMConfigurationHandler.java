package com.nottoomanyitems.stepup;

import de.guntram.mcmod.fabrictools.ConfigurationProvider;
import de.guntram.mcmod.fabrictools.GuiModOptions;
import io.github.prospector.modmenu.api.ModMenuApi;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.gui.Screen;

public class MMConfigurationHandler implements ModMenuApi
{
    @Override
    public String getModId() {
        return "stepup";
    }

    @Override
    public Optional<Supplier<Screen>> getConfigScreen(Screen screen) {
        return Optional.of(new GuiModOptions(screen, "StepUp", ConfigurationProvider.getHandler("StepUp")));
    }
}
