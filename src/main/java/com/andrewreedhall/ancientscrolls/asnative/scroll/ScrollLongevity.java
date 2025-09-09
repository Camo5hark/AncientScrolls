package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import oshi.util.tuples.Pair;

import java.util.Set;

public final class ScrollLongevity extends ItemScrollNative {
    public ScrollLongevity() {
        super("longevity", "Longevity", new String[] {
                "+4 max health"
        });
        this.modifyAttributesOfEquippingPlayers(
                Set.of(
                        new Pair<>(
                                Attribute.MAX_HEALTH,
                                new AttributeModifier(
                                        this.createSubkey("max_health"),
                                        4.0,
                                    AttributeModifier.Operation.ADD_NUMBER
                                )
                        )
                ),
                PLAYER_CONDITION_ALWAYS_TRUE,
                null
        );
        this.putMCLootTableGenProb("chests/stronghold_corridor", 0.119);
        this.putMCLootTableGenProb("chests/stronghold_library", 0.678);
        this.putMCLootTableGenProb("chests/woodland_mansion", 0.135);
        this.putMCLootTableGenProb("chests/bastion_bridge", 0.112);
        this.ominousVaultGenProb = 0.015;
    }
}
