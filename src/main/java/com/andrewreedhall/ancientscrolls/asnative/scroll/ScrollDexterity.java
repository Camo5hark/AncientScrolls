package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.PlayerInventory;

public final class ScrollDexterity extends ItemScrollNative implements Listener {
    public ScrollDexterity() {
        super("dexterity", "Dexterity", new String[] {
                "Pickup items within 5 blocks instantly"
        });
        this.vaultGenProb = 0.085;
        this.putMCLootTableGenProb("entities/enderman", 0.01);
        this.putMCLootTableGenProb("entities/creaking", 0.01);
        this.scheduleRepeatingTaskPerEquippingPlayer((final Player equippingPlayer) -> {
            final World equippingPlayerWorld = equippingPlayer.getWorld();
            final Location equippingPlayerLocation = equippingPlayer.getLocation();
            final PlayerInventory equippingPlayerInventory = equippingPlayer.getInventory();
            equippingPlayerWorld
                    .getNearbyEntitiesByType(Item.class, equippingPlayerLocation, 5.0, 1.0, 5.0)
                    .forEach((final Item nearbyItem) -> {
                        if (nearbyItem.getLocation().distanceSquared(equippingPlayerLocation) > 25.0 ||
                                !equippingPlayerInventory.addItem(nearbyItem.getItemStack()).isEmpty()
                        ) {
                            return;
                        }
                        nearbyItem.remove();
                        equippingPlayerWorld.playSound(equippingPlayer, Sound.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
                    });
        }, 60L);
    }

    @EventHandler
    public void onEntityPickupItem(final EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player pickingUpPlayer) || !this.isEquipping(pickingUpPlayer)) {
            return;
        }
        event.setCancelled(true);
    }
}
