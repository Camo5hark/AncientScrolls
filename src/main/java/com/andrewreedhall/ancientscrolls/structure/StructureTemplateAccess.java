package com.andrewreedhall.ancientscrolls.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftStructureTransformer;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.craftbukkit.util.TransformerGeneratorAccess;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public interface StructureTemplateAccess {
    default StructureTemplate load(final String structureID) {
        final StructureTemplate structureTemplate = new StructureTemplate();
        try (final InputStream structureNBTIn = this.getClass().getClassLoader().getResourceAsStream("structures/" + structureID + ".nbt")) {
            structureTemplate.load(
                    ((CraftServer) plugin().getServer()).getServer().registryAccess().lookup(Registries.BLOCK).get(),
                    NbtIo.readCompressed(Objects.requireNonNull(structureNBTIn), NbtAccounter.unlimitedHeap())
            );
        } catch (final IOException e) {
            plugin().getLogger().log(Level.SEVERE, "Could not get NBT input stream for structure " + structureID, e);
        }
        return structureTemplate;
    }

    default void place(
            final StructureTemplate structureTemplate,
            final ServerLevel level,
            final int blockX,
            final int blockY,
            final int blockZ,
            final Random random
    ) {
        // Reference: https://github.com/PaperMC/Paper/blob/main/paper-server/src/main/java/org/bukkit/craftbukkit/structure/CraftStructure.java
        final BlockPos position = new BlockPos(blockX, blockY, blockZ);
        final TransformerGeneratorAccess levelAccess = new TransformerGeneratorAccess();
        levelAccess.setDelegate(level);
        levelAccess.setStructureTransformer(new CraftStructureTransformer(level, new ChunkPos(position), Set.of(), Set.of()));
        final RandomSource randomSource = new RandomSourceWrapper(random);
        final boolean placed = structureTemplate.placeInWorld(
                levelAccess,
                position,
                position,
                new StructurePlaceSettings()
                        .setRotation(Rotation.getRandom(randomSource))
                        .setMirror(Mirror.NONE)
                        .setIgnoreEntities(false)
                        .setFinalizeEntities(true)
                        .setKnownShape(true)
                        .setLiquidSettings(LiquidSettings.APPLY_WATERLOGGING)
                        .setRandom(randomSource),
                randomSource,
                2
        );
        levelAccess.getStructureTransformer().discard();
        if (!placed) {
            plugin().getLogger().warning("Failed to place structure " + structureTemplate + " in " + level + " at [" + blockX + ", " + blockY + ", " + blockZ + "]");
        }
    }
}
