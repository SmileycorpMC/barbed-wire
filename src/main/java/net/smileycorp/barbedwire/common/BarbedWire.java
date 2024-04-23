package net.smileycorp.barbedwire.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.smileycorp.barbedwire.integration.FishsUndeadIntegration;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.NAME)
public class BarbedWire {

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.syncConfig(new Configuration(event.getSuggestedConfigurationFile()));
        MinecraftForge.EVENT_BUS.register(new BarbedWireEventHandler());
        if (Loader.isModLoaded("mod_lavacow")) MinecraftForge.EVENT_BUS.register(new FishsUndeadIntegration());
    }
    
}
