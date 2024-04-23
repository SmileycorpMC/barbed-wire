package net.smileycorp.barbedwire.integration;

import com.Fishmod.mod_LavaCow.init.ModEnchantments;
import com.Fishmod.mod_LavaCow.init.ModMobEffects;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.barbedwire.common.damage.DamageSourceBarbedWire;
import net.smileycorp.barbedwire.common.tile.TileBarbedWire;

public class FishsUndeadIntegration {
    
    @SubscribeEvent
    public void damageEntity(LivingDamageEvent event) {
        DamageSource cause = event.getSource();
        if (cause instanceof DamageSourceBarbedWire && event.getAmount() > 0) {
            TileBarbedWire tile = ((DamageSourceBarbedWire) cause).getSource();
            if (tile.hasEnchantment(Enchantments.FIRE_ASPECT)) event.getEntity().setFire(tile.getEnchantmentLevel(Enchantments.FIRE_ASPECT) * 4);
            if (tile.hasEnchantment(ModEnchantments.POISONOUS)) event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.POISON, 160,
                        tile.getEnchantmentLevel(ModEnchantments.POISONOUS) - 1));
            if (tile.hasEnchantment(ModEnchantments.CORROSIVE)) event.getEntityLiving().addPotionEffect(new PotionEffect(ModMobEffects.CORRODED, 80,
                        tile.getEnchantmentLevel(ModEnchantments.CORROSIVE) - 1));
        }
    }
    
}
