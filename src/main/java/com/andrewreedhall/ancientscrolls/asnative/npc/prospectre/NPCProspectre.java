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

package com.andrewreedhall.ancientscrolls.asnative.npc.prospectre;

import com.andrewreedhall.ancientscrolls.asnative.npc.NPCNative;
import com.andrewreedhall.ancientscrolls.npc.NPCInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import oshi.util.tuples.Pair;

import java.util.Set;

public final class NPCProspectre extends NPCNative implements Listener {
    private static final NPCInstance.Skin SKIN = new NPCInstance.Skin(
            "ewogICJ0aW1lc3RhbXAiIDogMTYzMTk5NjIzNTk3OSwKICAicHJvZmlsZUlkIiA6ICJhYTU1YzQ5ODM1ZTA0OGEyODgzYjdkNGU2MmQ2MTc4YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJLb2luZ19NYXJjdXMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA0NjZiZTYxYjZmZDllNmY5MWFkNzA4M2FmNDAxOTM1OGYwNjBhZTAzYjRmYzAzMWQ3ZDZiNWU3NmEzNTQzZiIKICAgIH0KICB9Cn0=",
            "iKEqxFxKE3OCIJkCdZlfUXDOQzMzWFr4hhqUPoLOjkW6DNHQzMmSJvxqb28hzGjAgEBIyXFlOdiGncLgUOhQ2pozsFL2cfsIWcjSELN9bqstl7PaK4PYBwiPhUbWCSRswXl9ehQyBiqk50zm02h6X3okRPEtfl1ij+WFHgsUUrwcEDJEFPVboB1xnO2KWvIWTb0cm3XpIBKrHHtrvP/iogPLJNBT/bhxlvn3sgjhexqQGdAwM49Ltw8MTT0nqCPR62X++u71bOIco4ELtDp8KG2c7tZYFAJsLkHwEFw0u/pmczTOeZ0je29YSqIAaseYxHkRpgOCv/BO588abK3KTBdLD/H+ooOyi3f4/fwsd7x3E53OxTlnfuuKiBsOEvwJUODyoxQZEnX3kIkIqIOpZNspyE/qvrvUzX30RANxAEGDWmzAJolfVS4tVSHf4qWvce6Vg9NNZCXcvvd3ln3KeKovXy2RJRibVBHHqe+dv1Yhl9x6krasbKpHqhD8sLelJ7J4arEK6GnozzUy91h7kRmaFacJwgVz/Lq3b/d01GXyhqxaMOOMBcB2EzLRivXV9NobJchN7rHWJ+FKc19aBGjLuRQS+SzEctTgmdK8SSjviuiXS+9HixwBVilXz2d7Zj8a/AHKovmbp5Zqf2jd/m8e0YgsXT2snmMBgSdvXoI="
    );

    public NPCProspectre() {
        super(
                "prospectre",
                "Prospectre",
                SKIN,
                new AdditionalAddInstanceToClientPacketBuilder(),
                Set.of(
                        new Pair<>(
                                (final Player player) -> false,
                                0.01
                        )
                )
        );
    }

    @EventHandler
    public void onPlayerInteractAtEntity(final PlayerInteractAtEntityEvent event) {
        if (!event.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }
        final NPCInstance interactedNPCInstance = this.getInstance(event.getRightClicked());
    }
}
