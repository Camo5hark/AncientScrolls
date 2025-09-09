package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class ScrollHellsBlessing extends ItemScrollNative implements Listener {
    public ScrollHellsBlessing() {
        super("hells_blessing", "Hell's Blessing", new String[] {
                "+15% damage towards and -15% damage from nether monsters"
        });
        this.putMCLootTableGenProb("entities/blaze", 0.005);
        this.putMCLootTableGenProb("entities/ghast", 0.005);
        this.putMCLootTableGenProb("entities/hoglin", 0.005);
        this.putMCLootTableGenProb("entities/magma_cube", 0.005);
        this.putMCLootTableGenProb("entities/piglin", 0.005);
        this.putMCLootTableGenProb("entities/piglin_brute", 0.005);
        this.putMCLootTableGenProb("entities/skeleton", 0.005);
        this.putMCLootTableGenProb("entities/wither_skeleton", 0.005);
        this.putMCLootTableGenProb("entities/zoglin", 0.005);
        this.putMCLootTableGenProb("entities/zombified_piglin", 0.005);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        this.applyBonusAgainstEntityTypes(event, CommonSets.NETHER_MONSTERS, 0.15);
    }
}
