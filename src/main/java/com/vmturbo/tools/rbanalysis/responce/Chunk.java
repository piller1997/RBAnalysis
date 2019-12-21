package com.vmturbo.tools.rbanalysis.responce;

import java.util.Collection;

import com.google.gson.annotations.SerializedName;

/**
 * Diff's chunk.
 */
public class Chunk {
    @SerializedName("lines")
    private final Collection<FileDiffLine> lines;

    @SerializedName("change")
    private final String change;

    /**
     * Creates {@link Chunk} instance.
     *
     * @param lines lines.
     * @param change change's type.
     */
    public Chunk(Collection<FileDiffLine> lines, String change) {
        this.lines = lines;
        this.change = change;
    }

    /**
     * Gets lines.
     *
     * @return lines.
     */
    public Collection<FileDiffLine> getLines() {
        return lines;
    }

    /**
     * Gets change's type.
     *
     * @return change's type.
     */
    public String getChange() {
        return change;
    }
}
