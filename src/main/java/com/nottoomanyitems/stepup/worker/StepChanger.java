package com.nottoomanyitems.stepup.worker;

import org.lwjgl.glfw.GLFW;

import com.nottoomanyitems.stepup.StepUp;
import com.nottoomanyitems.stepup.config.ConfigIO;
import com.nottoomanyitems.stepup.config.VersionChecker;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;

public class StepChanger {
	
	public static Player player;
    public static KeyMapping myKey;
    public static boolean firstRun = true;
    private static boolean forceStepUp = true;

    private static Minecraft mc = Minecraft.getInstance();

    public StepChanger() {
        
    }

    public static void TickEvent(PlayerTickEvent event) {
    	int autoJumpState = ConfigIO.autoJumpState;
    	player = event.player;
        
		if (player.isCrouching()) {
            player.maxUpStep = .6f;
        } else if (autoJumpState == AutoJumpState.DISABLED.getLevelCode() && player.maxUpStep >= 1.0f && forceStepUp == true) { //All Disabled
        	player.maxUpStep = .6f;
        	forceStepUp = false;
        } else if (autoJumpState == AutoJumpState.ENABLED.getLevelCode() && player.maxUpStep < 1.0f) { //StepUp Enabled
            player.maxUpStep = 1.25f;
            forceStepUp = true;
        } else if (autoJumpState == AutoJumpState.MINECRAFT.getLevelCode() && player.maxUpStep >= 1.0f) { //Minecraft Enabled
            player.maxUpStep = .6f;
            forceStepUp = true;
        }
        autoJump();
    }

    public static void init() {
    	ConfigIO.CheckForServerIP();
    	ConfigIO.updateCFG();
    	int display_update_message = ConfigIO.display_update_message;
    	int display_start_message = ConfigIO.display_start_message;
    	
    	if(firstRun){
    	    myKey =  new KeyMapping("key.stepup.desc", GLFW.GLFW_KEY_H, "key.categories.stepup");
    	    net.minecraftforge.fmlclient.registry.ClientRegistry.registerKeyBinding(myKey);
            if (VersionChecker.isLatestVersion() == false && display_update_message == 1) {
                updateMessage();
            }
            firstRun = false;
    	}
    	autoJump();
    	if(display_start_message == 1){
    		message();
    	}
    }
    
    //@SubscribeEvent
    public static void onKeyInput(KeyInputEvent event) {
        if (StepChanger.myKey.isDown()) {
        	int autoJumpState = ConfigIO.autoJumpState;
            if (autoJumpState == AutoJumpState.MINECRAFT.getLevelCode()) {
            	ConfigIO.autoJumpState = AutoJumpState.DISABLED.getLevelCode(); //0 StepUp and Minecraft Disabled
            } else if (autoJumpState == AutoJumpState.DISABLED.getLevelCode()) {
            	ConfigIO.autoJumpState = AutoJumpState.ENABLED.getLevelCode(); //1 StepUp Enabled
            } else if (autoJumpState == AutoJumpState.ENABLED.getLevelCode()) {
            	ConfigIO.autoJumpState = AutoJumpState.MINECRAFT.getLevelCode(); //2 Minecraft Enabled
            }
            ConfigIO.updateCFG();
            autoJump();
            message();
        }
    }

    private static void autoJump() {    	
    	net.minecraft.client.Options settings = mc.options;
    	int autoJumpState = ConfigIO.autoJumpState;
    	
        boolean b = settings.autoJump;
        if (autoJumpState < AutoJumpState.MINECRAFT.getLevelCode() && b == true) {
            settings.autoJump = false;
        } else if (autoJumpState == AutoJumpState.MINECRAFT.getLevelCode() && b == false) {
            settings.autoJump = true;
        }
    }

    private static void message() {
    	int autoJumpState = ConfigIO.autoJumpState;
        String m = (Object) ChatFormatting.DARK_AQUA + "[" + (Object) ChatFormatting.YELLOW + StepUp.MOD_NAME + (Object) ChatFormatting.DARK_AQUA + "]" + " ";
        if (autoJumpState == AutoJumpState.DISABLED.getLevelCode()) {
            m = m + (Object) ChatFormatting.RED + I18n.get(AutoJumpState.DISABLED.getDesc());
        } else if (autoJumpState == AutoJumpState.ENABLED.getLevelCode()) {
            m = m + (Object) ChatFormatting.BLUE + I18n.get(AutoJumpState.ENABLED.getDesc());
        } else if (autoJumpState == AutoJumpState.MINECRAFT.getLevelCode()) {
            m = m + (Object) ChatFormatting.GREEN + I18n.get(AutoJumpState.MINECRAFT.getDesc());
        }
        player.displayClientMessage(new TextComponent(m), true);
    }
    
    private static void updateMessage() {
        String m2 = (Object) ChatFormatting.GOLD + I18n.get("msg.stepup.updateAvailable") + ": " + (Object) ChatFormatting.DARK_AQUA + "[" + (Object) ChatFormatting.YELLOW + "StepUp-" + (Object) ChatFormatting.WHITE + VersionChecker.getLatestVersion() + (Object) ChatFormatting.DARK_AQUA + "]";
        String url = "https://www.curseforge.com/minecraft/mc-mods/stepup/files";
        ClickEvent versionCheckChatClickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL,url);
        HoverEvent versionCheckChatHoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(I18n.get("msg.stepup.updateTooltip") + "!"));
        TextComponent component = new TextComponent(m2);
        Style s = component.getStyle();
        s.withClickEvent(versionCheckChatClickEvent);	//setClickEvent
        s.withHoverEvent(versionCheckChatHoverEvent);	//setHoverEvent
        component.setStyle(s);
        player.sendMessage((TextComponent) component, null);
    }
    
    public enum AutoJumpState
    {
        DISABLED (0,"msg.stepup.disabled"), //StepUp Enabled
        ENABLED (1,"msg.stepup.enabled"), //"All Disabled" 
        MINECRAFT (2,"msg.stepup.minecraft"); //"Minecraft Jump Enabled" 
        
        private final int levelCode;
        private final String desc;

        AutoJumpState(int levelCode, String desc) {
            this.levelCode = levelCode;
            this.desc = desc;
        }
        
        public int getLevelCode() {
            return this.levelCode;
        }

		public String getDesc() {
			return this.desc;
		}
    }

}
