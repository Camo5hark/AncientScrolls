package com.andrewreedhall.ancientscrolls.structure;

import com.andrewreedhall.ancientscrolls.AncientScrollsRegistry;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.event.world.ChunkLoadEvent;

public abstract class AncientScrollsStructure extends AncientScrollsRegistry.Value {
    public AncientScrollsStructure(final NamespacedKey key) {
        super(key);
    }

    protected abstract boolean canGenerate(final Chunk chunk);

    public void generate(final ChunkLoadEvent event) {
        final Chunk chunk = event.getChunk();
        if (!this.canGenerate(chunk)) {
            return;
        }
        // generate
    }
}
