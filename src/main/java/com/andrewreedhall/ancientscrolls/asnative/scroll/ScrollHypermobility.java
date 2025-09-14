package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import oshi.util.tuples.Pair;

import java.util.Set;

public final class ScrollHypermobility extends ItemScrollNative {
    public ScrollHypermobility() {
        super("hypermobility", "Hypermobility", new String[] {
                "Increase reach by 4 blocks"
        });
        this.special = true;
        this.putMCLootTableGenProb("chests/end_city_treasure", 0.131);
        this.modifyAttributesOfEquippingPlayers(
                Set.of(
                        new Pair<>(
                                Attribute.BLOCK_INTERACTION_RANGE,
                                new AttributeModifier(
                                        this.createSubkey("block_interaction_range"),
                                        4.0,
                                        AttributeModifier.Operation.ADD_NUMBER
                                )
                        ),
                        new Pair<>(
                                Attribute.ENTITY_INTERACTION_RANGE,
                                new AttributeModifier(
                                        this.createSubkey("entity_interaction_range"),
                                        4.0,
                                        AttributeModifier.Operation.ADD_NUMBER
                                )
                        )
                ),
                CONDITION_BYPASS,
                null
        );
    }
}
