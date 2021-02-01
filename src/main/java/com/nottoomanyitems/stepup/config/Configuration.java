package com.nottoomanyitems.stepup.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import org.apache.commons.lang3.tuple.Pair;

public class Configuration {

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;

    static {
        Pair<Client,ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);

        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }

    public static class Client {

        public BooleanValue showStepUpInfoOnWorldJoin;

        Client(ForgeConfigSpec.Builder builder)
        {
            showStepUpInfoOnWorldJoin = builder
                    .comment("Whether info about StepUp will be show when joining a world.")
                    .translation("config.stepup.showStepUpInfoOnWorldJoin")
                    .define("showStepUpInfoOnWorldJoin", true);
        }
    }
}
