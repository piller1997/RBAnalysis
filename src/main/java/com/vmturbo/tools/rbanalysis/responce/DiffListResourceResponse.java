package com.vmturbo.tools.rbanalysis.responce;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.Nonnull;

import com.google.gson.annotations.SerializedName;

import com.vmturbo.mediation.connector.common.Response;

/**
 * Response from get token POST call.
 */
public class DiffListResourceResponse implements Response {
    @Nonnull
    @SerializedName("diffs")
    private final Collection<DiffListResourcesDiffs> diffs;

    /**
     * Create {@link DiffListResourceResponse} instance.
     *
     * @param diffs collection of Diff resource diffs
     */
    public DiffListResourceResponse(
            @Nonnull Collection<DiffListResourcesDiffs> diffs) {
        this.diffs = diffs;
    }

    /**
     * Gets diff list resources collection.
     *
     * @return list resources collection.
     */
    @Nonnull
    public Collection<DiffListResourcesDiffs> getDiffListResourcesDiffs() {
        return diffs;
    }

    /**
     * Returns max revision from all revisions from response.
     *
     * @return max revision
     * @throws Exception Throws then revisions are empty.
     */
    @Nonnull
    public int getMaxRevision() throws Exception {
        if (diffs.isEmpty()) {
            throw new Exception("Review request's revisions are empty.");
        } else {
            return Collections.max(diffs,
                    Comparator.comparingInt(DiffListResourcesDiffs::getRevision))
                    .getRevision();
        }
    }
}
