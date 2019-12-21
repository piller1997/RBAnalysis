package com.vmturbo.tools.rbanalysis.responce;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.vmturbo.mediation.connector.common.Response;

/**
 * Response from get token POST call.
 */
@Immutable
public class FileResponse implements Response {
    @Nonnull
    private final String text;

    /**
     * Creates of {@link FileResponse}.
     *
     * @param text Text of file.
     */
    public FileResponse(@Nonnull String text) {
        this.text = Objects.requireNonNull(text);
    }

    /**
     * Gets text of file.
     *
     * @return text.
     */
    @Nonnull
    public String getText() {
        return text;
    }
}
