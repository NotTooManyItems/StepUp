package com.nottoomanyitems.stepup.mixins;

import com.nottoomanyitems.stepup.ConfigHandler;
import com.nottoomanyitems.stepup.StepChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketJoinGame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class NetHandler {
    
    @Inject(method="handleJoinGame", at=@At("RETURN"))
    private void onConnectedToServerEvent(SPacketJoinGame packet, CallbackInfo cbi) {
        Minecraft mc=Minecraft.getInstance();
        ServerData serverData = mc.getCurrentServerData();
        StepChanger.serverIP = serverData != null ? serverData.serverIP.replace(".", "") : "0000";
        System.out.println("connected to "+StepChanger.serverIP);
        StepChanger.firstRun = true;
        ConfigHandler.loadConfig();
    }
}
