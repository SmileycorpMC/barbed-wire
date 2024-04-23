package net.smileycorp.barbedwire.common;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.barbedwire.common.tile.TileBarbedWire;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class BarbedWireContent {
    
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(Constants.name("tab")) {
        
        private final Random rand = new Random();
        private ItemStack stack;
        
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getIconItemStack() {
            if (stack == null || Minecraft.getMinecraft().world.getTotalWorldTime() % 80 == 0) {
                List<BarbedWireMaterial> values = BarbedWireMaterial.values();
                stack = new ItemStack(values.get(rand.nextInt(values.size())).getItem());
            }
            return stack;
        }
    
        @Override
        public ItemStack getTabIconItem() {
            return getIconItemStack();
        }
        
    };
    
    public static Item DIAMOND_NUGGET;
    
    public static final BarbedWireMaterial IRON = new BarbedWireMaterial(ConfigHandler.ironBarbedWire, Ingredient.fromItem(Items.IRON_NUGGET));
    public static final BarbedWireMaterial GOLD = new BarbedWireMaterial(ConfigHandler.goldBarbedWire, Ingredient.fromItem(Items.GOLD_NUGGET));
    public static final BarbedWireMaterial DIAMOND = new BarbedWireMaterial(ConfigHandler.diamondBarbedWire, Ingredient.fromItem(DIAMOND_NUGGET));
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for (BarbedWireMaterial material : BarbedWireMaterial.values()) registry.register(material.getItem());
        DIAMOND_NUGGET = new Item().setUnlocalizedName(Constants.name("DiamondNugget")).setRegistryName("diamond_nugget").setCreativeTab(CREATIVE_TAB);
        registry.register(DIAMOND_NUGGET);
    }
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        for (BarbedWireMaterial material : BarbedWireMaterial.values()) registry.register(material.getBlock());
        GameRegistry.registerTileEntity(TileBarbedWire.class, Constants.loc("barbed_wire"));
    }
    
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        OreDictionary.registerOre("nuggetDiamond", DIAMOND_NUGGET);
    }
    
    
}
