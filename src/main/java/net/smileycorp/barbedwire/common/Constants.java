package net.smileycorp.barbedwire.common;

import net.minecraft.util.ResourceLocation;

public class Constants {

    //mod constants
    public static final String MODID = "barbedwire";
    public static final String NAME = "Barbed Wire";
    public static final String VERSION = "1.1.0";
    
    //helper methods
    public static String name(String name) {
        return MODID + "." + name.replace("_", "");
    }

    public static ResourceLocation loc(String name) {
        return new ResourceLocation(MODID, name.toLowerCase());
    }
    
}
