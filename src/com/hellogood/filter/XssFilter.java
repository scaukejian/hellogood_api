package com.hellogood.filter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * xss跨域脚本攻击过滤器
 * Created by kejian on 2017/9/19.
 */
public class XssFilter implements Filter{

    Logger logger = LoggerFactory.getLogger(XssFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("xssFilter init......");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String url = req.getServletPath();
        logger.info("xss filter URL：" + url);
        //排除XSS过滤  富文本
        /*if(StringUtils.contains(url, "contentEdit/update")){
            chain.doFilter(req, resp);
        }else {*/
            chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
        /*}*/
    }

    @Override
    public void destroy() {
        logger.info("xssFilter destroy......");
    }
}
