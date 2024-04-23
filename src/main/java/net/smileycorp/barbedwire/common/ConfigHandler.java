package net.smileycorp.barbedwire.common;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    public static BarbedWireConfigEntry ironBarbedWire;
    public static BarbedWireConfigEntry goldBarbedWire;
    public static BarbedWireConfigEntry diamondBarbedWire;
    
    //load config properties
    public static void syncConfig(Configuration config) {
        try {
            config.load();
            ironBarbedWire = new BarbedWireConfigEntry(config, "Iron", 0.5f, 300, 14, 1);
            goldBarbedWire = new BarbedWireConfigEntry(config, "Gold", 3f, 100, 22, 2);
            diamondBarbedWire = new BarbedWireConfigEntry(config, "Diamond", 1f, 900, 10, 2);
        } catch (Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }

    }

}
