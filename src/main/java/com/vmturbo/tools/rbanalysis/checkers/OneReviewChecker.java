package com.vmturbo.tools.rbanalysis.checkers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vmturbo.mediation.connector.common.HttpConnector;
import com.vmturbo.mediation.connector.common.HttpConnectorException;
import com.vmturbo.mediation.connector.common.ParametersBasedHttpBody;
import com.vmturbo.mediation.connector.common.http.HttpParameter;
import com.vmturbo.tools.rbanalysis.Properties;
import com.vmturbo.tools.rbanalysis.checkstyle.Checkstyle;
import com.vmturbo.tools.rbanalysis.checkstyle.CheckstyleError;
import com.vmturbo.tools.rbanalysis.queries.DiffDataQuery;
import com.vmturbo.tools.rbanalysis.queries.DiffListResourceQuery;
import com.vmturbo.tools.rbanalysis.queries.FileDiffListResourceQuery;
import com.vmturbo.tools.rbanalysis.queries.HttpParameters;
import com.vmturbo.tools.rbanalysis.queries.PatchedFileQuery;
import com.vmturbo.tools.rbanalysis.queries.ReviewDiffCommentListResourceQuery;
import com.vmturbo.tools.rbanalysis.queries.ReviewListResourceQuery;
import com.vmturbo.tools.rbanalysis.queries.ReviewRequestResourceQuery;
import com.vmturbo.tools.rbanalysis.responce.Chunk;
import com.vmturbo.tools.rbanalysis.responce.DiffData;
import com.vmturbo.tools.rbanalysis.responce.DiffDataResponse;
import com.vmturbo.tools.rbanalysis.responce.DiffListResourceResponse;
import com.vmturbo.tools.rbanalysis.responce.Exception;
import com.vmturbo.tools.rbanalysis.responce.FileDiffLine;
import com.vmturbo.tools.rbanalysis.responce.FileDiffListResourcesFile;
import com.vmturbo.tools.rbanalysis.responce.FileDiffListResourcesResponse;
import com.vmturbo.tools.rbanalysis.responce.FileResponse;
import com.vmturbo.tools.rbanalysis.responce.ReviewListResourceResponse;
import com.vmturbo.tools.rbanalysis.responce.ReviewRequestResponse;
import com.vmturbo.tools.rbanalysis.responce.ReviewRequests;

/**
 * Review request's checker.
 */
public class OneReviewChecker implements Callable<Integer> {
    private final Logger logger;
    private final int reviewId;
    private static final String NEW_LINE = "\n";
    private static final String DELETED = "deleted";
    private final Properties properties;
    private final HttpConnector connector;
    private final int lastRevision;

    /**
     * Creates {@link OneReviewChecker} instance.
     *
     * @param reviewId review's id.
     * @param properties Program's configuration.
     * @param connector Http connector.
     * @param lastRevision last revision.
     */
    public OneReviewChecker(int reviewId, int lastRevision, @Nonnull Properties properties,
            @Nonnull HttpConnector connector) {
        this.reviewId = reviewId;
        this.properties = Objects.requireNonNull(properties);
        this.connector = Objects.requireNonNull(connector);
        this.lastRevision = lastRevision;
        logger = LogManager.getLogger(getClass());
    }

    /**
     * Runs checking.
     *
     * @return last revision.
     */
    @Override
    public Integer call() {
        try {
            final ReviewRequests reviewRequests = getReviewRequest();
            logger.info("Analyzing review request #" + reviewRequests.getId());
            return checkReviewRequest(reviewRequests);
        } catch (Exception | HttpConnectorException | InterruptedException e) {
            logger.warn("Review request #" + reviewId + " analysis failed", e);
        }

        return -1;
    }

