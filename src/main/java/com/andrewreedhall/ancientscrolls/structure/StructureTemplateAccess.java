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
import org.bukkit.util.BlockTransformer;
import org.bukkit.util.EntityTransformer;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public interface StructureTemplateAccess {
    Set<BlockTransformer> EMPTY_BLOCK_TRANSFORMERS = Set.of();
    Set<EntityTransformer> EMPTY_ENTITY_TRANSFORMERS = Set.of();

    AncientScrollsStructure getStructure();

    default void load() {
        final String structureID = this.getStructure().key.getKey();
        try (final InputStream structureNBTIn = this.getClass().getClassLoader().getResourceAsStream("structures/" + structureID + ".nbt")) {
            this.getStructure().structureTemplate.load(
                    ((CraftServer) plugin().getServer()).getServer().registryAccess().lookup(Registries.BLOCK).get(),
                    NbtIo.readCompressed(Objects.requireNonNull(structureNBTIn), NbtAccounter.unlimitedHeap())
            );
        } catch (final IOException e) {
            plugin().getLogger().log(Level.SEVERE, "Could not get NBT input stream for structure " + structureID, e);
        }
    }

    default void place(
            final ServerLevel level,
            final int blockX,
            final int blockY,
            final int blockZ,
            final Random random
    ) {
        // Reference: https://github.com/PaperMC/Paper/blob/main/paper-server/src/main/java/org/bukkit/craftbukkit/structure/CraftStructure.java
        final AncientScrollsStructure structure = this.getStructure();
        final StructureTemplate structureTemplate = structure.structureTemplate;
        final BlockPos position = new BlockPos(blockX, blockY, blockZ);
        final TransformerGeneratorAccess levelAccess = new TransformerGeneratorAccess();
        levelAccess.setDelegate(level);
        levelAccess.setStructureTransformer(new CraftStructureTransformer(
                level,
                new ChunkPos(position),
                structure.blockTransformers == null ? EMPTY_BLOCK_TRANSFORMERS : structure.blockTransformers,
                structure.entityTransformers == null ? EMPTY_ENTITY_TRANSFORMERS : structure.entityTransformers
        ));
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
