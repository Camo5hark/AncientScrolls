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

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public final class ScrollBallistics extends ItemScrollNative implements Listener {
    public ScrollBallistics() {
        super("ballistics", "Ballistics", new String[] {
                "Doubles projectile velocity"
        });
        this.putMCLootTableGenProb("entities/skeleton", 0.005);
        this.putMCLootTableGenProb("entities/stray", 0.005);
        this.putMCLootTableGenProb("entities/bogged", 0.005);
        this.putMCLootTableGenProb("chests/trial_chambers/entrance", 0.31);
        this.putMCLootTableGenProb("chests/pillager_outpost", 0.11);
    }

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        final Projectile launchedProjectile = event.getEntity();
        if (!(launchedProjectile.getShooter() instanceof Player launchingPlayer) || !this.isEquipping(launchingPlayer)) {
            return;
        }
        launchedProjectile.setVelocity(launchedProjectile.getVelocity().multiply(2.0));
        final World launchingPlayerWorld = launchingPlayer.getWorld();
        launchingPlayerWorld.playSound(launchingPlayer, Sound.BLOCK_DISPENSER_LAUNCH, 1.0F, 0.75F);
        launchingPlayerWorld.spawnParticle(Particle.SMOKE, launchingPlayer.getEyeLocation(), 10, 0.5, 0.5, 0.5, 0.1);
    }
}
