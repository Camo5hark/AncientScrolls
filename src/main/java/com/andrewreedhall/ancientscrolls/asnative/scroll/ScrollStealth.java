package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockReceiveGameEvent;

public final class ScrollStealth extends ItemScrollNative implements Listener {
    public ScrollStealth() {
        super("stealth", "Stealth", new String[] {
                "Prevents sculk sensor activation"
        });
        this.special = true;
        this.putMCLootTableGenProb("entities/warden", 1.0);
    }

    @EventHandler
    public void onBlockReceiveGameEvent(final BlockReceiveGameEvent event) {
        if (!event.getBlock().getType().equals(Material.SCULK_SENSOR) || !(event.getEntity() instanceof Player activatingPlayer) || !this.isEquipping(activatingPlayer)) {
            return;
        }
        event.setCancelled(true);
    }
}
