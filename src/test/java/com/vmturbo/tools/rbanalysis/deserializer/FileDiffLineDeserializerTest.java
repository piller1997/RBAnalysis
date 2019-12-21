package com.vmturbo.tools.rbanalysis.deserializer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.vmturbo.tools.rbanalysis.responce.Chunk;
import com.vmturbo.tools.rbanalysis.responce.DiffDataResponse;
import com.vmturbo.tools.rbanalysis.responce.FileDiffLine;

/**
 * Unit test fot {@link FileDiffLineTypeDeserializer}.
 */
public class FileDiffLineDeserializerTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Tests parsing of a single file-diff resource with information about update. We do not care a
     * log of all the data inside except of filediff line number and right-hand-side (patched file)
     * line number.
     *
     * @throws Exception on exceptions occurred.
     */
    @Test
    public void testParsingUpdate() throws Exception {
        final Gson gson = new GsonBuilder().registerTypeAdapter(FileDiffLine.class,
                new FileDiffLineTypeDeserializer()).create();
        try (InputStream is = getClass().getResourceAsStream("/FileDiffLineUpdate.json")) {
            final FileDiffLine response =
                    gson.fromJson(new InputStreamReader(is), FileDiffLine.class);
            Assert.assertEquals(42, response.getFileDiffLine());
            Assert.assertEquals(37, response.getSourceLine());
        }
    }

    /**
     * Tests parsing of a single file-diff resource with illegal information about update. We do
     * not care a log of all the data inside except of filediff line number and right-hand-side
     * (patched file) line number.
     *
     * @throws Exception on exceptions occurred.
     */
    @Test
    public void testParsingIllegalUpdate() throws Exception {
        final Gson gson = new GsonBuilder().registerTypeAdapter(FileDiffLine.class,
                new FileDiffLineTypeDeserializer()).create();
        try (InputStream is = getClass().getResourceAsStream("/FileDiffLineIllegalUpdate.json")) {
            exception.expect(JsonParseException.class);
            exception.expectMessage("Illegal JSON format.");
            gson.fromJson(new InputStreamReader(is), FileDiffLine.class);
        }
    }

    /**
     * Tests parsing of a single file-diff resource with information about insert. We do not care a
     * log of all the data inside except of filediff line number and right-hand-side (patched file)
     * line number.
     *
     * @throws Exception on exceptions occurred.
     */
    @Test
    public void testParsingInsert() throws Exception {
        final Gson gson = new GsonBuilder().registerTypeAdapter(FileDiffLine.class,
                new FileDiffLineTypeDeserializer()).create();
        try (InputStream is = getClass().getResourceAsStream("/FileDiffLineInsert.json")) {
            final FileDiffLine response =
                    gson.fromJson(new InputStreamReader(is), FileDiffLine.class);
            Assert.assertEquals(60, response.getFileDiffLine());
            Assert.assertEquals(31, response.getSourceLine());
        }
    }

    /**
     * Tests parsing of a whole file-diff resource with information about all changes. We do not
     * care a log of all the data inside except of filediff line number and right-hand-side (patched
     * file) line number.
     *
     * @throws Exception on exceptions occurred.
     */
    @Test
    public void testParsingWholeFile() throws Exception {
        final Gson gson = new GsonBuilder().registerTypeAdapter(FileDiffLine.class,
                new FileDiffLineTypeDeserializer()).create();
        try (InputStream is = getClass().getResourceAsStream("/FileDiffWhole.json")) {
            final DiffDataResponse response =
                    gson.fromJson(new InputStreamReader(is), DiffDataResponse.class);
            Assert.assertEquals(2, response.getDiffData().getChangedChunkIndexes().size());
            Iterator<Chunk> iteratorChunks = response.getDiffData().getChunks().iterator();
            FileDiffLine fileDiffLine = getFileDiffLine(iteratorChunks);
            Assert.assertEquals(51, fileDiffLine.getSourceLine());
            Assert.assertEquals(51, fileDiffLine.getFileDiffLine());

            fileDiffLine = getFileDiffLine(iteratorChunks);
            Assert.assertEquals(42, fileDiffLine.getSourceLine());
            Assert.assertEquals(59, fileDiffLine.getFileDiffLine());

            fileDiffLine = getFileDiffLine(iteratorChunks);
            Assert.assertEquals(168, fileDiffLine.getSourceLine());
            Assert.assertEquals(186, fileDiffLine.getFileDiffLine());
        }
    }

    /**
     * Gets first line from iterator.
     *
     * @param iterator Iterator from response's collection.
     * @return line.
     */
    FileDiffLine getFileDiffLine(Iterator<Chunk> iterator) {
        return iterator.next().getLines().iterator().next();
    }
}
