package com.andrewreedhall.ancientscrolls.item.scroll;

import com.andrewreedhall.ancientscrolls.util.PlayerDataHandler;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ItemScrollListener implements Listener {
    public ItemScrollListener() {}

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        if (itemStack == null || !ItemScroll.is(event.getItem())) {
            return;
        }
        final Player player = event.getPlayer();
        PlayerDataHandler.insertEquippedScroll(player, (ItemScroll) plugin().getItemRegistry().get(ItemScroll.getKey(itemStack)));
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            player.getInventory().remove(itemStack);
        }
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player deadPlayer = event.getPlayer();
        final World deadPlayerWorld = deadPlayer.getWorld();
        final Boolean keepInventory = deadPlayerWorld.getGameRuleValue(GameRule.KEEP_INVENTORY);
        if (keepInventory == null || !keepInventory) {
            return;
        }
        final ItemScroll[] deadPlayerEquippedScrolls = PlayerDataHandler.getEquippedScrolls(deadPlayer);
        PlayerDataHandler.clearEquippedScrolls(deadPlayer);
        final Location deadPlayerLocation = deadPlayer.getLocation();
        for (final ItemScroll deadPlayerEquippedScroll : deadPlayerEquippedScrolls) {
            deadPlayerWorld.dropItemNaturally(deadPlayerLocation, deadPlayerEquippedScroll.createItemStack(1));
        }
    }
}
