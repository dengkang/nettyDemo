package com.nged.netty.nettydemo.timeServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 它与前面的示例不同之处在于，它不包含任何请求就发送包含32位整数的消息，并在发送消息后关闭连接。
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


/*    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
        //分配byteBuf空间
        final ByteBuf time = ctx.alloc().buffer(4);
        //填充发送信息
        time.writeInt((int)(System.currentTimeMillis()/1000L + 2208988800L));
        //此处是异步操作
        final ChannelFuture f = ctx.writeAndFlush(time);
        //关闭ctx的操作必须放在write之后，所以通过监听处理
        //否者出现消息没发送ctx就关闭的情况
        f.addListener(channelFuture ->{
            assert  f ==  channelFuture;
            ctx.close();});
        //通过pre-defined的方式关闭ctx
//        f.addListener(ChannelFutureListener.CLOSE);

    }*/

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ChannelFuture f = ctx.writeAndFlush(new UnixTime());
        f.addListener(ChannelFutureListener.CLOSE);
    }
}
