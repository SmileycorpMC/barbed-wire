package net.smileycorp.barbedwire.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.smileycorp.barbedwire.common.BarbedWireMaterial;
import net.smileycorp.barbedwire.common.Constants;

import java.util.List;

public class ItemBarbedWire extends ItemBlock {

    private static final String TOOLTIP = "tooltip." + Constants.MODID + ".BarbedWire";
    private final BarbedWireMaterial material;
    
    public ItemBarbedWire(BarbedWireMaterial material) {
        super(material.getBlock());
        String name = material.getUnlocalisedName() + "_Barbed_Wire";
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        this.material = material;
        setMaxDamage(material.getDurability());
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(I18n.translateToLocal(TOOLTIP));
        tooltip.add(I18n.translateToLocal(TOOLTIP + "." + material.getUnlocalisedName()));
        if (stack.getItemDamage() > 0) tooltip.add(I18n.translateToLocal(TOOLTIP + ".durability")
                + " " + stack.getItemDamage() + "/" + material.getDurability());
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return material.getEnchantability();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.type.canEnchantItem(Items.IRON_SWORD);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getItemDamage() > 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (!showDurabilityBar(stack)) return 0;
        return 1 - ((double) stack.getItemDamage() / (double) material.getDurability());
    }


}
