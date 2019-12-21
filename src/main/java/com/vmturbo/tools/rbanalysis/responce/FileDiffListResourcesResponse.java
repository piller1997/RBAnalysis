package com.vmturbo.tools.rbanalysis.responce;

import java.util.Collection;

import javax.annotation.Nonnull;

import com.google.gson.annotations.SerializedName;

import com.vmturbo.mediation.connector.common.Response;

/**
 * Response from get token POST call.
 */
public class FileDiffListResourcesResponse implements Response {
    @Nonnull
    @SerializedName("files")
    private final Collection<FileDiffListResourcesFile> files;

    /**
     * Creates {@link FileDiffListResourcesResponse} instance.
     *
     * @param files Collection of {@link FileDiffListResourcesFile}
     */
    public FileDiffListResourcesResponse(
            @Nonnull Collection<FileDiffListResourcesFile> files) {
        this.files = files;
    }

    /**
     * Gets collection of files.
     *
     * @return collection of files.
     */
    @Nonnull
    public Collection<FileDiffListResourcesFile> getFiles() {
        return files;
    }
}
