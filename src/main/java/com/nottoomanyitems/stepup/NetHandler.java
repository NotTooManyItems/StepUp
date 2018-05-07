package com.nottoomanyitems.stepup;

import com.google.common.util.concurrent.ListenableFuture;
import com.nottoomanyitems.stepup.ConfigHandler;
import com.nottoomanyitems.stepup.StepChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetHandler {
    public static String serverIP;

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void clientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        Minecraft mainThread = Minecraft.func_71410_x();
        mainThread.func_152344_a(new Runnable(){

            @Override
            public void run() {
                ServerData serverData = Minecraft.func_71410_x().func_147104_D();
                NetHandler.serverIP = serverData != null ? serverData.field_78845_b.replace(".", "") : "0000";
                StepChanger.firstRun = true;
                ConfigHandler.loadConfig();
            }
        });
    }

}

