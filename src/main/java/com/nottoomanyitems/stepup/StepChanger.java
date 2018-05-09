package com.nottoomanyitems.stepup;

import com.nottoomanyitems.stepup.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class StepChanger {
    private static EntityPlayer player;
    public static KeyBinding myKey;
    public static int autoJumpState = -1; //0 StepUp, 1 None, 2 Minecraft
    public static Boolean firstRun = true;
    private String mc_version;
    
    private Minecraft mc = Minecraft.getMinecraft();

    public static void createKey() {
        myKey = new KeyBinding("Toggle StepUp", 36, "StepUp");
        ClientRegistry.registerKeyBinding((KeyBinding)myKey);
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.PlayerTickEvent event) {
    	boolean b = mc.gameSettings.getOptionOrdinalValue(Options.AUTO_JUMP);
        player = event.player;
        if(player.isSneaking()) {
        	player.stepHeight = .6f;
        }else if(autoJumpState == 0 && player.stepHeight < 1.0f){
        	player.stepHeight = 1.25f;
        }else if(autoJumpState == 1 && player.stepHeight >= 1.0f){
        	player.stepHeight = .6f;
        }else if(autoJumpState == 2 && player.stepHeight >= 1.0f){
        	player.stepHeight = .6f;
        }
        autoJump();
        
        if (firstRun && autoJumpState != -1) {
        	mc_version = Minecraft.getMinecraft().getVersion();
        	if(!Main.versionChecker.isLatestVersion()) {
        		updateMessage();
        	}
        	message();
            firstRun = false;
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (myKey.isPressed()) {
            if(autoJumpState == 0){
            	autoJumpState = 1;
            }else if(autoJumpState == 1){
            	autoJumpState = 2;
            }else if(autoJumpState == 2){
            	autoJumpState = 0;
            }
            autoJump();
            message();
            ConfigHandler.changeConfig();
        }
    }
    
    private void autoJump() {
    	boolean b = mc.gameSettings.getOptionOrdinalValue(Options.AUTO_JUMP);
    	if(autoJumpState < 2 && b == true) {
    		mc.gameSettings.setOptionValue(Options.AUTO_JUMP, 0);
    	}else if(autoJumpState == 2 && b == false) {
    		mc.gameSettings.setOptionValue(Options.AUTO_JUMP, 0);
    	}
    }
    
    private void message() {
    	String m = (Object)TextFormatting.DARK_AQUA + "[" + (Object)TextFormatting.YELLOW + "StepUp" + (Object)TextFormatting.DARK_AQUA + "]" + " ";
    	if(autoJumpState == 0) {
    		m = m + (Object)TextFormatting.GREEN + I18n.format("mod.stepup.enabled");
    	}else if(autoJumpState == 1) {
    		m = m + (Object)TextFormatting.RED + I18n.format("mod.stepup.disabled");
    	}else if(autoJumpState == 2) {
    		m = m + (Object)TextFormatting.GREEN + I18n.format("mod.stepup.minecraft") + " " + I18n.format("mod.stepup.autojump") + " " + I18n.format("mod.stepup.enabled");
    	}
    	
    	if(mc_version.contains("1.12")){
    		player.sendStatusMessage(new TextComponentString(m), true);
    	}else {
    		player.sendMessage((ITextComponent)new TextComponentString(m));
    	}
    }
    
    private void updateMessage() {
    	String m2 = (Object)TextFormatting.GOLD + "Update Available: " + (Object)TextFormatting.DARK_AQUA + "[" + (Object)TextFormatting.YELLOW + "StepUp" + (Object)TextFormatting.WHITE + " v" + VersionChecker.getLatestVersion() + (Object)TextFormatting.DARK_AQUA + "]";
		String url = "https://nottoomanyitems.wixsite.com/mods/step-up";
		ClickEvent versionCheckChatClickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
		TextComponentString component = new TextComponentString(m2);
		Style s = component.getStyle();
		s.setClickEvent(versionCheckChatClickEvent);
		component.setStyle(s);
		player.sendMessage((ITextComponent) component);
    }
}