    /**
     * Gets diff list resource by its coordinates.
     *
     * @return Diff list resource.
     * @throws HttpConnectorException Http connector exception.
     * @throws InterruptedException interrupted exception.
     */
    private DiffListResourceResponse getDiffListResource()
            throws HttpConnectorException, InterruptedException {
        final DiffListResourceQuery query = new DiffListResourceQuery(reviewId, properties);
        return connector.execute(query);
    }

    /**
     * Gets review request by its Id.
     *
     * @return review request.
     * @throws HttpConnectorException Http connector exception.
     * @throws InterruptedException Interrupted exception.
     */
    private ReviewRequests getReviewRequest() throws HttpConnectorException, InterruptedException {
        final ReviewRequestResourceQuery query =
                new ReviewRequestResourceQuery(reviewId, properties);
        final ReviewRequestResponse request = connector.execute(query);
        return request.getReviewRequest();
    }

    /**
     * Checks review request.
     *
     * @param reviewRequest review request, that's checks.
     * @return last revision.
     * @throws Exception some exception of analysis.
     * @throws HttpConnectorException Http connector exception.
     * @throws InterruptedException interrupted exception.
     */
    public int checkReviewRequest(@Nonnull ReviewRequests reviewRequest)
            throws Exception, HttpConnectorException, InterruptedException {
        Objects.requireNonNull(reviewRequest);
        final DiffListResourceResponse diffListResourceResponse = getDiffListResource();
        final int maxRevision = diffListResourceResponse.getMaxRevision();
        if (maxRevision > lastRevision) {
            checkDiffFiles(reviewRequest, maxRevision);
            logger.debug("Finished analyzing review request #" + reviewRequest.getId() +
                    " new revision.");
        } else {
            logger.debug("Finished analyzing review request #" + reviewRequest.getId() +
                    ". No new revisions found.");
        }
        return maxRevision;
    }

    /**
     * Checks diff files.
     *
     * @param reviewRequest review request
     * @param maxRevision checks maximum revision
     * @throws HttpConnectorException Http connector exception.
     * @throws InterruptedException interrupted exception.
     */
    private void checkDiffFiles(@Nonnull ReviewRequests reviewRequest, int maxRevision)
            throws HttpConnectorException, InterruptedException {
        Objects.requireNonNull(reviewRequest);
        final FileDiffListResourcesResponse files =
                getFileDiffListResource(reviewRequest.getId(), maxRevision);
        final ReviewListResourceQuery query =
                new ReviewListResourceQuery(reviewRequest.getId(), properties);
        ReviewListResourceResponse review = null;
        for (FileDiffListResourcesFile file : files.getFiles()) {
            final List<CheckstyleError> errors = checkDiff(reviewRequest, maxRevision, file);
            if (!errors.isEmpty()) {
                if (review == null) {
                    connector.execute(query);
                    review = connector.execute(query);
                }
                sendReviewCommentsFromAuditEvents(errors, review, file.getId());
            }
        }
    }

    /**
     * Gets file diff list resources by its coordinates.
     *
     * @param id diff's id.
     * @param revision diff's revision.
     * @return File diff list resources.
     * @throws HttpConnectorException Http connector exception.
     * @throws InterruptedException interrupted exception.
     */
    private FileDiffListResourcesResponse getFileDiffListResource(int id, int revision)
            throws HttpConnectorException, InterruptedException {
        final FileDiffListResourceQuery query =
                new FileDiffListResourceQuery(id, revision, properties);
        return connector.execute(query);
    }

    /**
     * Sends message from list of checkstyle errors.
     *
     * @param errors list of checkstyle errors.
     * @param response review.
     * @param fileDiffId file diff's id.
     * @throws HttpConnectorException Http connector exception.
     * @throws InterruptedException interrupted exception.
     */
    private void sendReviewCommentsFromAuditEvents(@Nonnull List<CheckstyleError> errors,
            @Nonnull ReviewListResourceResponse response, int fileDiffId)
            throws HttpConnectorException, InterruptedException {
        Objects.requireNonNull(errors);
        for (CheckstyleError event : errors) {
            final String message = event.getMessage();
            logger.debug(message);
            addReviewComment(message + NEW_LINE, event.getLine(), response, fileDiffId);
        }
    }

