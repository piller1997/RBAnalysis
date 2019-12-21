package com.vmturbo.tools.rbanalysis.queries;

import javax.annotation.Nonnull;

import com.vmturbo.mediation.connector.common.http.HttpParameter;

/**
 * HTTP Parameters for HTTP Query or HTTP Body.
 */
public enum HttpParameters implements HttpParameter {
    FILEDIFF_ID("filediff_id"), FIRST_LINE("first_line"), ISSUE_OPENED("issue_opened"),
    NUM_LINES("num_lines"), MAX_RESULTS("max-results"), START("start"), TEXT("text");

    private final String parameterId;

    /**
     * Creates {@link HttpParameters} instance.
     *
     * @param parameterId parameter id.
     */
    HttpParameters(String parameterId) {
        this.parameterId = parameterId;
    }

    /**
     * Gets parameter id.
     *
     * @return parameter id.
     */
    @Nonnull
    @Override
    public String getParameterId() {
        return parameterId;
    }
}
