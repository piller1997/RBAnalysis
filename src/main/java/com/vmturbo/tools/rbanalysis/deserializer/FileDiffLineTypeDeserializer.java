package com.vmturbo.tools.rbanalysis.deserializer;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.apache.commons.lang3.StringUtils;

import com.vmturbo.tools.rbanalysis.responce.FileDiffLine;

/**
 * Gson deserializer capable of reading file-diff line resources. It only stores file-diff line
 * number and patched file number. All the other information is ignored.
 */
public class FileDiffLineTypeDeserializer implements JsonDeserializer<FileDiffLine> {

    @Override
    public FileDiffLine deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        try {
            final JsonElement fileDiffLineElement = ((JsonArray)json).get(0);
            final int fileDiffLine;
            if (StringUtils.isBlank(fileDiffLineElement.getAsString())) {
                fileDiffLine = -1;
            } else {
                fileDiffLine = fileDiffLineElement.getAsInt();
            }
            final JsonElement patchedLineElement = ((JsonArray)json).get(4);
            final int patchedLine;
            if (StringUtils.isBlank(patchedLineElement.getAsString())) {
                patchedLine = -1;
            } else {
                patchedLine = patchedLineElement.getAsInt();
            }
            return new FileDiffLine(patchedLine, fileDiffLine);
        } catch (IllegalStateException e) {
            throw new JsonParseException("Illegal JSON format.");
        }
    }
}
