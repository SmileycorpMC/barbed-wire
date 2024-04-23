package net.smileycorp.barbedwire.common;

import net.minecraftforge.common.config.Configuration;

public class BarbedWireConfigEntry {
    
    private final String name;
    private final float damage;
    private final int durability;
    private final int enchantability;
    private final int harvestLevel;
    
    public BarbedWireConfigEntry(Configuration config, String name, float damage, int durability, int enchantability, int harvestLevel) {
        this.name = name;
        this.damage = (float) config.get(name, "damage", damage, "Damage dealt by " + name + " barbed wire each time it triggers.").getDouble();
        this.durability = config.get(name, "durability", durability, "How many times can " + name + " barbed wire deal damage before breaking?").getInt();
        this.enchantability = config.get(name, "enchantability", enchantability, "How enchantable is " + name + " barbed wire?").getInt();
        this.harvestLevel = config.get(name, "harvestLevel", enchantability, "What harvest level do you you need to mine " + name + " barbed wire? (0 is wood, 1 is stone, 2 is iron, 3 is diamond)").getInt();
    }
    
    public String getName() {
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
    
}
