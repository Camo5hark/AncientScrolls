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

package com.andrewreedhall.ancientscrolls.asnative.item.scroll;

import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class ScrollFlameCasting extends ItemScrollNative implements Listener {
    public ScrollFlameCasting() {
        super("flame_casting", "Flame Casting", new String[] {
                "Consume fire charges to launch fire balls",
                "Slain nether monsters drop fire charges"
        });
        this.putMCLootTableGenProb("entities/blaze", 0.005);
        this.putMCLootTableGenProb("entities/ghast", 0.01);
        this.putMCLootTableGenProb("chests/bastion_treasure", 0.386);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }
        final Player interactingPlayer = event.getPlayer();
        if (interactingPlayer.getCooldown(Material.FIRE_CHARGE) > 0 || !this.isEquipping(interactingPlayer)) {
            return;
        }
        final ItemStack interactingItemStack = event.getItem();
        if (interactingItemStack == null || !interactingItemStack.getType().equals(Material.FIRE_CHARGE)) {
            return;
        }
        event.setCancelled(true);
        if (!interactingPlayer.getGameMode().equals(GameMode.CREATIVE)) {
            interactingItemStack.subtract();
        }
        interactingPlayer.setCooldown(interactingItemStack, 10);
        interactingPlayer.launchProjectile(SmallFireball.class, interactingPlayer.getLocation().getDirection());
        final World interactingPlayerWorld = interactingPlayer.getWorld();
        interactingPlayerWorld.playSound(interactingPlayer, Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
        interactingPlayerWorld.spawnParticle(Particle.FLAME, interactingPlayer.getEyeLocation(), 10, 0.5, 0.5, 0.5, 0.1);
    }

    @EventHandler
    public void onEntityDeathEvent(final EntityDeathEvent event) {
        if (!(event.getDamageSource().getCausingEntity() instanceof Player killingPlayer) || !CommonSets.NETHER_MONSTERS.contains(event.getEntity().getType()) || !this.isEquipping(killingPlayer)) {
            return;
        }
        event.getDrops().add(new ItemStack(Material.FIRE_CHARGE));
    }
}
