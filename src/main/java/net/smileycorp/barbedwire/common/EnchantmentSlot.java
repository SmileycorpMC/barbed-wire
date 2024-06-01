package net.smileycorp.barbedwire.common;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.smileycorp.barbedwire.common.item.ItemBarbedWire;

public class EnchantmentSlot extends Slot {
    
    public EnchantmentSlot(IInventory inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack) {
        return stack.getItem() instanceof ItemBarbedWire ? 64 : getSlotStackLimit();
    }
    
}
