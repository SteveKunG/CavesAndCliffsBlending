package com.stevekung.cncblending;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.BelowZeroRetrogen;

@Mixin(ProtoChunk.class)
public abstract class MixinProtoChunk extends ChunkAccess
{
    @Shadow
    BelowZeroRetrogen belowZeroRetrogen;

    MixinProtoChunk()
    {
        super(null, null, null, null, 0, null, null);
    }

    // Not sure what exactly happened here when it crashed, occasionally happens in some world. Just prevent crash to occur.
    @Override
    public Holder<Biome> getNoiseBiome(int i, int j, int k)
    {
        if (this.getStatus().isOrAfter(ChunkStatus.BIOMES) || this.belowZeroRetrogen != null && this.belowZeroRetrogen.targetStatus().isOrAfter(ChunkStatus.BIOMES))
        {
            return super.getNoiseBiome(i, j, k);
        }
        //LogManager.getLogger().error("Asking for biomes before we have biomes at: {} {} {}, status: {}", i, j, k, this.getStatus());
        return super.getNoiseBiome(i, j, k);
        //throw new IllegalStateException("Asking for biomes before we have biomes");
    }
}