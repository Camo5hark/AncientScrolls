package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class ScrollHymnal extends ItemScrollNative implements Listener {
    public ScrollHymnal() {
        super("hymnal", "Hymnal", new String[] {
                "+15% damage towards and -15% damage from undead monsters"
        });
        this.putMCLootTableGenProb("entities/bogged", 0.005);
        this.putMCLootTableGenProb("entities/drowned", 0.005);
        this.putMCLootTableGenProb("entities/husk", 0.005);
        this.putMCLootTableGenProb("entities/phantom", 0.005);
        this.putMCLootTableGenProb("entities/skeleton", 0.005);
        this.putMCLootTableGenProb("entities/skeleton_horse", 0.005);
        this.putMCLootTableGenProb("entities/stray", 0.005);
        this.putMCLootTableGenProb("entities/wither_skeleton", 0.005);
        this.putMCLootTableGenProb("entities/zoglin", 0.005);
        this.putMCLootTableGenProb("entities/zombie", 0.005);
        this.putMCLootTableGenProb("entities/zombie_horse", 0.005);
        this.putMCLootTableGenProb("entities/zombie_villager", 0.005);
        this.putMCLootTableGenProb("entities/zombified_piglin", 0.005);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        this.applyBonusAgainstEntityTypes(event, CommonSets.UNDEAD_MONSTERS, 0.15);
    }
}
