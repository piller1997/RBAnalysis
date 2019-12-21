package com.vmturbo.tools.rbanalysis.responce;

import javax.annotation.Nonnull;

import com.google.gson.annotations.SerializedName;

/**
 * Part of response from get token POST call.
 */
public class ReviewRequestRepositoryLink {
    @Nonnull
    @SerializedName("title")
    private final String repository;

    /**
     * Constructs review request repository link.
     *
     * @param repository repository
     */
    public ReviewRequestRepositoryLink(@Nonnull String repository) {
        this.repository = repository;
    }

    /**
     * Gets repository name of RR.
     *
     * @return repository title.
     */
    @Nonnull
    public String getRepositoryTitle() {
        return repository;
    }
}
