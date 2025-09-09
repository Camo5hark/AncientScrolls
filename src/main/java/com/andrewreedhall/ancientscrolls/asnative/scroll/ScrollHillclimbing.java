package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class ScrollHillclimbing extends ItemScrollNative {
    public ScrollHillclimbing() {
        super("hillclimbing", "Hillclimbing", new String[] {
                "Jump boost in highland biomes"
        });
        this.putMCLootTableGenProb("chests/ruined_portal", 0.205);
        this.putMCLootTableGenProb("entities/spider", 0.005);
        this.putMCLootTableGenProb("entities/cave_spider", 0.01);
        this.addPotionEffectToEquippingPlayers(
                new PotionEffect(PotionEffectType.JUMP_BOOST, 25, 2, false),
                (final Player equippingPlayer) -> CommonSets.HIGHLAND_BIOMES.contains(equippingPlayer.getLocation().getBlock().getBiome())
        );
    }
}
