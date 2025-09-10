package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class ScrollNetherLife extends ItemScrollNative implements Listener {
    public ScrollNetherLife() {
        super("nether_life", "Nether Life", new String[] {
                "Night vision in the nether",
                "+20% damage towards and -20% damage from nether monsters"
        });
        this.special = true;
        this.putMCLootTableGenProb("entities/wither", 1.0);
        this.addPotionEffectToEquippingPlayers(NIGHT_VISION_POTION_EFFECT, (final Player equippingPlayer) -> equippingPlayer.getWorld().getEnvironment().equals(World.Environment.NETHER));
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        this.applyBonusAgainstEntityTypes(event, CommonSets.NETHER_MONSTERS, 0.2);
    }
}
