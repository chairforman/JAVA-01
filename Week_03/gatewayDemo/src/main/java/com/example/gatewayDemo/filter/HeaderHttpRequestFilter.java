package com.example.gatewayDemo.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class HeaderHttpRequestFilter implements HttpRequestFilter {
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().set("HeaderHttpRequestFilter", "from HeaderHttpRequestFilter");//实现请求过滤器
    }
}
