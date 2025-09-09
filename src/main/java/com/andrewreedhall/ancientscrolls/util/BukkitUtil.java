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

package com.andrewreedhall.ancientscrolls.util;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public final class BukkitUtil {
    public static void playBadSound(final Player player) {
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 0.5F);
    }

    public static boolean isRangedDamage(final EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof Projectile;
    }

    public static Entity getDamager(final EntityDamageByEntityEvent event) {
        final Entity damager = event.getDamager();
        return isRangedDamage(event) && ((Projectile) damager).getShooter() instanceof Entity shooter ? shooter : damager;
    }

    public static boolean isPlayerOnGround(final Player player) {
        if (player.isFlying() || player.isGliding() || player.isSwimming() || player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {
            return false;
        }
        double yVelocity = player.getVelocity().getY();
        return yVelocity < 0.0 && (player.hasPotionEffect(PotionEffectType.SLOW_FALLING) ? yVelocity == -0.009800000190734863 : yVelocity == -0.0784000015258789);
    }

    public static boolean canCrit(final Player player) {
        return player.getFallDistance() > 0.0 && !isPlayerOnGround(player);
    }

    public static AttributeInstance getAttributeInstance(final LivingEntity livingEntity, final Attribute attribute) {
        return Objects.requireNonNull(livingEntity.getAttribute(attribute), "Attribute " + attribute + " is not applicable for living entity " + livingEntity);
    }

    public static boolean hasAttributeModifier(final AttributeInstance attributeInstance, final NamespacedKey attributeModifierKey) {
        return attributeInstance.getModifiers().stream().anyMatch((final AttributeModifier attributeModifier) -> attributeModifier.getKey().equals(attributeModifierKey));
    }
}
