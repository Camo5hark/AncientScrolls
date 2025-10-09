/*
AncientScrolls SpigotMC plugin
Copyright (C) 2025  Andrew Hall

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.

License file: <project-root>/COPYING
GitHub repo URL: www.github.com/Camo5hark/AncientScrolls
 */

package com.andrewreedhall.ancientscrolls.item;

import com.andrewreedhall.ancientscrolls.AncientScrollsResource;
import com.andrewreedhall.ancientscrolls.util.Entropic;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Vault;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Represents a scroll item with configurable loot generation logic.
 */
public abstract class AncientScrollsItem extends AncientScrollsResource implements Entropic {
    /**
     * Per-loot-table generation probabilities (by loot table key).
     */
    protected final Map<NamespacedKey, Double> lootTableGenProbs = new HashMap<>();
    /**
     * Generation probability when found in a dungeon chest.
     */
    protected Double dungeonChestGenProb = null;
    /**
     * Generation probability when found in a standard vault.
     */
    protected Double vaultGenProb = null;
    /**
     * Generation probability when found in an ominous vault.
     */
    protected Double ominousVaultGenProb = null;
    private final long entropy;

    /**
     * Constructs a scroll item with the given key and initializes entropy.
     * @param key the unique item key
     */
    public AncientScrollsItem(final NamespacedKey key) {
        super(key);
        this.entropy = this.generateEntropy();
    }

    /**
     * Creates an {@link ItemStack} representing this item.
     * @param amount the quantity
     * @return the created item stack
     */
    public abstract ItemStack createItemStack(int amount);

    @Override
    protected File getConfigFile() {
        return new File(new File(plugin().getDataFolder(), "item"), this.key.getKey() + ".yml");
    }

    @Override
    protected Map<String, Object> getConfigDefaults() {
        final Map<String, Object> configDefaults = new HashMap<>();
        configDefaults.put("test", 1);
        return configDefaults;
    }

    /**
     * Adds a loot table generation probability for a vanilla loot table.
     * @param id the Minecraft loot table ID (e.g. "chests/abandoned_mineshaft")
     * @param prob the generation probability
     */
    protected void putMCLootTableGenProb(final String id, final double prob) {
        this.lootTableGenProbs.put(NamespacedKey.minecraft(id), prob);
    }

    void generateByLootTable(final LootGenerateEvent event) {
        final Double lootTableGenProb = this.lootTableGenProbs.get(event.getLootTable().getKey());
        if (lootTableGenProb == null) {
            return;
        }
        Random random;
        Inventory inventory = null;
        if (event.getInventoryHolder() instanceof final BlockInventoryHolder blockInventoryHolder) {
            final Block block = blockInventoryHolder.getBlock();
            random = this.generateRandom(this.entropy, block.getWorld().getSeed(), block.getX(), block.getY(), block.getZ());
            inventory = blockInventoryHolder.getInventory();
        } else {
            random = plugin().getUniversalRandom();
        }
        if (random.nextDouble() > lootTableGenProb * plugin().item_generation_probabilityScalar) {
            return;
        }
        final List<ItemStack> generatedLoot = event.getLoot();
        if (inventory != null) {
            while (generatedLoot.size() >= inventory.getSize()) {
                generatedLoot.removeFirst();
            }
        }
        event.getLoot().add(this.createItemStack(1));
    }

    void generateByDungeonChest() {
    }

    void generateByVault(final Block vault, final double genProb, final BlockDispenseLootEvent event) {
        final Random random = this.generateRandom(this.entropy, vault.getWorld().getSeed(), vault.getX(), vault.getY(), vault.getZ());
        if (random.nextDouble() > genProb * plugin().item_generation_probabilityScalar) {
            return;
        }
        final List<ItemStack> dispensedLoot = event.getDispensedLoot();
        dispensedLoot.add(this.createItemStack(1));
    }

    void generateByVault(final BlockDispenseLootEvent event) {
        final Block dispensingVault = event.getBlock();
        if (!dispensingVault.getType().equals(Material.VAULT) || !(dispensingVault.getBlockData() instanceof Vault dispensingVaultData)) {
            return;
        }
        if (this.vaultGenProb != null && !dispensingVaultData.isOminous()) {
            this.generateByVault(dispensingVault, this.vaultGenProb, event);
        } else if (this.ominousVaultGenProb != null) {
            this.generateByVault(dispensingVault, this.ominousVaultGenProb, event);
        }
    }
}
