package com.ljryh.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    /**
     * 在到服务器的连接已经建立之后将被调用
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        Channel channel = ctx.channel();
//        ByteBuf buf = Unpooled.copiedBuffer("client hello world",CharsetUtil.UTF_8);
//        ChannelFuture cf =channel.writeAndFlush(buf);
//        cf.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                if(channelFuture.isSuccess()){
//                    System.out.println("write success");
//                } else {
//                    System.out.println("write error");
//                    channelFuture.cause().printStackTrace();
//                }
//            }
//        });
        // 当被通知 Channel 是活跃的时候，发送一条信息
        ctx.writeAndFlush(Unpooled.copiedBuffer("client send data", CharsetUtil.UTF_8));
    }

    /**
     * 当服务器接收到一条消息时被调用
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        //记录已经收消息的转储
        System.out.println("Client received:" + byteBuf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 在处理过程中引发异常时被调用
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 在发生异常时，记录错误并关闭channel
        cause.printStackTrace();
        ctx.close();
    }
}
