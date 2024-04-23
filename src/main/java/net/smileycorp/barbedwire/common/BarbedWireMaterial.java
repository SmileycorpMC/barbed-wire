package net.smileycorp.barbedwire.common;

import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.smileycorp.barbedwire.common.block.BlockBarbedWire;
import net.smileycorp.barbedwire.common.item.ItemBarbedWire;
import net.smileycorp.barbedwire.common.tile.TileBarbedWire;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BarbedWireMaterial implements IStringSerializable {
    
    private static final LinkedHashMap<String, BarbedWireMaterial> VALUES = Maps.newLinkedHashMap();

    private final String name;
    private final float damage;
    private final int durability;
    private final int enchantability;
    private final int harvestLevel;
    private final Ingredient drop;
    private final BlockBarbedWire block;
    private final ItemBarbedWire item;
    private final Function<BarbedWireMaterial, TileBarbedWire> te;
    
    public BarbedWireMaterial(String name, float damage, int durability, int enchantability, int harvestLevel, Ingredient drop, Function<BarbedWireMaterial, TileBarbedWire> te) {
        this.name = name;
        this.damage = damage;
        this.durability = durability;
        this.enchantability = enchantability;
        this.harvestLevel = harvestLevel;
        this.drop = drop;
        this.te = te;
        VALUES.put(name, this);
        block = new BlockBarbedWire(this);
        item = new ItemBarbedWire(this);
    }
    
    public BarbedWireMaterial(BarbedWireConfigEntry config, Ingredient drop, Function<BarbedWireMaterial, TileBarbedWire> te) {
        this(config.getName(), config.getDamage(), config.getDurability(), config.getEnchantability(), config.getHarvestLevel(), drop, te);
    }
    
    public BarbedWireMaterial(BarbedWireConfigEntry config, Ingredient drop) {
        this(config, drop, TileBarbedWire::new);
    }
    
    @Override
    public String getName() {
        return name.toLowerCase();
    }

    public String getUnlocalisedName() {
        return name;
    }

    public float getDamage() {
        return damage;
    }

    public int getDurability() {
        return durability;
    }

    public int getEnchantability() {
        return enchantability;
    }
    
    public int getHarvestLevel() {
        return harvestLevel;
    }
    
    public ItemStack getDrop() {
        return drop.getMatchingStacks()[0].copy();
    }
    
    public static BarbedWireMaterial getMaterial(String name) {
        return VALUES.get(name);
    }
    
    public static List<BarbedWireMaterial> values() {
        return VALUES.values().stream().collect(Collectors.toList());
    }
    
    public BlockBarbedWire getBlock() {
        return block;
    }
    
    public ItemBarbedWire getItem() {
        return item;
    }
    
    public TileEntity createTileEntity() {
        return te.apply(this);
    }
    
}