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

import com.andrewreedhall.ancientscrolls.AncientScrollsRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Vault;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public abstract class AncientScrollsItem extends AncientScrollsRegistry.Value {
    private final long entropy;
    private final Map<NamespacedKey, Double> lootTableGenProbs = new HashMap<>();
    protected Double dungeonChestGenProb = null;
    protected Double vaultGenProb = null;
    protected Double ominousVaultGenProb = null;

    public AncientScrollsItem(final NamespacedKey key) {
        super(key);
        this.entropy = new Random(this.key.hashCode()).nextLong();
    }

    public abstract ItemStack createItemStack(int amount);

    protected void putMCLootTableGenProb(final String id, final double prob) {
        this.lootTableGenProbs.put(NamespacedKey.minecraft(id), prob);
    }

    public void generateByLootTable(final LootGenerateEvent event) {
        final Double lootTableGenProb = this.lootTableGenProbs.get(event.getLootTable().getKey());
        if (lootTableGenProb == null) {
            return;
        }
        // TODO debugger
        plugin().getLogger().info(this.key + " attempting generation in " + event.getLootTable().getKey());
        // TODO --------
        Random random;
        if (event.getInventoryHolder() instanceof final BlockInventoryHolder blockInventoryHolder) {
            random = this.createLocationBasedRandom(blockInventoryHolder.getBlock().getLocation());
        } else {
            random = plugin().getUniversalRandom();
        }
        if (random.nextDouble() > lootTableGenProb) {
            return;
        }
        // TODO debugger
        plugin().getLogger().info(this.key + " generating in " + event.getLootTable().getKey());
        // TODO --------
        final List<ItemStack> generatedLoot = event.getLoot();
        generatedLoot.removeFirst();
        event.getLoot().add(this.createItemStack(1));
    }

    public void generateByDungeonChest() {
    }

    private void generateByVault(final Block vault, final double genProb, final BlockDispenseLootEvent event) {
        if (this.createLocationBasedRandom(vault.getLocation()).nextDouble() > genProb) {
            return;
        }
        final List<ItemStack> dispensedLoot = event.getDispensedLoot();
        dispensedLoot.removeFirst();
        dispensedLoot.add(this.createItemStack(1));
    }

    public void generateByVault(final BlockDispenseLootEvent event) {
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

    private Random createLocationBasedRandom(final Location location) {
        final long x = (long) (location.getBlockX() & 0xFFFF) << 32;
        final long y = (long) (location.getBlockY() & 0xFFFF) << 16;
        final long z = location.getBlockZ() & 0xFFFF;
        long seed = (x | y | z) & this.entropy;
        final World world = location.getWorld();
        if (world != null) {
            seed ^= world.getSeed();
        }
        return new Random(seed);
    }
}
