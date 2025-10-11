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

import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollAcrobatics extends ItemScrollNative implements Listener {
    private static final String PMK_DOUBLE_JUMPED = "scroll_acrobatics_double_jumped";

    public ScrollAcrobatics() {
        super("acrobatics", "Acrobatics", new String[] {
                "Grants double jumping"
        });
        // TODO dungeon
        this.putMCLootTableGenProb("chests/abandoned_mineshaft", 0.282);
        this.putMCLootTableGenProb("chests/ruined_portal", 0.073);
        this.putMCLootTableGenProb("entities/slime", 0.005);
        this.vaultGenProb = 0.085;
        this.scheduleRepeatingTaskPerEquippingPlayer((final Player equippingPlayer) -> {
            if ((equippingPlayer.hasMetadata(PMK_DOUBLE_JUMPED) && !equippingPlayer.getMetadata(PMK_DOUBLE_JUMPED).getFirst().asBoolean()) || !BukkitUtil.isPlayerOnGround(equippingPlayer)) {
                return;
            }
            equippingPlayer.removeMetadata(PMK_DOUBLE_JUMPED, plugin());
            if (equippingPlayer.getFallDistance() < 5.0F) {
                equippingPlayer.setFallDistance(0.0F);
            }
        }, 1L);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (!action.equals(Action.LEFT_CLICK_AIR) && !action.equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }
        final Player interactingPlayer = event.getPlayer();
        if ((interactingPlayer.hasMetadata(PMK_DOUBLE_JUMPED) && interactingPlayer.getMetadata(PMK_DOUBLE_JUMPED).getFirst().asBoolean()) || BukkitUtil.isPlayerOnGround(interactingPlayer) || !this.isEquipping(interactingPlayer)) {
            return;
        }
        interactingPlayer.setMetadata(PMK_DOUBLE_JUMPED, new FixedMetadataValue(plugin(), true));
        final PotionEffect interactingPlayerJumpBoostEffect = interactingPlayer.getPotionEffect(PotionEffectType.JUMP_BOOST);
        double interactingPlayerYVel = 0.7;
        if (interactingPlayerJumpBoostEffect != null) {
            interactingPlayerYVel += 0.1 * (interactingPlayerJumpBoostEffect.getAmplifier() + 1);
        }
        interactingPlayer.setVelocity(interactingPlayer.getVelocity().setY(interactingPlayerYVel));
        final World interactingPlayerWorld = interactingPlayer.getWorld();
        interactingPlayerWorld.playSound(interactingPlayer, Sound.BLOCK_SAND_PLACE, 1.5F, 1.5F);
        interactingPlayerWorld.spawnParticle(Particle.CLOUD, interactingPlayer.getLocation().add(0.0, 0.2, 0.0), 8, 0.4, 0.2, 0.4, 0.1);
    }
}
