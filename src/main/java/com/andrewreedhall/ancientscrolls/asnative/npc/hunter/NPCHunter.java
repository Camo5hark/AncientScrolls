package com.andrewreedhall.ancientscrolls.asnative.npc.hunter;

import com.andrewreedhall.ancientscrolls.asnative.npc.NPCNative;
import com.andrewreedhall.ancientscrolls.npc.NPCInstance;
import com.andrewreedhall.ancientscrolls.util.CommonSets;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import oshi.util.tuples.Pair;

import java.util.Set;

public final class NPCHunter extends NPCNative {
    private static final NPCInstance.Skin SKIN = new NPCInstance.Skin(
            "ewogICJ0aW1lc3RhbXAiIDogMTc1OTE4Mjk4MzgzNywKICAicHJvZmlsZUlkIiA6ICJlZDUzZGQ4MTRmOWQ0YTNjYjRlYjY1MWRjYmE3N2U2NiIsCiAgInByb2ZpbGVOYW1lIiA6ICI0MTQxNDE0MWgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYwMzkwNmE4ZDY5NDk2YWYxNDFjNzU4NjJlZTQxZjI3ZTVlY2NkY2RhZjAzNzI0MzI5OTViYmYzZDUxN2FhYiIKICAgIH0KICB9Cn0=",
            "ZEpAeMsFPcrXAZX9ZPIh/5xqE7FE281WQ7Y/k2OFQUC4JVE07rZ0C3ZKbgUppNyWBv5uq6t/yJK/6ks7o+McjRNXG9dD5NFYSnfcj6Ad27XtJQ+iIqXq4l6NmG+jiinoqLtmYNP/3DceC6IZ3pKHd52dZZ9vPizB6JDNVSIj3Pf9f5+DUZFESMxH2/uPxoX//E1ZhETXxZQG0ePH3KM65teMNNzdAfpvlrN6nufDszl0OGxI3fYxnH5E0XjMX+/lFvsBI4I9kR5dvUfMbvSMoODl0L3NDYj0oy8IGHjkx6+wMEsgiZS4j0Ccy78oXh8QaFmWn+Y4uhvivC56hT0/vWRX4oXQLVlCNk7KQcolorPpB7ZoCDJc2v4ZZQmHq7dnbrcS9/EK/I+Mh6o8bIsS9qoGUOXqzjmzWASHo7Dq7RqCnb64WBsErZdY+BkQfjM5XZgDIq1YxL/rJgzDfla+3624YtCHt5YE/p23wWG1daGMHqnmVqeVlHcA8gpTqAgoAL+Pn1mzQDXQnTKJDRkOaNnGXMnRdNIrNXKgf6c0L+ppUENjm2jV1eM995q6o3EBcn6pCD70BKQkOwAaZA+4ygciBvpZvj8YuuoaybBWWf6xBkzoCShzmHZQLQ/szUZpwZZOYTJ+w9AX4M0aLbF3lsov0EWGRC8c4k70NtK3WXs="
    );

    public NPCHunter() {
        super(
                "hunter",
                "Hunter",
                SKIN,
                new Auxiliary(),
                new Pair<>(
                        (final LivingEntity livingEntity) -> {
                            if (!CommonSets.UNDEAD_MONSTERS.contains(livingEntity.getType()) || livingEntity.getWorld().isDayTime()) {
                                return false;
                            }
                            final Location livingEntityLocation = livingEntity.getLocation();
                            return livingEntityLocation.getBlockY() >= 60 &&
                                    CommonSets.WOODLAND_BIOMES.contains(livingEntityLocation.getBlock().getBiome());
                        },
                        0.005
                ),
                Set.of(
                        "ancientscrolls:ballistics",
                        "ancientscrolls:canine_studies",
                        "ancientscrolls:nocturnal",
                        "ancientscrolls:vigilance",
                        "ancientscrolls:jousting",
                        "ancientscrolls:bioluminescence",
                        "ancientscrolls:dexterity"
                ),
                Set.of(
                        new Pair<>(Material.COOKED_BEEF, 20),
                        new Pair<>(Material.COOKED_RABBIT, 10),
                        new Pair<>(Material.COOKED_MUTTON, 20),
                        new Pair<>(Material.BOW, 1),
                        new Pair<>(Material.CROSSBOW, 1),
                        new Pair<>(Material.IRON_SWORD, 1)
                )
        );
    }
}
