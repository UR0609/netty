package com.ljryh.service.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println("Usage : " + EchoServer.class.getSimpleName() + " <port>");
//            return;
//        }
//        int port = Integer.parseInt(args[1]);
        // 调用服务器的start()方法
        new EchoServer(9999).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        // 创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建 ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    // 指定所使用的 NIO 传输 Channel
                    .channel(NioServerSocketChannel.class)
                    // 使用指定接口设置套字段地址
                    .localAddress(new InetSocketAddress(port))
                    // 添加一个 EchoServerHandler 到子 Channel 的 ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // EchoServerHandler 被标注为 @Sharable，所以我们可以总是使用同样的实例
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });
            // 异步绑定服务器调用 sync()方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind().sync();
            // 获取 Channel 的 closeFuture ，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            // 关闭 EventLoopGroup，释放所有资源
            group.shutdownGracefully().sync();
        }
    }

}
