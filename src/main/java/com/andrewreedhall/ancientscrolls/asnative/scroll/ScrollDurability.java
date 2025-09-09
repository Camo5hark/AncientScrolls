package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import oshi.util.tuples.Pair;

import java.util.Set;

public final class ScrollDurability extends ItemScrollNative {
    public ScrollDurability() {
        super("durability", "Durability", new String[] {
                "+8 max health"
        });
        this.putMCLootTableGenProb("chests/stronghold_corridor", 0.025);
        this.putMCLootTableGenProb("chests/end_city_treasure", 0.21);
        this.modifyAttributesOfEquippingPlayers(
                Set.of(
                        new Pair<>(
                                Attribute.MAX_HEALTH,
                                new AttributeModifier(
                                        createSubkey("max_health"),
                                        8.0,
                                        AttributeModifier.Operation.ADD_NUMBER
                                )
                        )
                ),
                PLAYER_CONDITION_ALWAYS_TRUE,
                null
        );
    }
}
