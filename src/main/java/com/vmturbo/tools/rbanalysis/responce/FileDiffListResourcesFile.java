package com.vmturbo.tools.rbanalysis.responce;

import javax.annotation.Nonnull;

import com.google.gson.annotations.SerializedName;

/**
 * Response from get token POST call.
 */
public class FileDiffListResourcesFile {
    @Nonnull
    @SerializedName("status")
    private final String status;
    @Nonnull
    @SerializedName("id")
    private final int id;
    @Nonnull
    @SerializedName("source_revision")
    private final String sourceRevision;
    @Nonnull
    @SerializedName("dest_file")
    private final String destFile;

    /**
     * Creates of {@link FileDiffListResourcesFile} instance.
     *
     * @param status diff's status
     * @param id diff's id
     * @param sourceRevision diff's revision
     * @param destFile diff's destination file
     */
    public FileDiffListResourcesFile(@Nonnull String status, @Nonnull int id,
            @Nonnull String sourceRevision, @Nonnull String destFile) {
        this.status = status;
        this.id = id;
        this.sourceRevision = sourceRevision;
        this.destFile = destFile;
    }

    /**
     * Gets diff's status.
     *
     * @return diff's status.
     */
    @Nonnull
    public String getStatus() {
        return status;
    }

    /**
     * Gets diff's id.
     *
     * @return id
     */
    @Nonnull
    public int getId() {
        return id;
    }

    /**
     * Gets diff's source revision.
     *
     * @return Source revision.
     */
    @Nonnull
    public String getSourceRevision() {
        return sourceRevision;
    }

    /**
     * Gets diff's destination file.
     *
     * @return destination file
     */
    @Nonnull
    public String getDestFile() {
        return destFile;
    }
}
