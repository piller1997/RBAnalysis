package com.vmturbo.tools.rbanalysis.queries;

import java.util.Collections;

import javax.annotation.Nonnull;

import com.vmturbo.mediation.connector.common.HttpMethodType;
import com.vmturbo.tools.rbanalysis.Properties;
import com.vmturbo.tools.rbanalysis.responce.FileResponse;

/**
 * Executes GET HTTP requests to the RB instance to get the patched file
 * corresponding to a file diff.
 */
public class PatchedFileQuery extends GetQuery<FileResponse> {
    /**
     * Creates {@link PatchedFileQuery} instance.
     *
     * @param reviewRequestId review request's id
     * @param diffRevision diff's revision
     * @param fileDiffId file diff's id
     * @param properties program's properties
     */
    public PatchedFileQuery(int reviewRequestId, int diffRevision, int fileDiffId,
            @Nonnull Properties properties) {
        super(HttpMethodType.GET,
                String.format("review-requests/%d/diffs/%d/files/%d/patched-file/", reviewRequestId,
                        diffRevision, fileDiffId), null, FileResponse.class,
                Collections.emptyMap(), properties);
    }
}
