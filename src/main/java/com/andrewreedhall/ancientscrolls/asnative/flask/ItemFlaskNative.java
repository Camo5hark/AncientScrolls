package com.andrewreedhall.ancientscrolls.asnative.flask;

import com.andrewreedhall.ancientscrolls.item.flask.ItemFlask;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffectType;

public abstract class ItemFlaskNative extends ItemFlask {
    public ItemFlaskNative(final String id, final String displayName, final String[] lore, final Color color, final PotionEffectType buffPotionEffectType, final PotionEffectType debuffPotionEffectType) {
        super(fromAncientScrollsNamespace(id), displayName, lore, color, buffPotionEffectType, debuffPotionEffectType);
    }
}
