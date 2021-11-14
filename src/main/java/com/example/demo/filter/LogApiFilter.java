package com.example.demo.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogApiFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(requestWrapper, responseWrapper);

        logApi(request, response);
        logBody(requestWrapper, responseWrapper);
        responseWrapper.copyBodyToResponse();
    }

    private void logApi(HttpServletRequest request, HttpServletResponse response) {
        int status = response.getStatus();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String param = request.getQueryString();

        if (param != null) {
            uri = uri + "?" + param;
        }

        System.out.println(String.join(" ", Integer.toString(status), method, uri));

    }
    private void logBody(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) {
        String requestBody = getContent(requestWrapper.getContentAsByteArray());
        System.out.println("Request: " + requestBody);
        String responseBody = getContent(responseWrapper.getContentAsByteArray());
        System.out.println("Response: " + responseBody);
    }

    private String getContent(byte[] content) {
        String body = new String(content);
        return body.replaceAll("[\n\t ]", "");
    }
}
