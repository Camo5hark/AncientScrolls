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

package com.andrewreedhall.ancientscrolls;

import com.google.common.base.CaseFormat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

/**
 * Global utility methods
 */
public final class Utils {
    public static final Collection<EntityType> ENTITY_TYPES_UNDEAD = Set.of(
            EntityType.BOGGED,
            EntityType.DROWNED,
            EntityType.HUSK,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.PHANTOM,
            EntityType.SKELETON,
            EntityType.STRAY,
            EntityType.WITHER_SKELETON,
            EntityType.ZOGLIN,
            EntityType.ZOMBIE,
            EntityType.ZOMBIFIED_PIGLIN
    );

    public static final Collection<EntityType> ENTITY_TYPES_SKELETON = Set.of(
            EntityType.SKELETON,
            EntityType.WITHER_SKELETON,
            EntityType.SKELETON_HORSE
    );

    public static final Collection<EntityType> ENTITY_TYPES_ILLAGER = Set.of(
            EntityType.EVOKER,
            EntityType.ILLUSIONER,
            EntityType.PILLAGER,
            EntityType.VINDICATOR,
            EntityType.RAVAGER,
            EntityType.VEX,
            EntityType.WITCH
    );

    public static final int NULL_SALT = 0xFFFF0000;

    /**
     * e.g. EntityType.CAVE_SPIDER -> "Cave Spider"
     * @param entityType
     * @return
     * @see Entity#getName()
     */
    public static String getEntityNameByType(EntityType entityType) {
        char[] upperCamel = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, entityType.toString()).toCharArray();
        StringBuilder entityName = new StringBuilder();
        for (int i = 0; i < upperCamel.length; ++i) {
            entityName.append(upperCamel[i]);
            int j = i + 1;
            if (j < upperCamel.length && Character.isUpperCase(upperCamel[j])) {
                entityName.append(' ');
            }
        }
        return entityName.toString();
    }

    /**
     * Exigence: Player#isOnGround is deprecated
     * @param player an online player
     * @return true if player is not flying, gliding, or levitating and is standing on top of a block
     */
    public static boolean isPlayerOnGround(Player player) {
        if (player.isFlying() || player.isGliding()) {
            return false;
        }
        double yVelocity = player.getVelocity().getY();
        // rules out levitation
        if (yVelocity > 0.0) {
            return false;
        }
        return player.getPotionEffect(PotionEffectType.SLOW_FALLING) != null ? yVelocity == -0.009800000190734863 : yVelocity == -0.0784000015258789;
    }

    public static boolean isPlayerUnderwater(Player player) {
        if (player.isSwimming()) {
            return true;
        }
        Material blockType = player.getLocation().getBlock().getType();
        Material eyeBlockType = player.getEyeLocation().getBlock().getType();
        return blockType.equals(Material.WATER) && eyeBlockType.equals(Material.WATER);
    }

    public static boolean isEntityNonBossMonster(Entity entity) {
        return entity instanceof Monster && !(entity instanceof EnderDragon || entity instanceof Wither);
    }

    /**
     *
     * @param event
     * @return damager or damager projectile shooter
     */
    public static Entity getActualAttacker(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Projectile) {
            ProjectileSource projectileSource = ((Projectile) damager).getShooter();
            if (projectileSource instanceof Entity) {
                damager = (Entity) projectileSource;
            }
        }
        return damager;
    }

    /**
     *
     * @param event
     * @param scalar
     */
    public static void addDamageByScalar(EntityDamageEvent event, double scalar) {
        event.setDamage(event.getDamage() + (OriginalEntityDamageListener.getOriginalDamage(event) * scalar));
    }

    public static Location getLootGenerateEventLocation(LootGenerateEvent event) {
        InventoryHolder inventoryHolder = event.getInventoryHolder();
        if (inventoryHolder instanceof BlockInventoryHolder blockInventoryHolder) {
            return blockInventoryHolder.getBlock().getLocation();
        }
        return inventoryHolder instanceof Entity entity ? entity.getLocation() : null;
    }

    public static Random createRandomBasedOnLocation(World world, Location location, int salt) {
        long h = location.getBlockX() ^ salt;
        long l = location.getBlockZ() ^ ~salt;
        long x = (h << 32) | l;
        long seed = world.getSeed() ^ x;
        return new Random(seed);
    }
}
