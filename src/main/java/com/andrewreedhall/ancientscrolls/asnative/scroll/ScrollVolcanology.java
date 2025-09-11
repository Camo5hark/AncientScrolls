package com.andrewreedhall.ancientscrolls.asnative.scroll;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public final class ScrollVolcanology extends ItemScrollNative implements Listener {
    private static final int MAX_LAVA_DAMAGE_IMMUNITY_TTL = 200;

    private final NamespacedKey pdckLavaDamageImmunityTTL;

    public ScrollVolcanology() {
        super("volcanology", "Volcanology", new String[] {
                "Immunity to lava damage for 10 seconds"
        });
        this.pdckLavaDamageImmunityTTL = this.createSubkey("lava_damage_immunity_ttl");
        this.putMCLootTableGenProb("chests/nether_bridge", 0.111);
        this.putMCLootTableGenProb("entities/magma_cube", 0.005);
        this.scheduleRepeatingTaskPerEquippingPlayer((final Player equippingPlayer) -> {
            final PersistentDataContainer equippingPlayerPDC = equippingPlayer.getPersistentDataContainer();
            if (equippingPlayer.isInLava()) {
                final char[] meter = new char[equippingPlayerPDC.getOrDefault(this.pdckLavaDamageImmunityTTL, PersistentDataType.INTEGER, MAX_LAVA_DAMAGE_IMMUNITY_TTL)];
                Arrays.fill(meter, '|');
                equippingPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + new String(meter)));
            } else {
                equippingPlayerPDC.set(this.pdckLavaDamageImmunityTTL, PersistentDataType.INTEGER, 200);
            }
        }, 1L);
    }

    @EventHandler
    public void onEntityDamageByBlock(final EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer)) {
            return;
        }
        final Block damagingBlock = event.getDamager();
        if (damagingBlock == null || !damagingBlock.getType().equals(Material.LAVA) || !this.isEquipping(damagedPlayer)) {
            return;
        }
        final PersistentDataContainer damagedPlayerPDC = damagedPlayer.getPersistentDataContainer();
        final int damagedPlayerLavaDamageImmunityTTL = damagedPlayerPDC.getOrDefault(this.pdckLavaDamageImmunityTTL, PersistentDataType.INTEGER, MAX_LAVA_DAMAGE_IMMUNITY_TTL);
        if (damagedPlayerLavaDamageImmunityTTL <= 0) {
            return;
        }
        event.setCancelled(true);
        damagedPlayerPDC.set(this.pdckLavaDamageImmunityTTL, PersistentDataType.INTEGER, damagedPlayerLavaDamageImmunityTTL - 1);
    }
}
