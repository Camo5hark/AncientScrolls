package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import oshi.util.tuples.Pair;

import java.util.Set;

public final class ScrollCrossCountry extends ItemScrollNative {
    private static final Set<Material> BLOCK_TYPES = Set.of(
            Material.SNOW,
            Material.SNOW_BLOCK,
            Material.POWDER_SNOW,
            Material.ICE,
            Material.PACKED_ICE,
            Material.BLUE_ICE,
            Material.FROSTED_ICE
    );

    public ScrollCrossCountry() {
        super("cross_country", "Cross Country", new String[] {
                "+20% movement speed on snow and ice",
                "Turns powdered snow below user into regular snow"
        });
        this.putMCLootTableGenProb("chests/igloo_chest", 0.25);
        this.putMCLootTableGenProb("chests/village/village_snowy_house", 0.099);
        this.putMCLootTableGenProb("chests/village/village_taiga_house", 0.097);
        this.putMCLootTableGenProb("entities/stray", 0.005);
        this.modifyAttributesOfEquippingPlayers(
                Set.of(
                        new Pair<>(
                                Attribute.MOVEMENT_SPEED,
                                new AttributeModifier(
                                        this.createSubkey("movement_speed"),
                                        0.2,
                                        AttributeModifier.Operation.ADD_SCALAR
                                )
                        )
                ),
                (final Player equippingPlayer) -> {
                    final Block equippingPlayerBlock = equippingPlayer.getLocation().getBlock();
                    for (int i = 0; i < 3; ++i) {
                        if (BLOCK_TYPES.contains(equippingPlayerBlock.getRelative(BlockFace.DOWN, i).getType())) {
                            return true;
                        }
                    }
                    return false;
                },
                (final Player equippingPlayer) -> {
                    final Block equippingPlayerBlockBelow = equippingPlayer.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    if (!equippingPlayerBlockBelow.getType().equals(Material.POWDER_SNOW)) {
                        return;
                    }
                    equippingPlayerBlockBelow.setType(Material.SNOW_BLOCK);
                    final World equippingPlayerWorld = equippingPlayerBlockBelow.getWorld();
                    equippingPlayerWorld.playSound(equippingPlayer, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.0F, 1.0F);
                    equippingPlayerWorld.spawnParticle(Particle.HAPPY_VILLAGER, equippingPlayerBlockBelow.getLocation(), 5, 0.5, 0.5, 0.5, 0.1);
                }
        );
    }
}
