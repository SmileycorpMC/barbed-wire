package net.smileycorp.barbedwire.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.barbedwire.common.BarbedWireContent;
import net.smileycorp.barbedwire.common.BarbedWireMaterial;
import net.smileycorp.barbedwire.common.Constants;
import net.smileycorp.barbedwire.common.tile.TileBarbedWire;

@EventBusSubscriber(value = Side.CLIENT, modid = Constants.MODID)
public class ClientHandler {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        //register entity renderers
        ModelLoader.setCustomModelResourceLocation(BarbedWireContent.DIAMOND_NUGGET, 0,
                new ModelResourceLocation(BarbedWireContent.DIAMOND_NUGGET.getRegistryName().toString()));
        for (BarbedWireMaterial material : BarbedWireMaterial.values())
            ModelLoader.setCustomModelResourceLocation(material.getItem(), 0,
                    new ModelResourceLocation(material.getItem().getRegistryName().toString()));
        //register renderer for barbed wire healthbar
        ClientRegistry.bindTileEntitySpecialRenderer(TileBarbedWire.class, new TESRBarbedWire());
    }
    
}
