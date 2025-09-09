package com.andrewreedhall.ancientscrolls.asnative.flask;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.*;

public final class FlaskDragonsBlood extends ItemFlaskNative{
    public FlaskDragonsBlood() {
        super("dragons_blood", GOLD + "Dragon's Blood", new String[] {
                "Ancient dragons were born in fire"
        }, Color.RED, PotionEffectType.FIRE_RESISTANCE, PotionEffectType.BLINDNESS);
        this.putMCLootTableGenProb("chests/ruined_portal", 0.205);
        this.putMCLootTableGenProb("chests/nether_bridge", 0.179);
        this.putMCLootTableGenProb("chests/bastion_other", 0.244);
    }
}
