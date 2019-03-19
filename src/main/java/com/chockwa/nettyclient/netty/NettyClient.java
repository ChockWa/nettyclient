package com.chockwa.nettyclient.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/19 10:32
 * @description:
 */
@Component
public class NettyClient {

    public void run(String host, int port, ChannelHandler... handlers) throws InterruptedException, UnsupportedEncodingException, URISyntaxException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(handlers);
                }
            });
            ChannelFuture f = b.connect(host, port).sync(); // (5)
            URI uri = new URI("http://127.0.0.1:8899");
            String msg = "{\n" +
                    "“HeartBeat”: //心跳命令字，大小写敏感\n" +
                    "{”ipaddr“:”192.168.1.100”,//IPC的IP地址\n" +
                    "“ipc_id”:”ipc_201903170001”，//HTTP服务器给相机配置的ID\n" +
                    "”now_time“:1552817319}//当前时间，格式Unix时间戳\n" +
                    "}";
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                    uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));

            // 构建http请求
            request.headers().set(HttpHeaders.Names.HOST, host);
            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
            request.headers().set("messageType", "normal");
            request.headers().set("businessType", "testServerState");
            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }
}
