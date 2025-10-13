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

package com.andrewreedhall.ancientscrolls.item.scroll;

import com.andrewreedhall.ancientscrolls.PlayerDataHandler;
import com.andrewreedhall.ancientscrolls.item.AncientScrollsItem;
import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Represents a special item scroll with effects, lore, and generation behavior.
 */
public abstract class ItemScroll extends AncientScrollsItem {
    private static final List<Boolean> CACHED_FLAGS = List.of(true);
    private static final Component CACHED_DISPLAY_NAME = Component.text("Ancient Scroll", NamedTextColor.GOLD);
    /**
     * Night vision effect used by scrolls.
     */
    protected static final PotionEffect NIGHT_VISION_POTION_EFFECT = new PotionEffect(PotionEffectType.NIGHT_VISION, 240, 0, false);

    private final List<String> cachedKey;
    List<Component> cachedLore = null;
    /**
     * Whether this scroll is a reward from the Ender Dragon.
     */
    protected boolean enderDragonReward = false;
    /**
     * Whether this scroll is classified as special (boss drops, cannot be purchased from traveling merchant)
     */
    protected boolean special = false;

    /**
     * Constructs a new ItemScroll.
     * @param key unique scroll key
     * @param name scroll display name
     * @param lore lore lines
     */
    public ItemScroll(final NamespacedKey key, final String name, final String[] lore) {
        super(key);
        this.cachedKey = List.of(this.key.toString());
        if (lore != null) {
            this.cachedLore = new ArrayList<>(lore.length + 1);
            this.cachedLore.add(Component.text(name, NamedTextColor.LIGHT_PURPLE));
            for (String loreElem : lore) {
                this.cachedLore.add(Component.text(loreElem, NamedTextColor.GRAY, TextDecoration.ITALIC));
            }
        }
    }

