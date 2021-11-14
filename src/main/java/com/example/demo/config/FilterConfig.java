package com.example.demo.config;

import com.example.demo.filter.LogApiFilter;
import com.example.demo.filter.LogProcessTimeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

//    Use annotation to config this component.
//    @Bean
//    public FilterRegistrationBean logProcessTimeFilter() {
//        FilterRegistrationBean<LogProcessTimeFilter> bean = new FilterRegistrationBean<>();
//        bean.setFilter(new LogProcessTimeFilter());
//        bean.setName("logProcessTimeFilter");
//        bean.addUrlPatterns("/*");
//        bean.setOrder(1);
//        return bean;
//    }

    @Bean
    public FilterRegistrationBean logApiFilter() {
        FilterRegistrationBean<LogApiFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LogApiFilter());
        bean.setName("logApiFilter");
        bean.addUrlPatterns("/*");
        bean.setOrder(0);
        return bean;
    }
}
