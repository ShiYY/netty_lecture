package com.shiyang.netty.thirdexample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author shiyang
 * @create 2019-04-16 8:38 PM
 **/
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    // GlobalEventExecutor 单线程 单例 的EventExecutor实例 伸缩性不强 无法调度大量任务
    // 缓存与服务器建立过连接的客户端Channel实例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 服务器端收到任意客户端消息就会调用
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush(channel.remoteAddress() + ", 发送的消息: " + s + "\n");
            } else {
                ch.writeAndFlush("[自己] " + s + "\n");
            }
        });
    }

    // 加入连接
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 全部客户端广播 遍历channelGroup中所有的客户端channel
        channelGroup.writeAndFlush("[服务器] - " + channel.remoteAddress() + " 加入\n");

        channelGroup.add(channel);
    }

    // 断开连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
//        channelGroup.remove(channel); // 无需手写 连接断掉后netty会自动将客户端的channel从channelGroup中移除

        channelGroup.writeAndFlush("[服务器] - " + channel.remoteAddress() + " 离开\n");

        // channelGroup中缓存的channelGroup数量
        System.out.println("channelGroup: " + channelGroup.size());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 下线了");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
