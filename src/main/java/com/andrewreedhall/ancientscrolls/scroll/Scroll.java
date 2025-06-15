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

import com.andrewreedhall.ancientscrolls.Utils;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerator;
import com.andrewreedhall.ancientscrolls.scroll.player.PlayerStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Stream;

/**
 * Represents a to-be-registered Ancient Scroll singleton
 */
public abstract class Scroll implements Listener {
    /**
     * Behavioral flags<br>
     * Register events: singleton's @EventHandler methods will be registered to server upon registration<br>
     * Schedule per affected player: #onScheduledTickPerAffectedPlayer(Player) will be executed once per player with this scroll in their storage per tick<br>
     * Schedule per unaffected player: #onScheduledTickPerUnaffectedPlayer(Player) will be executed once per player without this scroll in their storage per tick
     */
    public static final int
    FLAG_REGISTER_EVENTS = 0x80000000,
    FLAG_SCHEDULE_PER_AFFECTED_PLAYER = 0x40000000,
    FLAG_SCHEDULE_PER_UNAFFECTED_PLAYER = 0x20000000;

    /**
     * Central identifier of this scroll<br>
     * General format: [plugin_name]:[scroll_name]
     */
    public final NamespacedKey id;
    /**
     * Behavioral flag bitfield
     * @see #FLAG_REGISTER_EVENTS
     */
    public int flags;
    /**
     * Scroll item stack lore containing name and description to be added to new scroll item stacks<br>
     * Item stack lore is the same for all item stacks of this scroll, so should be cached for making new item stacks efficiently
     * @see #createItemStackLore(String, String[])
     * @see #createItemStack()
     */
    private final List<String> cachedItemStackLore;
    /**
     * Frequency of execution of scheduled behavior
     * @see #FLAG_SCHEDULE_PER_AFFECTED_PLAYER
     * @see #onScheduledTickPerAffectedPlayer(Player)
     * @see #onScheduledTickPerUnaffectedPlayer(Player)
     */
    public long scheduledTickPeriod = 1;
    /**
     * The last tick where the scheduled behavior was executed
     * @see #FLAG_SCHEDULE_PER_AFFECTED_PLAYER
     * @see #onScheduledTickPerAffectedPlayer(Player)
     * @see #onScheduledTickPerUnaffectedPlayer(Player)
     */
    public long lastScheduledTick = 0;
    public ScrollGenerator[] generators = null;

