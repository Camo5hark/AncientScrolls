package com.andrewreedhall.ancientscrolls.asnative.flask;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.*;

public final class FlaskUnstableConcoction extends ItemFlaskNative {
    public FlaskUnstableConcoction() {
        super("unstable_concoction", AQUA + "Unstable Concoction", new String[] {
                "Boost of energy at a cost"
        }, Color.FUCHSIA, PotionEffectType.SPEED, PotionEffectType.WEAKNESS);
        this.putMCLootTableGenProb("chests/stronghold_corridor", 0.329);
        this.putMCLootTableGenProb("chests/stronghold_crossing", 0.343);
        this.putMCLootTableGenProb("chests/trial_chambers/entrance", 0.551);
        this.putMCLootTableGenProb("chests/trial_chambers/intersection", 0.215);
        this.putMCLootTableGenProb("chests/trial_chambers/supply", 0.204);
        this.putMCLootTableGenProb("chests/nether_bridge", 0.179);
        this.putMCLootTableGenProb("chests/bastion_other", 0.19);
    }
}
