package com.andrewreedhall.ancientscrolls.asnative.flask;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.*;

public final class FlaskFermentedCarrotJuice extends ItemFlaskNative {
    public FlaskFermentedCarrotJuice() {
        super("fermented_carrot_juice", GREEN + "Fermented Carrot Juice", new String[] {
                "Carrots are good for your eyesight"
        }, Color.ORANGE, PotionEffectType.NIGHT_VISION, PotionEffectType.NAUSEA);
        this.putMCLootTableGenProb("chests/village/village_desert_house", 0.266);
        this.putMCLootTableGenProb("chests/village/village_plains_house", 0.266);
        this.putMCLootTableGenProb("chests/village/village_savanna_house", 0.266);
        this.putMCLootTableGenProb("chests/village/village_snowy_house", 0.266);
        this.putMCLootTableGenProb("chests/village/village_taiga_house", 0.266);
        this.putMCLootTableGenProb("chests/abandoned_mineshaft", 0.423);
    }
}
