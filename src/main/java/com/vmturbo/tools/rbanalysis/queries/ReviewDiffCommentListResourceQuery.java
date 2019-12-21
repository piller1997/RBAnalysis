package com.vmturbo.tools.rbanalysis.queries;

import java.util.Collections;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.vmturbo.mediation.connector.common.HttpBody;
import com.vmturbo.mediation.connector.common.HttpMethodType;
import com.vmturbo.tools.rbanalysis.Properties;
import com.vmturbo.tools.rbanalysis.responce.ReviewDiffCommentListResourceResponce;

/**
 * Executes GET HTTP requests to the RB instance to create comment with body.
 */
public class ReviewDiffCommentListResourceQuery extends
        GetQuery<ReviewDiffCommentListResourceResponce> {

    /**
     * Creates {@link GetQuery} instance.
     *
     * @param body HTTP body.
     * @param properties program's properties.
     * @param idReviewRequest review request's id.
     * @param idReview review's id.
     */
    public ReviewDiffCommentListResourceQuery(@Nonnull HttpBody body,
            @Nonnull Properties properties, int idReviewRequest, int idReview) {
        super(HttpMethodType.POST,
                String.format("review-requests/%d/reviews/%d/diff-comments/", idReviewRequest,
                        idReview), body, ReviewDiffCommentListResourceResponce.class,
                Collections.emptyMap(), properties);
        Objects.requireNonNull(body);
    }
}