    /**
     * All parameters should be statically filled in inheriting class's super call<br>
     * Will cause error upon registration if this condition is not met
     * @param id @see #id
     * @param flags @see #flags
     * @param title the human-readable name of this scroll for the item stack lore
     * @param descriptionElements the human-readable description of this scroll for the item stack lore
     */
    public Scroll(NamespacedKey id, int flags, String title, String... descriptionElements) {
        this.id = id;
        this.flags = flags;
        this.cachedItemStackLore = this.createItemStackLore(title, descriptionElements);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof Scroll && obj.hashCode() == this.hashCode());
    }

    /**
     *
     * @param player a player with this scroll in their storage
     * @see #FLAG_SCHEDULE_PER_AFFECTED_PLAYER
     */
    public abstract void onScheduledTickPerAffectedPlayer(Player player);

    /**
     *
     * @param player a player that does not have this scroll in their storage
     * @see #FLAG_SCHEDULE_PER_UNAFFECTED_PLAYER
     */
    public abstract void onScheduledTickPerUnaffectedPlayer(Player player);

    /**
     *
     * @param title @see #Scroll(NamespacedKey, int, String, String...)
     * @param descriptionElements @see #Scroll(NamespacedKey, int, String, String...)
     * @return an unmodifiable list of strings representing the item stack lore for new scroll item stacks
     */
    private List<String> createItemStackLore(String title, String[] descriptionElements) {
        List<String> itemStackLore = new ArrayList<>();
        itemStackLore.add(ChatColor.LIGHT_PURPLE + title);
        for (String descriptionElement : descriptionElements) {
            itemStackLore.add(ChatColor.GRAY + descriptionElement);
        }
        return Collections.unmodifiableList(itemStackLore);
    }

    /**
     *
     * @param flag @see #FLAG_REGISTER_EVENTS
     * @return true if the specified flag is present in the #flags bitfield
     */
    public boolean isFlagHigh(int flag) {
        return (this.flags & flag) != 0;
    }

    /**
     *
     * @return a new item stack representing this scroll
     */
    public ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            throw new NullPointerException("itemMeta == null");
        }
        CustomModelDataComponent itemData = itemMeta.getCustomModelDataComponent();
        itemData.setStrings(List.of(this.id.toString()));
        itemMeta.setCustomModelDataComponent(itemData);
        itemMeta.setDisplayName(ChatColor.GOLD + "Ancient Scroll");
        itemMeta.setLore(this.cachedItemStackLore);
        itemMeta.addEnchant(Enchantment.UNBREAKING, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     *
     * @param player a player
     * @return true if the player has this scroll in their storage
     */
    public boolean doesPlayerHaveScroll(Player player) {
        PlayerStorage.Instance playerStorage = PlayerStorage.access(player);
        for (Scroll scroll : playerStorage.scrolls) {
            if (scroll != null && scroll.equals(this)) {
                return true;
            }
        }
        return false;
    }

    protected NamespacedKey createAttributeModifierID(String name) {
        return NamespacedKey.fromString(this.id.toString() + "_" + name);
    }

    protected Optional<AttributeModifier> getAttributeModifier(Stream<AttributeModifier> attributeModifiers, NamespacedKey modifierID) {
        return attributeModifiers.filter((AttributeModifier attributeModifier) -> attributeModifier.getKey().equals(modifierID)).findAny();
    }

    protected void removeAttributeModifierIfPresent(AttributeInstance attributeInstance, NamespacedKey modifierID) {
        Optional<AttributeModifier> attributeModifier = this.getAttributeModifier(attributeInstance.getModifiers().stream(), modifierID);
        if (attributeModifier.isEmpty()) {
            return;
        }
        attributeInstance.removeModifier(attributeModifier.get());
    }

    protected void removeAttributeModifierIfPresent(Player player, Attribute attribute, NamespacedKey modifierID) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance == null) {
            return;
        }
        removeAttributeModifierIfPresent(attributeInstance, modifierID);
    }

    protected void addAttributeModifierIfNotPresent(AttributeInstance attributeInstance, NamespacedKey modifierID, double amount, AttributeModifier.Operation operation) {
        if (this.getAttributeModifier(attributeInstance.getModifiers().stream(), modifierID).isPresent()) {
            return;
        }
        attributeInstance.addModifier(new AttributeModifier(modifierID, amount, operation, EquipmentSlotGroup.ANY));
    }

    protected void addAttributeModifierIfNotPresent(Player player, Attribute attribute, NamespacedKey modifierID, double amount, AttributeModifier.Operation operation) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance == null) {
            return;
        }
        addAttributeModifierIfNotPresent(attributeInstance, modifierID, amount, operation);
    }

    protected void negateEffect(EntityPotionEffectEvent event, PotionEffectType effectType) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        PotionEffect effect = event.getNewEffect();
        if (effect == null || !effect.getType().equals(effectType) || !this.doesPlayerHaveScroll(player)) {
            return;
        }
        event.setCancelled(true);
    }

    protected void negateDamageFrom(EntityDamageByEntityEvent event, Collection<Class<? extends Entity>> entityTypes) {
        if (!entityTypes.contains(event.getDamager().getClass()) || !(event.getEntity() instanceof Player player) || !this.doesPlayerHaveScroll(player)) {
            return;
        }
        event.setCancelled(true);
    }

    protected void addDamageToPlayerByScalar(EntityDamageByEntityEvent event, double scalar, Collection<EntityType> attackerTypes) {
        if (attackerTypes == null || !attackerTypes.contains(Utils.getActualAttacker(event).getType())) {
            return;
        }
        if (!(event.getEntity() instanceof Player player) || !this.doesPlayerHaveScroll(player)) {
            return;
        }
        Utils.addDamageByScalar(event, scalar);
    }

    protected void addDamageToEntityByScalar(EntityDamageByEntityEvent event, double scalar, Collection<EntityType> entityTypes) {
        if (entityTypes == null || !entityTypes.contains(event.getEntity().getType())) {
            return;
        }
        if (!(Utils.getActualAttacker(event) instanceof Player player) || !this.doesPlayerHaveScroll(player)) {
            return;
        }
        Utils.addDamageByScalar(event, scalar);
    }
}
