package com.nged.netty.nettydemo.timeServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 *
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
/*    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        ByteBuf m = (ByteBuf) msg;
        try{
            long currentTImeMillis = ( m.readUnsignedInt() -2208988800L)* 1000L;
            System.out.println(new Date(currentTImeMillis));
            ctx.close();
        }finally {
            m.release();
        }

    }*/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        UnixTime m = (UnixTime) msg;
        System.out.println(m);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
