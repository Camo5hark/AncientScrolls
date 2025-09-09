package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import oshi.util.tuples.Pair;

import java.util.Set;

public final class ScrollCobbling extends ItemScrollNative {
    public ScrollCobbling() {
        super("cobbling", "Cobbling", new String[] {
                "+15% movement speed"
        });
        // TODO dungeon
        this.putMCLootTableGenProb("chests/village/village_cartographer", 0.333);
        this.putMCLootTableGenProb("chests/village/village_temple", 0.448);
        this.modifyAttributesOfEquippingPlayers(
                Set.of(
                        new Pair<>(
                                Attribute.MOVEMENT_SPEED,
                                new AttributeModifier(
                                        this.createSubkey("movement_speed"),
                                        0.15,
                                        AttributeModifier.Operation.ADD_SCALAR
                                )
                        )
                ),
                PLAYER_CONDITION_ALWAYS_TRUE,
                (final Player player) -> {
                    if (player.isSneaking() || player.getForwardsMovement() <= 0.0F || !BukkitUtil.isPlayerOnGround(player)) {
                        return;
                    }
                    player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(0.0, 0.1, 0.0), 3, 0.25, 0.1, 0.25, 0.2);
                }
        );
    }
}
