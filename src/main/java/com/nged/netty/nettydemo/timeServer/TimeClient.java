package com.nged.netty.nettydemo.timeServer;

import com.nged.netty.nettydemo.timeServer.decoder.TimeDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

    public static void main(String[] args) throws InterruptedException {
        String host ="localhost";
        int port =8080;

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE,true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
//                    socketChannel.pipeline().addLast(new TimeClientHandler());
                    socketChannel.pipeline().addLast(new TimeDecoder(),new TimeClientHandler());
                }
            });
            //start client
            ChannelFuture f = b.connect(host,port).sync();
            //wait util the connection is closed
            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
        }


    }
}