    /**
     * Creates an ItemStack of this scroll.
     * @param amount stack size
     * @return the created ItemStack
     */
    @Override
    public ItemStack createItemStack(final int amount) {
        final ItemStack itemStack = new ItemStack(Material.PAPER, 1);
        final ItemMeta itemMeta = BukkitUtil.getItemMeta(itemStack);
        final CustomModelDataComponent modelData = itemMeta.getCustomModelDataComponent();
        modelData.setFlags(CACHED_FLAGS);
        modelData.setStrings(this.cachedKey);
        itemMeta.setCustomModelDataComponent(modelData);
        itemMeta.setMaxStackSize(1);
        itemMeta.displayName(CACHED_DISPLAY_NAME);
        if (this.cachedLore != null) {
            itemMeta.lore(this.cachedLore);
        }
        itemMeta.setEnchantmentGlintOverride(true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Checks whether the scroll is currently equipped by a player.
     * @param player player to check
     * @return true if equipped, false otherwise.
     */
    public boolean isEquipping(final Player player) {
        return PlayerDataHandler.hasEquippedScroll(player, this);
    }

    /**
     * Schedules a repeating task for all players equipping this scroll.
     * @param task task to run per player
     * @param period repetition period in ticks
     */
    protected void scheduleRepeatingTaskPerEquippingPlayer(final Consumer<Player> task, final long period) {
        plugin().scheduleTask(
                (final BukkitScheduler scheduler) -> scheduler.scheduleSyncRepeatingTask(
                        plugin(),
                        () -> plugin()
                                .getServer()
                                .getOnlinePlayers()
                                .stream()
                                .filter(this::isEquipping)
                                .forEach(task),
                        0L,
                        period
                )
        );
    }

    /**
     * Applies or removes attribute modifiers for equipping players based on a condition.
     * @param attributeModifierDescriptors set of attribute and modifier pairs
     * @param condition predicate to test players
     * @param extraTaskPerEquippingPlayer optional task per player, null for no task
     */
    protected void modifyAttributesOfEquippingPlayers(final Set<Pair<Attribute, AttributeModifier>> attributeModifierDescriptors, final Predicate<Player> condition, final Consumer<Player> extraTaskPerEquippingPlayer) {
        plugin().scheduleTask((final BukkitScheduler scheduler) ->
                scheduler.scheduleSyncRepeatingTask(plugin(), () ->
                        plugin().getServer().getOnlinePlayers().forEach((final Player onlinePlayer) -> {
                            if (this.isEquipping(onlinePlayer) && (condition != null && condition.test(onlinePlayer))) {
                                attributeModifierDescriptors.forEach((final Pair<Attribute, AttributeModifier> attributeModifierDescriptor) -> {
                                    final AttributeInstance attributeInstance = BukkitUtil.getAttributeInstance(
                                            onlinePlayer,
                                            attributeModifierDescriptor.getA()
                                    );
                                    final AttributeModifier attributeModifier = attributeModifierDescriptor.getB();
                                    if (!BukkitUtil.hasAttributeModifier(attributeInstance, attributeModifier.getKey())) {
                                        attributeInstance.addTransientModifier(attributeModifier);
                                    }
                                    if (extraTaskPerEquippingPlayer != null) {
                                        extraTaskPerEquippingPlayer.accept(onlinePlayer);
                                    }
                                });
                            } else {
                                attributeModifierDescriptors.forEach((final Pair<Attribute, AttributeModifier> attributeModifierDescriptor) ->
                                        BukkitUtil.getAttributeInstance(onlinePlayer, attributeModifierDescriptor.getA()).removeModifier(attributeModifierDescriptor.getB())
                                );
                            }
                        }),
                        0L,
                        1L
                )
        );
    }

    /**
     * Adds a potion effect to players equipping this scroll, if condition passes.
     * @param potionEffect effect to apply
     * @param condition predicate to test players
     */
    protected void addPotionEffectToEquippingPlayers(final PotionEffect potionEffect, final Predicate<Player> condition) {
        this.scheduleRepeatingTaskPerEquippingPlayer((final Player equippingPlayer) -> {
            if (!condition.test(equippingPlayer)) {
                return;
            }
            equippingPlayer.addPotionEffect(potionEffect);
        }, 20L);
    }

    /**
     * Cancels block consumption on placement based on scroll and chance.
     * @param event placement event
     * @param blockTypes block types to affect
     * @param prob probability to reduce consumption
     */
    protected void reduceConsumedPlacedBlocks(final BlockPlaceEvent event, final Set<Material> blockTypes, final double prob) {
        final Player placingPlayer = event.getPlayer();
        if (
                placingPlayer.getGameMode().equals(GameMode.CREATIVE) ||
                !this.isEquipping(placingPlayer) ||
                !blockTypes.contains(event.getBlock().getType()) ||
                plugin().getUniversalRandom().nextDouble() > prob
        ) {
            return;
        }
        event.getItemInHand().add();
    }

    /**
     * Applies damage bonuses or reductions against specific entity types.
     * @param event damage event
     * @param entityTypes affected entity types
     * @param bonus bonus or reduction multiplier
     */
    protected void applyBonusAgainstEntityTypes(final EntityDamageByEntityEvent event, final Set<EntityType> entityTypes, final double bonus) {
        final Entity damaged = event.getEntity();
        final Entity damager = event.getDamager();
        if (entityTypes.contains(damaged.getType()) && (damager instanceof Player damagerPlayer)) {
            if (!this.isEquipping(damagerPlayer)) {
                return;
            }
            event.setDamage(event.getDamage() + (event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE) * bonus));
            damaged.getWorld().spawnParticle(Particle.ENCHANTED_HIT, ((LivingEntity) damaged).getEyeLocation(), 10, 0.5, 0.5, 0.5, 0.1);
        } else if (entityTypes.contains(damager.getType()) && (damaged instanceof Player damagedPlayer)) {
            if (!this.isEquipping(damagedPlayer)) {
                return;
            }
            event.setDamage(event.getDamage() - (event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE) * bonus));
        }
    }

    /**
     * Cancels a specific potion effect if the player is equipping this scroll.
     * @param event potion effect event
     * @param potionEffectType type of effect to cancel
     */
    protected void negatePotionEffectType(final EntityPotionEffectEvent event, final PotionEffectType potionEffectType) {
        if (!(event.getEntity() instanceof Player affectedPlayer)) {
            return;
        }
        final PotionEffect newPotionEffect = event.getNewEffect();
        if (newPotionEffect == null || !newPotionEffect.getType().equals(potionEffectType) || !this.isEquipping(affectedPlayer)) {
            return;
        }
        event.setCancelled(true);
    }

