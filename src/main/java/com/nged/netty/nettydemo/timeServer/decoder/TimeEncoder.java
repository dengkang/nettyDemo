package com.nged.netty.nettydemo.timeServer.decoder;

import com.nged.netty.nettydemo.timeServer.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 *
 */
public class TimeEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
         UnixTime m = (UnixTime) msg;
        ByteBuf encoded = ctx.alloc().buffer(4);
        encoded.writeInt((int) m.value());
        ctx.write(encoded,promise); //1
    }
/* 最后一行
    在这一行中有很多重要的事情。

    首先，我们按原样传递原始的ChannelPromise，以便当编码数据实际写到线路上时，Netty会将其标记为成功或失败。

    其次，我们没有调用ctx.flush（）。 有一个单独的处理程序方法void flush（ChannelHandlerContext ctx），用于覆盖flush（）操作。*/
}
