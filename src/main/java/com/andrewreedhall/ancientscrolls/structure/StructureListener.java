package com.andrewreedhall.ancientscrolls.structure;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class StructureListener implements Listener {
    public StructureListener() {}

    @EventHandler
    public void onChunkLoad(final ChunkLoadEvent event) {
        if (!event.isNewChunk()) {
            return;
        }
        plugin().getStructureRegistry().getAll().forEach((final AncientScrollsStructure structure) -> structure.generate(event));
    }
}
