package com.nottoomanyitems.stepup.worker;

import com.nottoomanyitems.stepup.StepUp;
import com.nottoomanyitems.stepup.config.ConfigIO;
import com.nottoomanyitems.stepup.config.VersionChecker;
import com.nottoomanyitems.stepup.init.KeyBindings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;

public class StepChanger {
	
	public static PlayerEntity player;
    public KeyBinding myKey;
    public static boolean firstRun = true;
    private static boolean forceStepUp = true;

    private static Minecraft mc = Minecraft.getInstance();

    public StepChanger() {
        
    }

    public static void TickEvent(PlayerTickEvent event) {
        player = event.player;
        int autoJumpState = ConfigIO.autoJumpState;
        
		if (player.isCrouching()) {
            player.stepHeight = .6f;
        } else if (autoJumpState == 0 && player.stepHeight >= 1.0f && forceStepUp == true) { //All Disabled
        	player.stepHeight = .6f;
        	forceStepUp = false;
        	//player.sendMessage((ITextComponent) new StringTextComponent("ASDJKJKJSD"), Util.field_240973_b_);
        } else if (autoJumpState == 1 && player.stepHeight < 1.0f) { //StepUp Enabled
            player.stepHeight = 1.25f;
            forceStepUp = true;
        } else if (autoJumpState == 2 && player.stepHeight >= 1.0f) { //Minecraft Enabled
            player.stepHeight = .6f;
            forceStepUp = true;
        }
        autoJump();
    }

    public static void init() {
    	if(firstRun) {
            if (VersionChecker.isLatestVersion() == false) {
                updateMessage();
            }
            firstRun = false;
    	}
    	ConfigIO.CheckForServerIP();
    	autoJump();
        message();
    }
    
    //@SubscribeEvent
    public static void onKeyInput(KeyInputEvent event) {
    	int autoJumpState = ConfigIO.autoJumpState;

        if (KeyBindings.KEYBINDINGS[0].isPressed()) {	//(event.getKey() == 36) HOME KEY
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
    	int autoJumpState = ConfigIO.autoJumpState;
    	
        boolean b = mc.gameSettings.autoJump;
        if (autoJumpState < AutoJumpState.MINECRAFT.getLevelCode() && b == true) {
            mc.gameSettings.autoJump = false;
        } else if (autoJumpState == AutoJumpState.MINECRAFT.getLevelCode() && b == false) {
            mc.gameSettings.autoJump = true;
        }
    }

    private static void message() {
    	int autoJumpState = ConfigIO.autoJumpState;
    	
        String m = (Object) TextFormatting.DARK_AQUA + "[" + (Object) TextFormatting.YELLOW + StepUp.MOD_NAME + (Object) TextFormatting.DARK_AQUA + "]" + " ";
        if (autoJumpState == AutoJumpState.DISABLED.getLevelCode()) {
            m = m + (Object) TextFormatting.RED + I18n.format(AutoJumpState.DISABLED.getDesc());
        } else if (autoJumpState == AutoJumpState.ENABLED.getLevelCode()) {
            m = m + (Object) TextFormatting.BLUE + I18n.format(AutoJumpState.ENABLED.getDesc());
        } else if (autoJumpState == AutoJumpState.MINECRAFT.getLevelCode()) {
            m = m + (Object) TextFormatting.GREEN + I18n.format(AutoJumpState.MINECRAFT.getDesc());
        }

        player.sendMessage((ITextComponent) new StringTextComponent(m), Util.field_240973_b_);
    }
    
    private static void updateMessage() {
        String m2 = (Object) TextFormatting.GOLD + I18n.format("msg.stepup.updateAvailable") + ": " + (Object) TextFormatting.DARK_AQUA + "[" + (Object) TextFormatting.YELLOW + "StepUp-" + (Object) TextFormatting.WHITE + VersionChecker.getLatestVersion() + (Object) TextFormatting.DARK_AQUA + "]";
        String url = "https://www.curseforge.com/minecraft/mc-mods/stepup/files";
        ClickEvent versionCheckChatClickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
        HoverEvent versionCheckChatHoverEvent = new HoverEvent(HoverEvent.Action.field_230550_a_, new StringTextComponent(I18n.format("msg.stepup.updateTooltip") + "!"));
        TextComponent component = new StringTextComponent(m2);
        Style s = component.getStyle();
        s.func_240715_a_(versionCheckChatClickEvent);	//setClickEvent
        s.func_240716_a_(versionCheckChatHoverEvent);	//setHoverEvent
        component.func_230530_a_(s);
        player.sendMessage((ITextComponent) component, Util.field_240973_b_);
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
