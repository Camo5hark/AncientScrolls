package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Set;

public final class ScrollLandscaping extends ItemScrollNative implements Listener {
    private static final Set<Material> BLOCK_TYPES = Set.of(
            Material.DIRT,
            Material.SAND,
            Material.MUD,
            Material.SNOW
    );

    public ScrollLandscaping() {
        super("landscaping", "Landscaping", new String[] {
                "20% chance to not consume the following blocks when placed:",
                "- Dirt",
                "- Sand",
                "- Mud",
                "- Snow"
        });
        this.putMCLootTableGenProb("chests/village/village_desert_house", 0.333);
        this.putMCLootTableGenProb("chests/village/village_plains_house", 0.333);
        this.putMCLootTableGenProb("chests/village/village_savanna_house", 0.333);
        this.putMCLootTableGenProb("chests/village/village_snowy_house", 0.333);
        this.putMCLootTableGenProb("chests/village/village_taiga_house", 0.333);
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        this.reduceConsumedPlacedBlocks(event, BLOCK_TYPES, 0.2);
    }
}
