package com.shiyang.netty.secondexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author shiyang
 * @create 2019-04-16 12:39 AM
 **/
public class MyServer {

    public static void main(String[] args) throws  Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        /**
         * ServerBootstrap childHandler() handler()的区别
         * 客户端一般不使用childHandler()
         * 服务端使用childHandler() handler()的区别
         * handler() 针对的是bossGroup组发挥作用的
         * childHandler() 针对的是workerGroup组发挥作用的
         */

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup).channel(NioServerSocketChannel.class).childHandler(new MyServerInitializer());

        try {
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
