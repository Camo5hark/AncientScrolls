package com.andrewreedhall.ancientscrolls.asnative.scroll;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import oshi.util.tuples.Pair;

import java.util.Set;

public final class ScrollFlexibility extends ItemScrollNative {
    public ScrollFlexibility() {
        super("flexibility", "Flexibility", new String[] {
                "Increase reach by 2 blocks"
        });
        this.vaultGenProb = 0.035;
        this.ominousVaultGenProb = 0.11;
        this.modifyAttributesOfEquippingPlayers(
                Set.of(
                        new Pair<>(
                                Attribute.BLOCK_INTERACTION_RANGE,
                                new AttributeModifier(
                                        this.createSubkey("block_interaction_range"),
                                        2.0,
                                        AttributeModifier.Operation.ADD_NUMBER
                                )
                        ),
                        new Pair<>(
                                Attribute.ENTITY_INTERACTION_RANGE,
                                new AttributeModifier(
                                        this.createSubkey("entity_interaction_range"),
                                        2.0,
                                        AttributeModifier.Operation.ADD_NUMBER
                                )
                        )
                ),
                CONDITION_BYPASS,
                null
        );
    }
}
