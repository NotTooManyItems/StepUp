package com.nottoomanyitems.stepup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.GameSettings.Options;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.dimdev.rift.listener.client.ClientTickable;
import org.dimdev.rift.listener.client.KeyBindingAdder;
import org.dimdev.rift.listener.client.KeybindHandler;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_J;

public final class StepChanger implements KeyBindingAdder, KeybindHandler, ClientTickable {
    public KeyBinding myKey;
    public static int autoJumpState = -1; //0 StepUp, 1 None, 2 Minecraft
    public static boolean firstRun = true;
    public static String serverIP;
    private String mc_version;
    
    private Minecraft mc;

    @Override
    public Collection<? extends KeyBinding> getKeyBindings() {
        List<KeyBinding> myBindings=new ArrayList();
        myBindings.add(myKey = new KeyBinding("Toggle StepUp", GLFW_KEY_J, "StepUp"));
        return myBindings;
    }

    @Override
    public void clientTick(Minecraft client) {
        EntityPlayerSP player;
        mc=client;      // can't do this in onInit because mixins need to be applied first
        player = client.player;
        if (player==null)
            return;
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
        	mc_version = Minecraft.getInstance().getVersion();
        	message();
            firstRun = false;
        }
    }

    @Override
    public void processKeybinds() {
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
    		mc.player.sendStatusMessage(new TextComponentString(m), true);
    	}else {
    		mc.player.sendMessage((ITextComponent)new TextComponentString(m));
    	}
    }
}

