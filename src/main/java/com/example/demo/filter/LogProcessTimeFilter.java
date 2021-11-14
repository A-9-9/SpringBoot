package com.example.demo.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*", filterName = "logProcessTimeFilter")
public class LogProcessTimeFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Long startTime = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        Long processTime = System.currentTimeMillis() - startTime;
        System.out.println(processTime + "ms");
    }
}
