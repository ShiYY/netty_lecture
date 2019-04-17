package com.shiyang.netty.fourthexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author shiyang
 * @create 2019-04-18 12:14 AM
 **/
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 责任链模式 一个一个Handler进行处理
        // 在设定的时间内如果服务器没有做相应的读/写/读写 就会触发相应的事件
        pipeline.addLast(new IdleStateHandler(7, 7, 3, TimeUnit.SECONDS)); // netty提供的针对空闲状态检测Handler
        pipeline.addLast(new MyServerHandler());
    }
}
