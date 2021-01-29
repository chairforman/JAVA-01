package com.example.gatewayDemo.filter;

import io.netty.handler.codec.http.FullHttpResponse;

public class HeaderHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("HeaderHttpResponseFilter", "from HeaderHttpResponseFilter");//实现响应过滤器
    }
}
