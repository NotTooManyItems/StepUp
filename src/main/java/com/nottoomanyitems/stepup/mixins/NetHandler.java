package com.nottoomanyitems.stepup.mixins;

import com.nottoomanyitems.stepup.ConfigHandler;
import com.nottoomanyitems.stepup.StepChanger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.packet.GameJoinS2CPacket;
import net.minecraft.client.options.ServerEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class NetHandler {
    
    @Inject(method="onGameJoin", at=@At("RETURN"))
    private void onConnectedToServerEvent(GameJoinS2CPacket packet, CallbackInfo cbi) {
        MinecraftClient mc=MinecraftClient.getInstance();
        ServerEntry serverData = mc.getCurrentServerEntry();
        StepChanger.serverIP = serverData != null ? serverData.address.replace(".", "") : "0000";
        System.out.println("connected to "+StepChanger.serverIP);
        StepChanger.firstRun = true;
        ConfigHandler.loadConfig();
    }
}
