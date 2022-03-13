package com.stevekung.cncblending;

import java.util.Optional;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.Codec;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import net.minecraft.world.level.storage.DimensionDataStorage;

@Mixin(value = ChunkStorage.class, priority = 1200)
public class MixinChunkStorage
{
    private static final int JE_1_17 = 2724;
    private static final int JE_1_18_EXPERIMENTAL_7 = 2831;

    @Inject(method = "upgradeChunkTag", at = @At(value = "INVOKE", target = "net/minecraft/nbt/CompoundTag.putInt(Ljava/lang/String;I)V"))
    private void addBlendingDataTag(ResourceKey<Level> levelKey, Supplier<DimensionDataStorage> storage, CompoundTag chunkData, Optional<ResourceKey<Codec<? extends ChunkGenerator>>> optional, CallbackInfoReturnable<CompoundTag> info)
    {
        var version = ChunkStorage.getVersion(chunkData);

        if (version >= JE_1_17 && version <= JE_1_18_EXPERIMENTAL_7 && levelKey == Level.OVERWORLD)
        {
            if (!chunkData.contains("blending_data"))
            {
                var compoundTag = new CompoundTag();
                compoundTag.putBoolean("old_noise", true);
                chunkData.put("blending_data", compoundTag);
            }
            else if (!chunkData.contains("yPos"))
            {
                chunkData.putInt("yPos", -4);
            }
        }
    }
}