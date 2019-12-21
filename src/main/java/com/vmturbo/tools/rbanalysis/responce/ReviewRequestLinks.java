package com.vmturbo.tools.rbanalysis.responce;

import javax.annotation.Nonnull;

import com.google.gson.annotations.SerializedName;

/**
 * Part of response from get token POST call.
 */
public class ReviewRequestLinks {
    @Nonnull
    @SerializedName("repository")
    private final ReviewRequestRepositoryLink repository;

    /**
     * Constructs revuew request links object.
     *
     * @param repository repository reference.
     */
    public ReviewRequestLinks(@Nonnull ReviewRequestRepositoryLink repository) {
        this.repository = repository;
    }

    /**
     * Gets repository link of RR.
     *
     * @return repository link
     */
    @Nonnull
    public ReviewRequestRepositoryLink getRepository() {
        return repository;
    }
}
