package com.ljryh.service.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@ChannelHandler.Sharable
@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 对于每个传去的信息都要调用
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        Channel channel = ctx.channel();
//        ByteBuf buf = Unpooled.copiedBuffer("service hello world",CharsetUtil.UTF_8);
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

        log.info(msg.toString());

        // 将接受到的信息写个发送者，而不是冲刷出站消息
        ByteBuf in = (ByteBuf) msg;
        System.out.println("service received :" + in.toString(CharsetUtil.UTF_8));
        ByteBuf buf = Unpooled.copiedBuffer(in.toString(CharsetUtil.UTF_8) +":service return", CharsetUtil.UTF_8);
        in = buf;
        ctx.write(in);
    }

    /**
     * 通知ChannelInboundHandler 最后一次对channelRead()的调用是当先批量读取中的最后一条信息
     *
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 将未决消息冲刷到远程节点，并关闭channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 在读取操作期间，有异常抛出是会调用
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 打印异常栈跟踪
        cause.printStackTrace();
        // 关闭该channel
        ctx.close();
    }
}
