package com.example.ecommerce.common.configurations.logging.wrapper;

import lombok.extern.slf4j.Slf4j;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class LoggingHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final LoggingServletOutpuStream loggingServletOutpuStream = new LoggingServletOutpuStream();

    private final HttpServletResponse delegate;

    public LoggingHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        delegate = response;
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return loggingServletOutpuStream;
    }

    @Override
    public PrintWriter getWriter() {
        return new PrintWriter(loggingServletOutpuStream.baos);
    }

    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>(0);
        for (String headerName : getHeaderNames()) {
            headers.put(headerName, getHeader(headerName));
        }
        return headers;
    }

    public String getContent() {
        try {
            String responseEncoding = delegate.getCharacterEncoding();
            return loggingServletOutpuStream.baos.toString(responseEncoding != null ? responseEncoding : UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            log.warn("UNSUPPORTED ENCODING", e);
            return "[UNSUPPORTED ENCODING]";
        }
    }

    public byte[] getContentAsBytes() {
        return loggingServletOutpuStream.baos.toByteArray();
    }

    private class LoggingServletOutpuStream extends ServletOutputStream {

        private ByteArrayOutputStream baos = new ByteArrayOutputStream();

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
        }

        @Override
        public void write(int b) {
            baos.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            baos.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) {
            baos.write(b, off, len);
        }
    }
}
