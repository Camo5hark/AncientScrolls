package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class ScrollWhalelore extends ItemScrollNative {
    public ScrollWhalelore() {
        super("whalelore", "Whalelore", new String[] {
                "Dolphin's grace when swimming"
        });
        this.special = true;
        this.putMCLootTableGenProb("entities/elder_guardian", 1.0);
        this.addPotionEffectToEquippingPlayers(
                new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 25, 1, false),
                Player::isSwimming
        );
    }
}
