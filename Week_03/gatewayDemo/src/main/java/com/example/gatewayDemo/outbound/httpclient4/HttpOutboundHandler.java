package com.example.gatewayDemo.outbound.httpclient4;

import com.example.gatewayDemo.filter.HeaderHttpResponseFilter;
import com.example.gatewayDemo.filter.HttpRequestFilter;
import com.example.gatewayDemo.filter.HttpResponseFilter;
import com.example.gatewayDemo.router.HttpEndpointRouter;
import com.example.gatewayDemo.router.RandomHttpEndpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpOutboundHandler {

    private List<String> backendUrls;
    HttpEndpointRouter router = new RandomHttpEndpointRouter();
    HttpResponseFilter filter = new HeaderHttpResponseFilter();

    public HttpOutboundHandler(List<String> backends) {
        this.backendUrls = backends;
    }


    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpRequestFilter filter) {
        String backendUrl = router.route(this.backendUrls);//实现路由
        final String url = backendUrl + fullRequest.uri();
        filter.filter(fullRequest, ctx);//实现请求过滤器
        fetchGet(fullRequest, ctx, url);
    }

    private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);//整合上次作业httpclient

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    FullHttpResponse fullResponse = null;
                    if (status >= 200 && status < 300) {
                        try {
                            byte[] body = EntityUtils.toByteArray(response.getEntity());
                            fullResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
                            fullResponse.headers().set("Content-Type", "application/json");
                            fullResponse.headers().setInt("Content-Length", Integer.parseInt(response.getFirstHeader("Content-Length").getValue()));
                            fullResponse.headers().set("HeaderHttpRequestFilter", inbound.headers().get("HeaderHttpRequestFilter"));
                            filter.filter(fullResponse);//实现响应过滤器

                        } catch (Exception e) {
                            e.printStackTrace();
                            fullResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
                            e.printStackTrace();
                            ctx.close();
                        } finally {
                            if (inbound != null) {
                                if (!HttpUtil.isKeepAlive(inbound)) {
                                    ctx.write(fullResponse).addListener(ChannelFutureListener.CLOSE);
                                } else {
                                    //response.headers().set(CONNECTION, KEEP_ALIVE);
                                    ctx.write(fullResponse);
                                }
                            }
                            ctx.flush();
                        }


                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);//整合上次作业httpclient
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            httpclient.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
        }
    }
}
