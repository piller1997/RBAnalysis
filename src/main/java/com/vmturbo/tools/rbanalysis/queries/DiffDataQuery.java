package com.vmturbo.tools.rbanalysis.queries;

import java.util.Collections;

import javax.annotation.Nonnull;

import com.vmturbo.mediation.connector.common.HttpMethodType;
import com.vmturbo.tools.rbanalysis.Properties;
import com.vmturbo.tools.rbanalysis.responce.DiffDataResponse;

/**
 * Executes GET HTTP requests to the RB instance to get diff data.
 */
public class DiffDataQuery extends GetQuery<DiffDataResponse> {
    /**
     * Creates {@link DiffDataQuery} instance.
     *
     * @param properties program's properties.
     * @param reviewRequestId review request's id.
     * @param diffRevision diff's revision.
     * @param filediffId file diff's id.
     */
    public DiffDataQuery(@Nonnull Properties properties, int reviewRequestId,
            int diffRevision, int filediffId) {
        super(HttpMethodType.GET,
                String.format("review-requests/%d/diffs/%d/files/%d/", reviewRequestId,
                        diffRevision, filediffId), null, DiffDataResponse.class,
                Collections.emptyMap(), properties);
    }
}
