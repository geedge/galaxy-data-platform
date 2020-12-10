package com.mesalab.hos.config;

import io.micrometer.core.instrument.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LogChartMetricsFilter implements Filter {


    private MeterRegistry registry;
    Map<String, Double> dashboardMap = new HashMap<String, Double>();



    public LogChartMetricsFilter(MeterRegistry registry) {
        this.registry = registry;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        filterChain.doFilter(servletRequest, servletResponse);
        if (req.getRequestURI().contains("/prometheus")) {
            registryDashboardInfo();
        }
    }

    @Override
    public void destroy() {

    }





    /**
     * 更新info指标
     */
    private void registryDashboardInfo() {
        //去除容器中值防止影响
        Double lc =HosProperties.smallFileCount;
        Double mc =HosProperties.middleFileCount;
        Double bc=HosProperties.bigFileCount;
        Double ec=HosProperties.errorCount;
        Double sc=HosProperties.successCount;

        Double sp =HosProperties.sumPacket;
        HosProperties.smallFileCount=0.0;
        HosProperties.middleFileCount=0.0;
        HosProperties.bigFileCount=0.0;
        HosProperties.errorCount=0.0;
        HosProperties.successCount=0.0;
        HosProperties.sumPacket=0.0;
        dashboardMap.put("smallFileCount", lc);
        dashboardMap.put("middleFileCount",mc);
        dashboardMap.put("bigFileCount", bc);
        dashboardMap.put("errorCount", ec);
        dashboardMap.put("successCount", sc);
        dashboardMap.put("sumPacket", sp);
        registryMetrics(dashboardMap);

    }



    private void registryMetrics(Map<String, Double> map) {
        if (!ObjectUtils.isEmpty(map)) {
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                //去除容器中值防止影响


                Gauge.builder("dashInfo", map, x -> x.get(entry.getKey()))
                        .tags("id", entry.getKey(), "name", entry.getKey(), "severity", entry.getKey())
                        .description("info")
                        .register(registry);
            }
        }

    }

}