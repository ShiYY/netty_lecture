package com.shiyang.netty.secondexample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import sun.applet.resources.MsgAppletViewer;

import java.time.LocalDateTime;

/**
 * @author shiyang
 * @create 2019-04-16 7:37 PM
 **/
public class MyClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        // 服务器端向客户端发送请求调用
        System.out.println(channelHandlerContext.channel().remoteAddress());
        System.out.println("client outPut: " + s);
        channelHandlerContext.writeAndFlush("from client: " + LocalDateTime.now());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("客户端连接成功 向服务器发送数据");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
