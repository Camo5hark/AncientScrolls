package com.andrewreedhall.ancientscrolls.asnative.flask;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.*;

public final class FlaskAmbrosia extends ItemFlaskNative {
    public FlaskAmbrosia() {
        super("ambrosia", LIGHT_PURPLE + "Ambrosia", new String[] {
                "Nectar of the Gods"
        }, Color.YELLOW, PotionEffectType.STRENGTH, PotionEffectType.SLOW_FALLING);
        this.putMCLootTableGenProb("chests/jungle_temple", 0.333);
        this.putMCLootTableGenProb("chests/desert_pyramid", 0.333);
    }
}
