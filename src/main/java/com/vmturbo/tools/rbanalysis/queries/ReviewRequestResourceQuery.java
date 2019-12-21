package com.vmturbo.tools.rbanalysis.queries;

import java.util.Collections;

import javax.annotation.Nonnull;

import com.vmturbo.mediation.connector.common.HttpMethodType;
import com.vmturbo.tools.rbanalysis.Properties;
import com.vmturbo.tools.rbanalysis.responce.ReviewRequestResponse;

/**
 * Executes GET HTTP requests to the RB instance to get review requests by its id.
 */
public class ReviewRequestResourceQuery extends GetQuery<ReviewRequestResponse> {
    /**
     * Creates {@link GetQuery} instance.
     *
     * @param reviewRequestId Review request's id.
     * @param properties program's properties
     */
    public ReviewRequestResourceQuery(int reviewRequestId,
            @Nonnull Properties properties) {
        super(HttpMethodType.GET, String.format("review-requests/%d/", reviewRequestId), null,
                ReviewRequestResponse.class, Collections.emptyMap(), properties);
    }
}
