package org.dynasite.page;

import fi.iki.elonen.NanoHTTPD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Map;
import java.util.Objects;

public class FilePage extends Page {

    private static final Logger LOG = LogManager.getLogger();

    private final File file;

    public FilePage(File file) {
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public NanoHTTPD.Response getPageResponse(Map<String, String> headers, NanoHTTPD.IHTTPSession session) {
        if(!file.exists()) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_HTML,
                    "<h1>File Not Found!</h1>" + "<p>" + file.getAbsolutePath()
            );
        }

        try {
            FileInputStream inputStream = new FileInputStream(file);

            String mimeType = NanoHTTPD.getMimeTypeForFile(file.toURI().toString());

            LOG.info("Serving page " + file.getAbsolutePath() + " with mime: " + mimeType);

            return NanoHTTPD.newFixedLengthResponse(
                    NanoHTTPD.Response.Status.OK, mimeType,
                    inputStream, file.length()
            );

        } catch (IOException e) {
            return new ErrorPage(e).getPageResponse(headers, session);
        }
    }
}
