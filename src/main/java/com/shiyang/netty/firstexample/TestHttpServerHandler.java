package com.shiyang.netty.firstexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author shiyang
 * @create 2019-04-08 5:28 PM
 **/
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    // 读取客户端发过来的请求 并向客户度返回响应
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if (httpObject instanceof  HttpRequest) {
            System.out.println("channelRead0...");

            HttpRequest httpRequest = (HttpRequest) httpObject;
            System.out.println("请求方法名: " + httpRequest.method().name());
            URI uri = new URI(httpRequest.uri());
            // 请求网页图标
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求favicon.ico");
                return;
            }

            ByteBuf content = Unpooled.copiedBuffer("Hello World\n", CharsetUtil.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            channelHandlerContext.writeAndFlush(response);
        }
    }

    /*
        --curl请求--
        initChannel...
        handler Added // 新的通道添加
        channel Registered // 连接注册
        channel Active // 连接处于活动状态
        channelRead0...
        请求方法名: GET
        channel Inactive // 连接变为不活动状态
        channel Unregistered // 连接注销

        --浏览器请求--
        initChannel...
        handler Added
        channel Registered
        channel Active
        channelRead0...
        请求方法名: GET
        channel Inactive
        channel Unregistered
        channelRead0...
        请求方法名: GET
        请求favicon.ico

        netty本身并不是遵循servlet规范来进行的 20
    */


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Registered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Unregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Active");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Inactive");
        super.channelInactive(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler Added");
        super.handlerAdded(ctx);
    }
}
