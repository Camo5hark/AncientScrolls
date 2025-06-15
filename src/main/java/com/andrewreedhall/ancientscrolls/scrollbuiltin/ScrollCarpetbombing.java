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

    License file: COPYING
    Email for contact: camo5hark10@gmail.com

 */

package com.andrewreedhall.ancientscrolls.scrollbuiltin;

import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerator;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollCarpetbombing extends Scroll {
    public ScrollCarpetbombing() {
        super(
                NamespacedKey.fromString("carpetbombing", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Carpetbombing",

                "Grants ability to drop TNT when flying with elytra"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.05, Chest.class, Structure.END_CITY)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        if (itemStack == null || !itemStack.getType().equals(Material.TNT)) {
            return;
        }
        Player player = event.getPlayer();
        if (!player.isGliding() || player.hasCooldown(Material.TNT) || !this.doesPlayerHaveScroll(player)) {
            return;
        }
        ItemStack chestItemStack = player.getInventory().getItem(EquipmentSlot.CHEST);
        if (chestItemStack == null || !chestItemStack.getType().equals(Material.ELYTRA)) {
            return;
        }
        itemStack.setAmount(itemStack.getAmount() - 1);
        player.setCooldown(Material.TNT, 80);
        player.playSound(player, Sound.ENTITY_TNT_PRIMED, 1.0F, 1.0F);
        TNTPrimed tnt = player.getWorld().spawn(player.getLocation().subtract(0.0, 1.0, 0.0), TNTPrimed.class);
        tnt.setSource(player);
        tnt.setVelocity(player.getVelocity().setY(0).multiply(0.5));
        tnt.setFuseTicks(80);
    }
}
