package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class ScrollNightLife extends ItemScrollNative implements Listener {
    public ScrollNightLife() {
        super("nightlife", "Nightlife", new String[] {
                "Night vision at night",
                "+20% damage towards and -20% damage from overworld monsters"
        });
        this.putMCLootTableGenProb("chests/end_city_treasure", 0.131);
        this.addPotionEffectToEquippingPlayers(NIGHT_VISION_POTION_EFFECT, (final Player equippingPlayer) -> !equippingPlayer.getWorld().isDayTime());
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        this.applyBonusAgainstEntityTypes(event, CommonSets.OVERWORLD_MONSTERS, 0.2);
    }
}
