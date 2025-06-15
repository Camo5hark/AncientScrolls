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

import com.andrewreedhall.ancientscrolls.Utils;
import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerator;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollAcrobatics extends Scroll {
    private static final Set<UUID> DOUBLE_JUMPED_PLAYERS = new HashSet<>();

    public ScrollAcrobatics() {
        super(
                NamespacedKey.fromString("acrobatics", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Acrobatics",

                "Grants the ability to double jump",
                "LEFT CLICK when airborne"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.3, StorageMinecart.class, Structure.MINESHAFT),
                new ScrollGeneratorTreasure(0.1, Chest.class, Structure.RUINED_PORTAL)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (!action.equals(Action.LEFT_CLICK_AIR) && !action.equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }
        Player player = event.getPlayer();
        UUID playerUID = player.getUniqueId();
        if (DOUBLE_JUMPED_PLAYERS.contains(playerUID) || Utils.isPlayerOnGround(player) || !this.doesPlayerHaveScroll(player)) {
            return;
        }
        DOUBLE_JUMPED_PLAYERS.add(player.getUniqueId());
        // Attribute.JUMP_STRENGTH always returns save value no matter the effect fsr, so this works instead
        PotionEffect jumpBoostEffect = player.getPotionEffect(PotionEffectType.JUMP_BOOST);
        double yVelocity = 0.7;
        if (jumpBoostEffect != null) {
            int amplifier = jumpBoostEffect.getAmplifier() + 1;
            yVelocity += 0.1 * amplifier;
        }
        player.setVelocity(player.getVelocity().multiply(1.7).setY(yVelocity));
        World world = player.getWorld();
        world.playSound(player, Sound.BLOCK_SAND_PLACE, 1.5F, 1.5F);
        world.spawnParticle(Particle.CLOUD, player.getLocation().add(0.0, 0.2, 0.0), 8, 0.4, 0.2, 0.4, 0.1);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUID = player.getUniqueId();
        if (!DOUBLE_JUMPED_PLAYERS.contains(playerUID) || !Utils.isPlayerOnGround(player)) {
            return;
        }
        DOUBLE_JUMPED_PLAYERS.remove(playerUID);
        if (player.getFallDistance() < 5.0F) {
            player.setFallDistance(0.0F);
        }
    }
}
