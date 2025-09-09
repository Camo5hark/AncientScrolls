package com.andrewreedhall.ancientscrolls.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.event.world.LootGenerateEvent;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ItemListener implements Listener {
    public ItemListener() {}

    @EventHandler
    public void onLootGenerate(final LootGenerateEvent event) {
        // TODO debugging statement
        plugin().getLogger().info("Loot generated: " + event.getLootTable().getKey());
        // TODO -------------------
        plugin().getItemRegistry().getAll().forEach((final AncientScrollsItem item) -> item.generateByLootTable(event));
    }

    @EventHandler
    public void onBlockDispenseLoot(final BlockDispenseLootEvent event) {
        // TODO debugging statement
        plugin().getLogger().info("Block dispense: " + event.getBlock());
        // TODO -------------------
        plugin().getItemRegistry().getAll().forEach((final AncientScrollsItem item) -> item.generateByVault(event));
    }
}
