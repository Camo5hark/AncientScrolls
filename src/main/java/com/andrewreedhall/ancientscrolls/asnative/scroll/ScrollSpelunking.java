package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.entity.Player;

public final class ScrollSpelunking extends ItemScrollNative {
    public ScrollSpelunking() {
        super("spelunking", "Spelunking", new String[] {
                "Night vision at Y=30 and below"
        });
        this.putMCLootTableGenProb("chests/abandoned_mineshaft", 0.271);
        this.putMCLootTableGenProb("archaeology/trail_ruins_rare", 0.083);
        this.putMCLootTableGenProb("chests/ancient_city", 0.161);
        this.addPotionEffectToEquippingPlayers(NIGHT_VISION_POTION_EFFECT, (final Player equippingPlayer) -> equippingPlayer.getLocation().getBlockY() <= 30);
    }
}
