package com.vmturbo.tools.rbanalysis.checkstyle;

/**
 * Checkstyle error for sending on the server.
 */
public class CheckstyleError {
    private final int line;
    private final String message;

    /**
     * Creates {@link CheckstyleError} instance.
     *
     * @param line Line of error.
     * @param message Message of error.
     */
    public CheckstyleError(int line, String message) {
        this.line = line;
        this.message = message;
    }

    /**
     * Gets line of error.
     *
     * @return line of error.
     */
    public int getLine() {
        return line;
    }

    /**
     * Gets message of error.
     *
     * @return message of error.
     */
    public String getMessage() {
        return message;
    }
}
