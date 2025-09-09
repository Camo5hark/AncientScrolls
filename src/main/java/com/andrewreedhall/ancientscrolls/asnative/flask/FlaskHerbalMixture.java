package com.andrewreedhall.ancientscrolls.asnative.flask;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.*;

public final class FlaskHerbalMixture extends ItemFlaskNative {
    public FlaskHerbalMixture() {
        super("herbal_mixture", GREEN + "Herbal Mixture", new String[] {
                "Composed of spring flowers and insects"
        }, Color.GREEN, PotionEffectType.JUMP_BOOST, PotionEffectType.INFESTED);
        this.putMCLootTableGenProb("chests/village/village_plains_house", 0.266);
        this.putMCLootTableGenProb("chests/village/village_savanna_house", 0.266);
        this.putMCLootTableGenProb("chests/jungle_temple", 0.333);
    }
}