    /**
     * Sends message onto server Review Board.
     *
     * @param message message of comment.
     * @param line Start line.
     * @param response Response.
     * @param fileDiffId file diff's id,
     * @throws HttpConnectorException Http connector exception.
     * @throws InterruptedException interrupted exception.
     */
    private void addReviewComment(@Nonnull String message, int line,
            @Nonnull ReviewListResourceResponse response, int fileDiffId)
            throws HttpConnectorException, InterruptedException {

        Objects.requireNonNull(message);
        Objects.requireNonNull(response);

        final Map<HttpParameter, String> parameters = new HashMap<>();
        parameters.put(HttpParameters.FILEDIFF_ID, String.valueOf(fileDiffId));
        parameters.put(HttpParameters.FIRST_LINE, String.valueOf(line));
        parameters.put(HttpParameters.NUM_LINES, String.valueOf(1));
        parameters.put(HttpParameters.TEXT, message);
        parameters.put(HttpParameters.ISSUE_OPENED, String.valueOf(true));
        final ParametersBasedHttpBody body = new ParametersBasedHttpBody(parameters);
        final ReviewDiffCommentListResourceQuery queryNewComment =
                new ReviewDiffCommentListResourceQuery(body, properties, reviewId,
                        response.getReview().getId());
        connector.execute(queryNewComment);
    }

    /**
     * Checks two files - original and patched on checkstyle, finds diffs in files.
     *
     * @param reviewRequest Review request.
     * @param maxRevision Maximum revision.
     * @param file File's description.
     * @return list of checkstyle errors.
     * @throws HttpConnectorException Http connector exception.
     * @throws InterruptedException interrupted exception.
     */
    private List<CheckstyleError> checkDiff(@Nonnull ReviewRequests reviewRequest, int maxRevision,
            @Nonnull FileDiffListResourcesFile file)
            throws HttpConnectorException, InterruptedException {
        Objects.requireNonNull(reviewRequest);
        Objects.requireNonNull(file);
        final List<CheckstyleError> result = new ArrayList<>();
        final FileResponse patchedFile =
                getPatchedFile(reviewRequest.getId(), maxRevision, file.getId(), file.getStatus());
        if (!(properties.getRepositoryWithGeneratedFile()
                .indexOf(reviewRequest.getLinks().getRepository().getRepositoryTitle()) > 0 &&
                isGeneratedFile(patchedFile))) {

            DiffData diffData = getDiffData(maxRevision, file.getId());
            final List<FileDiffLine> numStrings = getDiffLines(diffData);

            final File destFile = new File(file.getDestFile());
            try {
                final File tmpFile = writeFile(patchedFile, destFile.getName());

                final Checkstyle checkstyleChecker = new Checkstyle(properties);
                final Map<Integer, List<AuditEvent>> errors =
                        checkstyleChecker.runCheckstyle(new File(tmpFile.getAbsolutePath()));
                final List<CheckstyleError> errorsInDiff = findErrorsInDiffs(numStrings, errors);

                result.addAll(errorsInDiff);
                tmpFile.delete();
            } catch (IOException | CheckstyleException e) {
                logger.warn(e);
            }
        } else {
            logger.debug("File " + file.getDestFile() + " is generated");
        }
        return result;
    }

    /**
     * Gets patched file by its coordinates.
     *
     * @param idDiff diff's id.
     * @param revision diff's revision.
     * @param idFile file's id.
     * @param status file's status.
     * @return Patched file.
     * @throws HttpConnectorException Http connector exception.
     * @throws InterruptedException interrupted exception.
     */
    private FileResponse getPatchedFile(int idDiff, int revision, int idFile,
            @Nonnull String status) throws HttpConnectorException, InterruptedException {
        Objects.requireNonNull(status);
        FileResponse response = null;
        if (!DELETED.equals(status)) {
            final PatchedFileQuery query =
                    new PatchedFileQuery(idDiff, revision, idFile, properties);
            response = connector.execute(query);
        }
        if (response == null) {
            response = new FileResponse("");
        }
        return response;
    }

