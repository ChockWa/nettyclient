package com.chockwa.nettyclient.netty;

import com.chockwa.nettyclient.properties.NettyProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/19 10:32
 * @description:
 */
@Component
public class NettyClient {


    @Autowired
    private NettyProperties nettyProperties;

    public void run(ChannelHandler... handlers) throws InterruptedException, URISyntaxException, IOException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(new IdleStateHandler(0, 4, 0))
                            .addLast(new HttpResponseDecoder())
                            .addLast(new HttpRequestEncoder())
                            .addLast(handlers);
                }
            });
            ChannelFuture f = b.connect(nettyProperties.getHost(), nettyProperties.getPort()).sync(); // (5)
            sendRegister(f);
//            sendTestData(f);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private void sendTestData(ChannelFuture f) throws URISyntaxException, IOException {

        final Resource resource = new ClassPathResource("alarminfotest.json");

        String data = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());

        URI uri = new URI("http://" + nettyProperties.getHost() + ":" + nettyProperties.getPort());
//            String msg = "{\"HeartBeat\":{\"ipaddr\":\"192.168.1.100\",\"ipc_id\":\"ipc_201903170001\",\"now_time\":" + System.currentTimeMillis() + "}}";
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                uri.toASCIIString(), Unpooled.wrappedBuffer(data.getBytes(CharsetUtil.UTF_8)));

        // 构建http请求
        request.headers().set(HttpHeaderNames.HOST, nettyProperties.getHost());
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        request.headers().set("messageType", "normal");
        request.headers().set("businessType", "testServerState");
        f.channel().writeAndFlush(request);
    }

    private void sendRegister(ChannelFuture f) throws URISyntaxException {
        URI uri = new URI("http://" + nettyProperties.getHost() + ":" + nettyProperties.getPort());
//            String msg = "{\"HeartBeat\":{\"ipaddr\":\"192.168.1.100\",\"ipc_id\":\"ipc_201903170001\",\"now_time\":" + System.currentTimeMillis() + "}}";
        String reg = "{\"RegisterIPC\":{\"devname\":\"测试设备001\",\"ipaddr\":\"192.168.1.100\",\"user\":\"test\",\"pass\":\"123456\",\"serialno\":\"testdevice\"}}";
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                uri.toASCIIString(), Unpooled.wrappedBuffer(reg.getBytes(CharsetUtil.UTF_8)));

        // 构建http请求
        request.headers().set(HttpHeaderNames.HOST, nettyProperties.getHost());
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        request.headers().set("messageType", "normal");
        request.headers().set("businessType", "testServerState");
        f.channel().writeAndFlush(request);
    }
}
