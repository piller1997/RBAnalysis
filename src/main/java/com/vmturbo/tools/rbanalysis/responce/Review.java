package com.vmturbo.tools.rbanalysis.responce;

import javax.annotation.Nonnull;

import com.google.gson.annotations.SerializedName;

/**
 * Review, that contains comments.
 */
public class Review {
    @Nonnull
    @SerializedName("id")
    private final int id;

    /**
     * Creates {@link Review} instance.
     *
     * @param id id of review.
     */
    public Review(int id) {
        this.id = id;
    }

    /**
     * Gets review's id.
     *
     * @return id.
     */
    public int getId() {
        return id;
    }
}
