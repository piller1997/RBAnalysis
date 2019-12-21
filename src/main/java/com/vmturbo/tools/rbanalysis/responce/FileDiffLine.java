package com.vmturbo.tools.rbanalysis.responce;

/**
 * Response for file-diff line resource. We are only interested in source (patched file) line number
 * and file-diff line number. The later one is used to correctly locate the comment when posting
 * to review request.
 */
public class FileDiffLine {
    private final int sourceLine;
    private final int fileDiffLine;

    /**
     * Constructs filediff response.
     *
     * @param sourceLine source line number - a line number in the patched file, that has
     *         been changed.
     * @param fileDiffLine file-diff line number - a line number that is used in ReivewBoard
     *         to locate comments in t review request
     */
    public FileDiffLine(int sourceLine, int fileDiffLine) {
        this.sourceLine = sourceLine;
        this.fileDiffLine = fileDiffLine;
    }

    /**
     * Returns line number in the patched source file.
     *
     * @return line number in source file
     */
    public int getSourceLine() {
        return sourceLine;
    }

    /**
     * Returns line number in file-diff, which is used to locate comments in review request UI.
     *
     * @return file-diff line number.
     */
    public int getFileDiffLine() {
        return fileDiffLine;
    }
}
