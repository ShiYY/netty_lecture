package com.shiyang.netty.firstexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author shiyang
 * @create 2019-04-08 2:37 PM
 **/
public class TestServer {

    public static void main(String[] args) throws Exception {
        // 定义两个事件循环组 可以理解为两个死循环
        // 不断的从客户端接收连接 但不做任何处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 对连接做具体的处理
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // Netty提供的启动服务端的类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).
                    childHandler(new TestServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
