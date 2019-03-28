package com.woobux.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;


/**
 * @Auther: wangboxue
 * @Date: 2018/12/24 10:26
 * @Description:
 */
public class HttpServer extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        //uri
        String uri = msg.uri();
        System.out.println("uri is :"+uri);

        //
        HttpMethod method = msg.method();
        System.out.println("method is :"+method.name());

        //
        HttpMessage = msg.content();


    }
}
