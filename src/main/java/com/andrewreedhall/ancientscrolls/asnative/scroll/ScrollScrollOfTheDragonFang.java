package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import org.bukkit.*;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollScrollOfTheDragonFang extends ItemScrollNative implements Listener {
    private static final Set<Material> WEAPON_TYPES = Set.of(
            Material.WOODEN_SWORD,
            Material.STONE_SWORD,
            Material.IRON_SWORD,
            Material.GOLDEN_SWORD,
            Material.DIAMOND_SWORD,

            Material.WOODEN_AXE,
            Material.STONE_AXE,
            Material.IRON_AXE,
            Material.GOLDEN_AXE,
            Material.DIAMOND_AXE,

            Material.MACE
    );
    private static final int N_EVOKER_FANGS_IN_TRAIL = 10;

    private final Map<Player, Integer> evokerFangTrailTTLs = new HashMap<>();

    public ScrollScrollOfTheDragonFang() {
        super("scroll_of_the_dragon_fang", "Scroll of the Dragon Fang", new String[] {
                "Conjure evoker fangs with sword, axe, or mace"
        });
        this.enderDragonReward = true;
        this.special = true;
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }
        final ItemStack interactingItemStack = event.getItem();
        if (interactingItemStack == null || !WEAPON_TYPES.contains(interactingItemStack.getType())) {
            return;
        }
        final Player interactingPlayer = event.getPlayer();
        if (!this.isEquipping(interactingPlayer)) {
            return;
        }
        if (WEAPON_TYPES.stream().anyMatch((final Material weaponType) -> interactingPlayer.getCooldown(weaponType) > 0)) {
            BukkitUtil.playBadSound(interactingPlayer);
            return;
        }
        WEAPON_TYPES.forEach((final Material weaponType) -> interactingPlayer.setCooldown(weaponType, 100));
        final World interactingPlayerWorld = interactingPlayer.getWorld();
        final Location interactingPlayerLocation = interactingPlayer.getLocation();
        interactingPlayerWorld
                .getNearbyEntitiesByType(Monster.class, interactingPlayerLocation, 10, 5, 10)
                .forEach((final Monster nearbyMonster) -> {
                    interactingPlayerWorld.spawn(nearbyMonster.getLocation(), EvokerFangs.class);
                    interactingPlayerWorld.spawnParticle(
                            Particle.DUST,
                            nearbyMonster.getEyeLocation(),
                            10,
                            0.5,
                            0.5,
                            0.5,
                            new Particle.DustOptions(Color.MAROON, 1.5F)
                    );
                });
        interactingPlayerWorld.playSound(interactingPlayer, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 0.75F, 0.75F);
        interactingPlayerWorld.spawnParticle(
                Particle.DUST,
                interactingPlayerLocation,
                100,
                10.0,
                10.0,
                10.0,
                new Particle.DustOptions(Color.FUCHSIA, 1.25F)
        );
        this.evokerFangTrailTTLs.put(interactingPlayer, N_EVOKER_FANGS_IN_TRAIL);
        this.scheduleEvokerFangTrailNext(interactingPlayer);
    }

    private void scheduleEvokerFangTrailNext(final Player player) {
        plugin().scheduleTask((final BukkitScheduler scheduler) -> scheduler.scheduleSyncDelayedTask(
                plugin(),
                () -> {
                    final Integer evokerFangTrailTTL = this.evokerFangTrailTTLs.get(player);
                    if (evokerFangTrailTTL == null || evokerFangTrailTTL == 0) {
                        return;
                    }
                    this.evokerFangTrailTTLs.put(player, evokerFangTrailTTL - 1);
                    final World playerWorld = player.getWorld();
                    final Location playerLocation = player.getLocation();
                    final Vector playerDirectionXZ = playerLocation.getDirection().setY(0.0).normalize();
                    final Location evokerFangLocation = playerLocation
                            .add(
                                    playerDirectionXZ
                                            .clone()
                                            .multiply(N_EVOKER_FANGS_IN_TRAIL - evokerFangTrailTTL + 1)
                            )
                            .add(
                                    playerDirectionXZ
                                            .clone()
                                            .multiply(0.5)
                                            .rotateAroundY(Math.PI * 0.5 * (evokerFangTrailTTL % 2 == 1 ? -1.0 : 1.0))
                            );
                    playerWorld.spawn(evokerFangLocation, EvokerFangs.class);
                    playerWorld.spawnParticle(
                            Particle.DUST,
                            evokerFangLocation.add(0.0, 0.5, 0.0),
                            10,
                            0.5,
                            0.5,
                            0.5,
                            new Particle.DustOptions(Color.fromRGB(plugin().getUniversalRandom().nextInt() & 0xFFFFFF), 1.0F)
                    );
                    this.scheduleEvokerFangTrailNext(player);
                },
                2L
        ));
    }
}
