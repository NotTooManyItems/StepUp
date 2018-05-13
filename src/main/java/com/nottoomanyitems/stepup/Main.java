package com.nottoomanyitems.stepup;

import com.nottoomanyitems.stepup.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="stepup", name="StepUp", version="1.0.2-mc1.10-1.12.2", acceptedMinecraftVersions="[1.10,1.12.2]")
public class Main {
    @SidedProxy(clientSide="com.nottoomanyitems.stepup.ClientProxy", serverSide="com.nottoomanyitems.stepup.ServerProxy")
    public static CommonProxy proxy;
    public static final String MODID = "stepup";
    public static final String MODNAME = "StepUp";
    public static final String MODVERSION = "1.0.3";
    public static final String VERSION = MODVERSION+"-mc1.10-1.12.2";
    
    @Mod.Instance
    public static Main instance;
	public static VersionChecker versionChecker;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    static {
        instance = new Main();
    }
}

