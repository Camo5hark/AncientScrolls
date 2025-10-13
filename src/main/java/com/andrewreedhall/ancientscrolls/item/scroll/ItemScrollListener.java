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

package com.andrewreedhall.ancientscrolls.item.scroll;

import com.andrewreedhall.ancientscrolls.item.AncientScrollsItem;
import com.andrewreedhall.ancientscrolls.PlayerDataHandler;
import com.andrewreedhall.ancientscrolls.util.Randomizer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

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
        if (keepInventory != null && keepInventory) {
            return;
        }
        final ItemScroll[] deadPlayerEquippedScrolls = PlayerDataHandler.getEquippedScrolls(deadPlayer);
        PlayerDataHandler.clearEquippedScrolls(deadPlayer);
        final Location deadPlayerLocation = deadPlayer.getLocation();
        for (final ItemScroll deadPlayerEquippedScroll : deadPlayerEquippedScrolls) {
            if (deadPlayerEquippedScroll == null) {
                continue;
            }
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
        deadEnderDragonWorld
                .getPlayers()
                .stream()
                .filter((final Player player) -> !player.isDead()).forEach((final Player player) -> {
                    final Block playerRewardChestBlock = player.getEyeLocation().getBlock().getRelative(BlockFace.UP);
                    playerRewardChestBlock.setType(Material.CHEST);
                    ((Chest) playerRewardChestBlock.getState()).getInventory().setContents(
                            plugin()
                                    .getItemRegistry()
                                    .getAll((final AncientScrollsItem item) -> item instanceof ItemScroll scroll && scroll.enderDragonReward, false)
                                    .map((final AncientScrollsItem item) -> item.createItemStack(1))
                                    .toArray(ItemStack[]::new)
                    );
                    playerRewardChestBlock.getWorld().spawnParticle(
                            Particle.DUST,
                            playerRewardChestBlock.getLocation(),
                            25,
                            0.5,
                            0.5,
                            0.5,
                            new Particle.DustOptions(Color.FUCHSIA, 1.0F)
                    );
                });
    }

    @EventHandler
    public void onEntitySpawn(final EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof WanderingTrader spawnedWanderingTrader)) {
            return;
        }
        final List<MerchantRecipe> spawnedWanderingTraderRecipes = new ArrayList<>(spawnedWanderingTrader.getRecipes());
        final List<ItemScroll> registeredNonSpecialScrolls = new ArrayList<>(
                ItemScroll.createListOfAllRegistered()
                        .parallelStream()
                        .filter((final ItemScroll scroll) -> !scroll.isSpecial())
                        .toList()
        );
        Randomizer.SCROLL_RANDOMIZER.sort(registeredNonSpecialScrolls, null);
        final MerchantRecipe scrollRecipe = new MerchantRecipe(
                registeredNonSpecialScrolls.get(
                        plugin()
                                .getUniversalRandom()
                                .nextInt(registeredNonSpecialScrolls.size())
                ).createItemStack(1),
                1
        );
        scrollRecipe.addIngredient(new ItemStack(Material.PAPER));
        scrollRecipe.addIngredient(new ItemStack(Material.EMERALD, plugin().getUniversalRandom().nextInt(20, 46)));
        spawnedWanderingTraderRecipes.add(scrollRecipe);
        spawnedWanderingTrader.setRecipes(spawnedWanderingTraderRecipes);
    }
}
