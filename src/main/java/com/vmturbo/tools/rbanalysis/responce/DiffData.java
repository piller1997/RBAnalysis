package com.vmturbo.tools.rbanalysis.responce;

import java.util.Collection;

import com.google.gson.annotations.SerializedName;

/**
 * Diff data from response.
 */
public class DiffData {
    @SerializedName("changed_chunk_indexes")
    private final Collection<Integer> changedChunkIndexes;

    @SerializedName("chunks")
    private final Collection<Chunk> chunks;

    /**
     * Creates {@link DiffData} instance.
     *
     * @param changedChunkIndexes Collection of chunk indexes.
     * @param chunks Collection of chunks.
     */
    public DiffData(Collection<Integer> changedChunkIndexes,
            Collection<Chunk> chunks) {
        this.changedChunkIndexes = changedChunkIndexes;
        this.chunks = chunks;
    }

    /**
     * Gets collection of chunk indexes.
     *
     * @return Collection of chunk indexes.
     */
    public Collection<Integer> getChangedChunkIndexes() {
        return changedChunkIndexes;
    }

    /**
     * Gets collection of chunks.
     *
     * @return Collection of chunks.
     */
    public Collection<Chunk> getChunks() {
        return chunks;
    }
}
