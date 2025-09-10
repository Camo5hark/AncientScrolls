package com.andrewreedhall.ancientscrolls.item.scroll;

import com.andrewreedhall.ancientscrolls.item.AncientScrollsItem;
import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import com.andrewreedhall.ancientscrolls.util.PlayerDataHandler;
import org.bukkit.*;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        if (!(event.getEntity() instanceof EnderDragon deadEnderDragon)) {
            return;
        }
        final World deadEnderDragonWorld = deadEnderDragon.getWorld();
        if (!deadEnderDragonWorld.getEnvironment().equals(World.Environment.THE_END)) {
            return;
        }
        deadEnderDragonWorld.getPlayers().forEach((final Player player) -> {
            final PlayerInventory playerInventory = player.getInventory();
            plugin().getItemRegistry().getAll().forEach((final AncientScrollsItem item) -> {
                if (!(item instanceof ItemScroll scroll) || !scroll.isEnderDragonReward()) {
                    return;
                }
                BukkitUtil.addItem(playerInventory, scroll.createItemStack(1));
            });
        });
    }
}
