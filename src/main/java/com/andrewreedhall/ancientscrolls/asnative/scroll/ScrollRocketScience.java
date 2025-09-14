package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollRocketScience extends ItemScrollNative implements Listener {
    public ScrollRocketScience() {
        super("rocket_science", "Rocket Science", new String[] {
                "75% chance to not consume elytra rocket"
        });
        this.special = true;
        this.putMCLootTableGenProb("chests/end_city_treasure", 0.131);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final ItemStack interactingItemStack = event.getItem();
        if (interactingItemStack == null || !interactingItemStack.getType().equals(Material.FIREWORK_ROCKET)) {
            return;
        }
        final Player interactingPlayer = event.getPlayer();
        if (!interactingPlayer.isGliding() || interactingPlayer.getGameMode().equals(GameMode.CREATIVE) || !this.isEquipping(interactingPlayer) || plugin().getUniversalRandom().nextDouble() < 0.75) {
            return;
        }
        interactingItemStack.add();
    }
}
