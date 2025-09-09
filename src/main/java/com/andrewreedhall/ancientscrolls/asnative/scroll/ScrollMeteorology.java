package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.entity.Player;

public final class ScrollMeteorology extends ItemScrollNative {
    public ScrollMeteorology() {
        super("meteorology", "Meteorology", new String[] {
                "Night vision in adverse weather"
        });
        this.putMCLootTableGenProb("chests/ruined_portal", 0.073);
        this.putMCLootTableGenProb("chests/jungle_temple", 0.127);
        this.putMCLootTableGenProb("entities/bogged", 0.005);
        this.addNightVisionEffectToEquippingPlayers((final Player equippingPlayer) -> equippingPlayer.getWorld().hasStorm());
    }
}
