package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.entity.Player;

public final class ScrollAquaticLensing extends ItemScrollNative {
    public ScrollAquaticLensing() {
        super("aquatic_lensing", "Aquatic Lensing", new String[] {
                "Night vision in aquatic biomes"
        });
        this.putMCLootTableGenProb("chests/shipwreck_treasure", 0.167);
        this.putMCLootTableGenProb("chests/buried_treasure", 0.25);
        this.putMCLootTableGenProb("entities/drowned", 0.01);
        this.addPotionEffectToEquippingPlayers(NIGHT_VISION_POTION_EFFECT, (final Player equippingPlayer) -> CommonSets.AQUATIC_BIOMES.contains(equippingPlayer.getLocation().getBlock().getBiome()));
    }
}
