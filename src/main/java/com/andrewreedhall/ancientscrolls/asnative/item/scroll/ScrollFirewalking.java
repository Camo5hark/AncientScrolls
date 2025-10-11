package com.andrewreedhall.ancientscrolls.asnative.item.scroll;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Set;

public final class ScrollFirewalking extends ItemScrollNative implements Listener {
    private static final Set<Material> BLOCK_TYPES = Set.of(
            Material.MAGMA_BLOCK,
            Material.CAMPFIRE,
            Material.SOUL_CAMPFIRE
    );
    private static final Set<EntityDamageEvent.DamageCause> DAMAGE_CAUSES = Set.of(
            EntityDamageEvent.DamageCause.FIRE,
            EntityDamageEvent.DamageCause.FIRE_TICK
    );

    public ScrollFirewalking() {
        super("firewalking", "Firewalking", new String[] {
                "Negates damage from burning, fire blocks, magma, and campfires"
        });
        this.ominousVaultGenProb = 0.075;
        this.putMCLootTableGenProb("entities/blaze", 0.005);
        this.putMCLootTableGenProb("chests/bastion_treasure", 0.169);
    }

    @EventHandler
    public void onEntityDamageByBlock(final EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer)) {
            return;
        }
        final Block damagingBlock = event.getDamager();
        if (damagingBlock == null || !BLOCK_TYPES.contains(damagingBlock.getType()) || !this.isEquipping(damagedPlayer)) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) ||
                !DAMAGE_CAUSES.contains(event.getCause()) ||
                !this.isEquipping(damagedPlayer)
        ) {
            return;
        }
        event.setCancelled(true);
    }
}
