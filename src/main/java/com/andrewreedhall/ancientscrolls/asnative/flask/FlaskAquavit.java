package com.andrewreedhall.ancientscrolls.asnative.flask;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.*;

public final class FlaskAquavit extends ItemFlaskNative {
    public FlaskAquavit() {
        super("aquavit", BLUE + "Aquavit", new String[] {
                "Contains the spirit of the sea"
        }, Color.AQUA, PotionEffectType.DOLPHINS_GRACE, PotionEffectType.MINING_FATIGUE);
        this.putMCLootTableGenProb("chests/buried_treasure", 0.25);
        this.putMCLootTableGenProb("chests/shipwreck_supply", 0.167);
        this.putMCLootTableGenProb("chests/underwater_ruin_big", 0.217);
    }
}