    /**
     * Creates a namespaced subkey under this scroll's key.
     * @param id subkey identifier
     * @return a derived namespaced key
     */
    protected NamespacedKey createSubkey(final String id) {
        return fromAncientScrollsNamespace(this.key.getKey() + "/" + id);
    }

    /**
     * Creates an ItemStack with additional lore about scroll generation.
     * @param amount stack size
     * @return the created ItemStack
     * @see ItemScroll#createItemStack(int)
     */
    public ItemStack createItemStackWithGenerationInfo(final int amount) {
        final ItemStack itemStack = this.createItemStack(amount);
        final ItemMeta itemMeta = BukkitUtil.getItemMeta(itemStack);
        final List<Component> lore = itemMeta.hasLore() ? new ArrayList<>(Objects.requireNonNull(itemMeta.lore())) : new ArrayList<>();
        if (this.enderDragonReward) {
            lore.add(Component.text("Ender Dragon reward", NamedTextColor.DARK_PURPLE));
        }
        if (this.special) {
            lore.add(Component.text("Special", NamedTextColor.YELLOW));
        }
        if (!this.lootTableGenProbs.isEmpty()) {
            lore.add(Component.text("Basic generation", NamedTextColor.GREEN));
            this.lootTableGenProbs.forEach((final NamespacedKey key, final Double prob) ->
                    lore.add(Component.join(
                            JoinConfiguration.noSeparators(),
                            Component.text("- " + key.toString(), NamedTextColor.DARK_GREEN),
                            formatGenerationProb(prob)
                    ))
            );
        }
        if (this.vaultGenProb != null) {
            lore.add(Component.join(
                    JoinConfiguration.noSeparators(),
                    Component.text("Vaults", NamedTextColor.GOLD),
                    formatGenerationProb(this.vaultGenProb)
            ));
        }
        if (this.ominousVaultGenProb != null) {
            lore.add(Component.join(
                    JoinConfiguration.noSeparators(),
                    Component.text("Ominous Vaults", NamedTextColor.AQUA),
                    formatGenerationProb(this.ominousVaultGenProb)
            ));
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Returns whether this scroll is an Ender Dragon reward.
     * @return true if so, false otherwise
     */
    public boolean isEnderDragonReward() {
        return this.enderDragonReward;
    }

    /**
     * Returns whether this scroll is special (boss drop, cannot be purchased from traveling merchants)
     * @return true if so, false otherwise.
     */
    public boolean isSpecial() {
        return this.special;
    }

    /**
     * Checks whether an ItemStack is a valid scroll ItemStack.
     * @param itemStack the ItemStack to check
     * @return true if it's a scroll, false otherwise
     */
    public static boolean is(final ItemStack itemStack) {
        if (!itemStack.getType().equals(Material.PAPER)) {
            return false;
        }
        final ItemMeta itemMeta = BukkitUtil.getItemMeta(itemStack);
        if (!itemMeta.hasCustomModelDataComponent()) {
            return false;
        }
        final List<Boolean> modelDataFlags = itemMeta.getCustomModelDataComponent().getFlags();
        return !modelDataFlags.isEmpty() && modelDataFlags.getFirst();
    }

    /**
     * Extracts the scroll key from an ItemStack.
     * @param scrollItemStack the scroll ItemStack
     * @return the extracted key, or null if not a scroll ItemStack
     */
    public static NamespacedKey getKey(final ItemStack scrollItemStack) {
        final List<String> scrollModelDataStrings = BukkitUtil.getItemMeta(scrollItemStack).getCustomModelDataComponent().getStrings();
        return scrollModelDataStrings.isEmpty() ? null : NamespacedKey.fromString(scrollModelDataStrings.getFirst());
    }

    /**
     * Creates a list of all registered ItemScrolls.
     * @return unmodifiable list of registered scrolls
     */
    public static List<ItemScroll> createListOfAllRegistered() {
        return plugin()
                .getItemRegistry()
                .getAll()
                .parallelStream()
                .filter((final AncientScrollsItem item) -> item instanceof ItemScroll)
                .map((final AncientScrollsItem item) -> (ItemScroll) item)
                .toList();
    }

    private static Component formatGenerationProb(final double prob) {
        return Component.text(" = " + String.format("%.1f%%", prob * 100.0), NamedTextColor.WHITE);
    }
}
