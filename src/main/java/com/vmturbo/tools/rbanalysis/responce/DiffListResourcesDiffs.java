package com.vmturbo.tools.rbanalysis.responce;

import javax.annotation.Nonnull;

import com.google.gson.annotations.SerializedName;

/**
 * Response from get token POST call.
 */
public class DiffListResourcesDiffs {
    @Nonnull
    @SerializedName("revision")
    private final int revision;

    /**
     * Creates instance of {@link DiffListResourcesDiffs}.
     *
     * @param revision diff's revision.
     */
    public DiffListResourcesDiffs(@Nonnull int revision) {
        this.revision = revision;
    }

    /**
     * Gets diff's revision.
     *
     * @return diff's revision
     */
    @Nonnull
    public int getRevision() {
        return revision;
    }

    /**
     * Checking on equals.
     *
     * @param o
     * @return equals.
     */
    @Override
    public boolean equals(Object o) {
        return revision == revision;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
