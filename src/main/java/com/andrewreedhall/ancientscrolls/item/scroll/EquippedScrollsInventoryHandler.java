package com.andrewreedhall.ancientscrolls.item.scroll;

import com.andrewreedhall.ancientscrolls.util.PlayerDataHandler;
import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;
import static org.bukkit.ChatColor.*;

public final class EquippedScrollsInventoryHandler implements Listener, Runnable {
    private final Map<Player, Inventory> equippedScrollInventories = new HashMap<>();

    public EquippedScrollsInventoryHandler() {}

    @Override
    public void run() {
        this.equippedScrollInventories.forEach(
                (final Player player, final Inventory equippedScrollInventory) ->
                        equippedScrollInventory.setContents(
                                Arrays.stream(PlayerDataHandler.getEquippedScrolls(player))
                                        .map((final ItemScroll equippedScroll) ->
                                                equippedScroll == null ? new ItemStack(Material.AIR) : equippedScroll.createItemStack(1)
                                        )
                                        .toArray(ItemStack[]::new)
                        )
        );
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final int maxEquippedScrolls = plugin().getDefaultCachedConfig().item_scroll_maxEquippedScrolls;
        final int equippedScrollInventorySize = ((maxEquippedScrolls / 9) + (maxEquippedScrolls % 9 == 0 ? 0 : 1)) * 9;
        this.equippedScrollInventories.put(player, plugin().getServer().createInventory(player, equippedScrollInventorySize, "Ancient Knowledge"));
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.equippedScrollInventories.remove(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null || !(event.getWhoClicked() instanceof Player clickerPlayer)) {
            return;
        }
        final Inventory equippedScrollInventory = event.getView().getTopInventory();
        if (!this.equippedScrollInventories.containsValue(equippedScrollInventory)) {
            return;
        }
        final ItemStack scrollItemStack = event.getCurrentItem();
        if (scrollItemStack == null || !ItemScroll.is(scrollItemStack)) {
            event.setCancelled(true);
            clickerPlayer.sendMessage(RED + "You can only interact with scrolls when viewing the Ancient Knowledge inventory");
            BukkitUtil.playBadSound(clickerPlayer);
            return;
        }
        final Inventory clickerInventory = clickerPlayer.getInventory();
        final int clickedSlot = event.getSlot();
        final Player equippedScrollPlayer = (Player) equippedScrollInventory.getHolder();
        if (clickedInventory.equals(equippedScrollInventory)) {
            equippedScrollInventory.setItem(clickedSlot, null);
            PlayerDataHandler.removeEquippedScroll(equippedScrollPlayer, clickedSlot);
            final HashMap<Integer, ItemStack> notGivenItemStacks = clickerInventory.addItem(scrollItemStack);
            if (!notGivenItemStacks.isEmpty()) {
                clickerPlayer.getWorld().dropItem(clickerPlayer.getLocation(), notGivenItemStacks.get(0));
            }
        } else if (clickedInventory.equals(clickerInventory) && PlayerDataHandler.insertEquippedScroll(equippedScrollPlayer, (ItemScroll) plugin().getItemRegistry().get(ItemScroll.getKey(scrollItemStack)))) {
            clickerInventory.setItem(clickedSlot, null);
        }
    }

    public void open(final Player openingPlayer, final Player equippedScrollsPlayer) {
        final Inventory equippedScrollInventory = this.equippedScrollInventories.get(equippedScrollsPlayer);
        if (equippedScrollInventory == null) {
            plugin().getLogger().warning(equippedScrollsPlayer.getName() + " does not have an equipped scrolls inventory");
            return;
        }
        openingPlayer.openInventory(equippedScrollInventory);
        openingPlayer.playSound(openingPlayer, Sound.BLOCK_ENDER_CHEST_OPEN, 1.0F, 1.0F);
    }
}