    /**
     * Checks is generated file by external tools.
     *
     * @param file Check file.
     * @return is generated file.
     */
    private boolean isGeneratedFile(@Nonnull FileResponse file) {
        Objects.requireNonNull(file);
        boolean isGeneratedFile = false;
        if (file.getText().contains("@generated")) {
            isGeneratedFile = true;
        }
        return isGeneratedFile;
    }

    /**
     * Finds checkstyle errors in diffs.
     *
     * @param numStrings diffs.
     * @param errors checkstyle errors.
     * @return list of checkstyle.
     */
    private List<CheckstyleError> findErrorsInDiffs(@Nonnull List<FileDiffLine> numStrings,
            @Nonnull Map<Integer, List<AuditEvent>> errors) {
        Objects.requireNonNull(numStrings);
        Objects.requireNonNull(errors);
        final List<CheckstyleError> errorsInDiff = new ArrayList<>();
        for (FileDiffLine i : numStrings) {
            errorsInDiff.addAll(getErrorByLine(errors, i));
        }
        return errorsInDiff;
    }

    /**
     * Writes file to tmp system path.
     *
     * @param patchedFile Pached file.
     * @param filename Filename.
     * @return writed file.
     * @throws IOException IO exception.
     */
    private File writeFile(@Nonnull FileResponse patchedFile, @Nonnull String filename)
            throws IOException {
        Objects.requireNonNull(patchedFile);
        Objects.requireNonNull(filename);
        final File tmpFile = new File("/tmp/" + filename);
        final FileWriter writer = new FileWriter(tmpFile);
        writer.write(patchedFile.getText());
        writer.close();
        return tmpFile;
    }

    /**
     * Gets list of errors from errors that's has error with num line lineNum.
     *
     * @param errors errors.
     * @param lineNum num line.
     * @return list of errors.
     */
    @Nonnull
    private List<CheckstyleError> getErrorByLine(@Nonnull Map<Integer, List<AuditEvent>> errors,
            @Nonnull FileDiffLine lineNum) {
        Objects.requireNonNull(errors);
        Objects.requireNonNull(lineNum);
        final List<CheckstyleError> result = new ArrayList<>();
        if (errors.get(lineNum.getSourceLine()) != null) {
            for (AuditEvent error : errors.get(lineNum.getSourceLine())) {
                final CheckstyleError checkstyleError =
                        new CheckstyleError(lineNum.getFileDiffLine(), error.getMessage());
                result.add(checkstyleError);
            }
        }
        return result;
    }

    /**
     * Gets diff data.
     *
     * @param revision review's revision.
     * @param idFile file's id.
     * @return diff data.
     * @throws HttpConnectorException Http connector exception.
     * @throws InterruptedException Interrupted exception.
     */
    private DiffData getDiffData(int revision, int idFile)
            throws HttpConnectorException, InterruptedException {
        DiffDataQuery query = new DiffDataQuery(properties, reviewId, revision, idFile);
        DiffDataResponse response = connector.execute(query);
        return response.getDiffData();
    }

    /**
     * Gets diff lines.
     *
     * @param diffData diff data.
     * @return list of diff lines.
     */
    private List<FileDiffLine> getDiffLines(@Nonnull DiffData diffData) {
        Objects.requireNonNull(diffData);
        final List<FileDiffLine> resultLines = new ArrayList<>();
        for (Chunk chunk : diffData.getChunks()) {
            if (chunk.getChange().equals("insert") || chunk.getChange().equals("replace")) {
                resultLines.addAll(chunk.getLines());
            }
        }
        return resultLines;
    }
}
