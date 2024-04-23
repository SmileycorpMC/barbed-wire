package net.smileycorp.barbedwire.common.damage;

import net.minecraft.util.DamageSource;
import net.smileycorp.barbedwire.common.tile.TileBarbedWire;

public class DamageSourceBarbedWire extends DamageSource {

    private final TileBarbedWire source;

    public DamageSourceBarbedWire(TileBarbedWire source) {
        super("BarbedWire");
        this.source = source;
    }

    public TileBarbedWire getSource() {
        return source;
    }

}
