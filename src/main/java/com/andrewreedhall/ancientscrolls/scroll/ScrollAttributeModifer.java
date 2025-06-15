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

package com.andrewreedhall.ancientscrolls.scroll;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public abstract class ScrollAttributeModifer extends Scroll {
    private final Attribute attribute;
    private final NamespacedKey attributeModifierID;
    private final double attributeModifierAmount;
    private final AttributeModifier.Operation attributeModifierOperation;

    /**
     * All parameters should be statically filled in inheriting class's super call<br>
     * Will cause error upon registration if this condition is not met
     *
     * @param id                  @see #id
     * @param title               the human-readable name of this scroll for the item stack lore
     * @param descriptionElements the human-readable description of this scroll for the item stack lore
     */
    public ScrollAttributeModifer(NamespacedKey id, String title, Attribute attribute, double attributeModifierAmount, AttributeModifier.Operation attributeModifierOperation, String... descriptionElements) {
        super(id, Scroll.FLAG_SCHEDULE_PER_AFFECTED_PLAYER | Scroll.FLAG_SCHEDULE_PER_UNAFFECTED_PLAYER, title, descriptionElements);
        this.attribute = attribute;
        this.attributeModifierID = this.createAttributeModifierID(attribute.toString().toLowerCase());
        this.attributeModifierAmount = attributeModifierAmount;
        this.attributeModifierOperation = attributeModifierOperation;
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {
        this.addAttributeModifierIfNotPresent(player, this.attribute, this.attributeModifierID, this.attributeModifierAmount, this.attributeModifierOperation);
    }

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {
        this.removeAttributeModifierIfPresent(player, this.attribute, this.attributeModifierID);
    }
}
