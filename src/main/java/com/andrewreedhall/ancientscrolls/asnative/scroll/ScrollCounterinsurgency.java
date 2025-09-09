package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class ScrollCounterinsurgency extends ItemScrollNative implements Listener {
    public ScrollCounterinsurgency() {
        super("counterinsurgency", "Counterinsurgency", new String[] {
                "+15% damage towards and -15% damage from illagers"
        });
        this.putMCLootTableGenProb("chests/pillager_outpost", 0.25);
        this.putMCLootTableGenProb("chests/woodland_mansion", 0.283);
        this.putMCLootTableGenProb("chests/village/village_armorer", 0.1);
        this.putMCLootTableGenProb("chests/village/village_weaponsmith", 0.056);
        this.putMCLootTableGenProb("entities/evoker", 0.01);
        this.putMCLootTableGenProb("entities/pillager", 0.005);
        this.putMCLootTableGenProb("entities/vindicator", 0.005);
        this.putMCLootTableGenProb("entities/vex", 0.01);
        this.putMCLootTableGenProb("entities/witch", 0.005);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        this.applyBonusAgainstEntityTypes(event, CommonSets.ILLAGERS, 0.15);
    }
}
