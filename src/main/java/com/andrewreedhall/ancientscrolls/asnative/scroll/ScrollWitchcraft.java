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

package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollWitchcraft extends ItemScrollNative implements Listener {
    public ScrollWitchcraft() {
        super("witchcraft", "Witchcraft", new String[] {
                "Attack witch to transform it into villager"
        });
        this.putMCLootTableGenProb("entities/witch", 0.05);
        this.putMCLootTableGenProb("entities/evoker", 0.05);
        this.putMCLootTableGenProb("chests/woodland_mansion", 0.135);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Witch damagedWitch) ||
                !(BukkitUtil.getDamager(event) instanceof Player damagingPlayer) ||
                !this.isEquipping(damagingPlayer)
        ) {
            return;
        }
        damagedWitch.remove();
        final World damagedWitchWorld = damagedWitch.getWorld();
        final Villager villager = damagedWitchWorld.spawn(damagedWitch.getLocation(), Villager.class);
        villager.setVillagerType(getRandomRegistryValue(Registry.VILLAGER_TYPE));
        villager.setProfession(getRandomRegistryValue(Registry.VILLAGER_PROFESSION));
        damagedWitchWorld.playSound(damagedWitch, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.5F);
        damagedWitchWorld.spawnParticle(
                Particle.HAPPY_VILLAGER,
                damagedWitch.getEyeLocation(),
                10,
                0.5,
                0.5,
                0.5,
                0.1
        );
    }

    private static <T extends Keyed> T getRandomRegistryValue(final Registry<@NotNull T> registry) {
        final List<T> registryList = registry.stream().toList();
        return registryList.get(plugin().getUniversalRandom().nextInt(registryList.size()));
    }
}
