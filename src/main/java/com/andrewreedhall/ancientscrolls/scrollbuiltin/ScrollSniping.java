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
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorMonsterDrop;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.UUID;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollSniping extends Scroll {
    public ScrollSniping() {
        super(
                NamespacedKey.fromString("sniping", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Sniping",

                "Fired arrows instantly damage target"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.3, Chest.class, Structure.END_CITY),
                new ScrollGeneratorMonsterDrop(0.02, EntityType.ENDERMAN),
                new ScrollGeneratorMonsterDrop(0.005, EntityType.SKELETON)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Arrow arrow) || !(arrow.getShooter() instanceof Player playerShooter) || !this.doesPlayerHaveScroll(playerShooter)) {
            return;
        }
        // projectile not needed
        arrow.remove();
        Location playerShooterLocation = playerShooter.getEyeLocation();
        Vector playerShooterDirection = playerShooterLocation.getDirection();
        UUID playerShooterUID = playerShooter.getUniqueId();
        RayTraceResult rayTraceResult = playerShooter.getWorld().rayTraceEntities(
                playerShooterLocation,
                playerShooterDirection,
                Math.max(8, (playerShooter.getWorld().getSimulationDistance() - 1) * 16),
                (Entity entity) -> !entity.getUniqueId().equals(playerShooterUID)
        );
        if (rayTraceResult == null) {
            return;
        }
        Entity hitEntity = rayTraceResult.getHitEntity();
        if (!(hitEntity instanceof LivingEntity hitLivingEntity)) {
            return;
        }
        // hit target
        double distance = new Vector(playerShooterLocation.getX(), playerShooterLocation.getY(), playerShooterLocation.getZ()).distance(rayTraceResult.getHitPosition());
        hitLivingEntity.damage(arrow.getDamage() - (0.1 * distance), playerShooter);
        hitLivingEntity.setVelocity(hitLivingEntity.getVelocity().add(playerShooterDirection.multiply(8.0 / distance)));
        playerShooter.playSound(playerShooter, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
    }
}
