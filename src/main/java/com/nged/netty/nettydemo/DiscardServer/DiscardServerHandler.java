package com.nged.netty.nettydemo.DiscardServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        //直接释放消息不处理
       ((ByteBuf)msg).release();

        //打印收到的消息
      /*  ByteBuf in = (ByteBuf)msg;
         try{
             while(in.isReadable()){
                 System.out.println((char)in.readByte());
                 System.out.flush();
             }
         }finally {
             ReferenceCountUtil.release(msg);
         }*/


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }

}
