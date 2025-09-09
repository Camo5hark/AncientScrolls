package com.andrewreedhall.ancientscrolls.asnative.flask;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.*;

public final class FlaskLaudanum extends ItemFlaskNative{
    public FlaskLaudanum() {
        super("laudanum", DARK_PURPLE + "Laudanum", new String[] {
                "An ancient pain-relieving supplement"
        }, Color.BLACK, PotionEffectType.REGENERATION, PotionEffectType.SLOWNESS);
        this.putMCLootTableGenProb("chests/stronghold_corridor", 0.329);
        this.putMCLootTableGenProb("chests/stronghold_crossing", 0.343);
        this.putMCLootTableGenProb("chests/trial_chambers/entrance", 0.551);
        this.putMCLootTableGenProb("chests/trial_chambers/intersection", 0.215);
        this.putMCLootTableGenProb("chests/trial_chambers/supply", 0.204);
    }
}
