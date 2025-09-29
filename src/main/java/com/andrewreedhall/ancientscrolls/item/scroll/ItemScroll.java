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

import com.andrewreedhall.ancientscrolls.util.PlayerDataHandler;
import com.andrewreedhall.ancientscrolls.item.AncientScrollsItem;
import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
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
import static org.bukkit.ChatColor.*;

public abstract class ItemScroll extends AncientScrollsItem {
    private static final List<Boolean> CACHED_FLAGS = List.of(true);
    private static final String CACHED_DISPLAY_NAME = GOLD + "Ancient Scroll";
    protected static final PotionEffect NIGHT_VISION_POTION_EFFECT = new PotionEffect(PotionEffectType.NIGHT_VISION, 240, 0, false);

    private final List<String> cachedKey;
    private final List<String> cachedLore;
    protected boolean enderDragonReward = false;
    protected boolean special = false;

    public ItemScroll(final NamespacedKey key, final String name, final String[] lore) {
        super(key);
        this.cachedKey = List.of(this.key.toString());
        this.cachedLore = new ArrayList<>(lore.length + 1);
        this.cachedLore.add(LIGHT_PURPLE + name);
        for (String loreElem : lore) {
            this.cachedLore.add(GRAY + ITALIC.toString() + loreElem);
        }
    }

    @Override
    public ItemStack createItemStack(int amount) {
        final ItemStack itemStack = new ItemStack(Material.PAPER, amount);
        ItemMeta itemMeta = BukkitUtil.getItemMeta(itemStack);
        final CustomModelDataComponent modelData = itemMeta.getCustomModelDataComponent();
        modelData.setFlags(CACHED_FLAGS);
        modelData.setStrings(this.cachedKey);
        itemMeta.setCustomModelDataComponent(modelData);
        itemMeta.setMaxStackSize(1);
        itemMeta.setDisplayName(CACHED_DISPLAY_NAME);
        itemMeta.setLore(this.cachedLore);
        itemMeta.setEnchantmentGlintOverride(true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public boolean isEquipping(final Player player) {
        return PlayerDataHandler.hasEquippedScroll(player, this);
    }

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

    protected void addPotionEffectToEquippingPlayers(final PotionEffect potionEffect, final Predicate<Player> condition) {
        this.scheduleRepeatingTaskPerEquippingPlayer((final Player equippingPlayer) -> {
            if (!condition.test(equippingPlayer)) {
                return;
            }
            equippingPlayer.addPotionEffect(potionEffect);
        }, 20L);
    }

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

    protected NamespacedKey createSubkey(final String id) {
        return fromAncientScrollsNamespace(this.key.getKey() + "/" + id);
    }

    public ItemStack createItemStackWithGenerationInfo(final int amount) {
        final ItemStack itemStack = this.createItemStack(amount);
        final ItemMeta itemMeta = BukkitUtil.getItemMeta(itemStack);
        final List<String> lore = itemMeta.hasLore() ? new ArrayList<>(Objects.requireNonNull(itemMeta.getLore())) : new ArrayList<>();
        if (this.enderDragonReward) {
            lore.add(DARK_PURPLE + "Ender Dragon reward");
        }
        if (this.special) {
            lore.add(YELLOW + "Special");
        }
        if (!this.lootTableGenProbs.isEmpty()) {
            lore.add(GREEN + "Basic generation:");
            this.lootTableGenProbs.forEach((final NamespacedKey key, final Double prob) ->
                    lore.add(DARK_GREEN + "- " + key.toString() + WHITE + " = " + formatGenerationProb(prob))
            );
        }
        if (this.vaultGenProb != null) {
            lore.add(GOLD + "Vaults" + WHITE + " = " + formatGenerationProb(this.vaultGenProb));
        }
        if (this.ominousVaultGenProb != null) {
            lore.add(AQUA + "Ominous Vaults" + WHITE + " = " + formatGenerationProb(this.ominousVaultGenProb));
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public boolean isEnderDragonReward() {
        return this.enderDragonReward;
    }

    public boolean isSpecial() {
        return this.special;
    }

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

    public static NamespacedKey getKey(final ItemStack scrollItemStack) {
        final List<String> scrollModelDataStrings = BukkitUtil.getItemMeta(scrollItemStack).getCustomModelDataComponent().getStrings();
        return scrollModelDataStrings.isEmpty() ? null : NamespacedKey.fromString(scrollModelDataStrings.getFirst());
    }

    public static List<ItemScroll> createListOfAllRegistered() {
        return plugin()
                .getItemRegistry()
                .getAll()
                .parallelStream()
                .filter((final AncientScrollsItem item) -> item instanceof ItemScroll)
                .map((final AncientScrollsItem item) -> (ItemScroll) item)
                .toList();
    }

    private static String formatGenerationProb(final double prob) {
        return String.format("%.1f%%", prob * 100.0);
    }

    public static ItemScroll get(final NamespacedKey key) {
        final AncientScrollsItem scroll = plugin().getItemRegistry().get(key);
        return Objects.requireNonNull(scroll instanceof ItemScroll ? (ItemScroll) scroll : null, key + " is not an ItemScroll");
    }
}
