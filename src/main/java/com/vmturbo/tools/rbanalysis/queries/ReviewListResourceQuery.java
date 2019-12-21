package com.vmturbo.tools.rbanalysis.queries;

import java.util.Collections;

import javax.annotation.Nonnull;

import com.vmturbo.mediation.connector.common.HttpMethodType;
import com.vmturbo.tools.rbanalysis.Properties;
import com.vmturbo.tools.rbanalysis.responce.ReviewListResourceResponse;

/**
 * Executes GET HTTP requests to the RB instance.
 */
public class ReviewListResourceQuery extends GetQuery<ReviewListResourceResponse> {
    /**
     * Creates {@link ReviewListResourceQuery} instance.
     *
     * @param reviewRequestId review request's id
     * @param properties program's properties
     */
    public ReviewListResourceQuery(int reviewRequestId,
            @Nonnull Properties properties) {
        super(HttpMethodType.POST, String.format("review-requests/%d/reviews/", reviewRequestId),
                null, ReviewListResourceResponse.class, Collections.emptyMap(),
                properties);
    }
}
