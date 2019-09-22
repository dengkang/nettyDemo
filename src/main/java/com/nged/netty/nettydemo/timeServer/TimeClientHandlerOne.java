package com.nged.netty.nettydemo.timeServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * 解决 流数据碎片化的方式一 按消息的固定长度来解析消息
 */
public class TimeClientHandlerOne extends ChannelInboundHandlerAdapter {
    private ByteBuf buf;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      ByteBuf m = (ByteBuf) msg;
      buf.writeBytes(m);
      m.release();
      if(buf.readableBytes()>=4){
          long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
          System.out.println(new Date(currentTimeMillis));
          ctx.close();
      }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
            cause.printStackTrace();
            ctx.close();
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
       buf = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
         buf.release();
         buf = null;
    }
}
