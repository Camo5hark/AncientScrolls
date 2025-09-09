package com.andrewreedhall.ancientscrolls.npc;

import com.andrewreedhall.ancientscrolls.AncientScrollsRegistry;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public abstract class AncientScrollsNPC extends AncientScrollsRegistry.Value {
    public AncientScrollsNPC(NamespacedKey key) {
        super(key);
    }

    public abstract NPCInstance spawn(Location location);
}
