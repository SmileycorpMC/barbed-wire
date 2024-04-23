package net.smileycorp.barbedwire.common;

import net.minecraft.init.Enchantments;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.barbedwire.common.damage.DamageSourceBarbedWire;
import net.smileycorp.barbedwire.common.tile.TileBarbedWire;

public class BarbedWireEventHandler {
    
    @SubscribeEvent
    public void getLootingModifier(LootingLevelEvent event) {
        DamageSource cause = event.getDamageSource();
        if (cause instanceof DamageSourceBarbedWire) {
            TileBarbedWire tile = ((DamageSourceBarbedWire) cause).getSource();
            if (tile.hasEnchantment(Enchantments.LOOTING)) {
                event.setLootingLevel(tile.getEnchantmentLevel(Enchantments.LOOTING));
            }
        }
    }
    
    @SubscribeEvent
    public void damageEntity(LivingDamageEvent event) {
        DamageSource cause = event.getSource();
        if (cause instanceof DamageSourceBarbedWire && event.getAmount() > 0) {
            TileBarbedWire tile = ((DamageSourceBarbedWire) cause).getSource();
            if (tile.hasEnchantment(Enchantments.FIRE_ASPECT)) event.getEntity().setFire(tile.getEnchantmentLevel(Enchantments.FIRE_ASPECT) * 4);
        }
    }

}
