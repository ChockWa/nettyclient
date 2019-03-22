package com.chockwa.nettyclient.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.time.LocalDateTime;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/19 10:36
 * @description:
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    HttpResponse response;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                System.out.println("开始发送心跳:" + LocalDateTime.now());

                URI uri = new URI("http://193.112.46.15:18899");
                String msg = "{\"HeartBeat\":{\"ipaddr\":\"193.112.46.15\",\"ipc_id\":\"ipc_201903170001\",\"now_time\":" +
                        System.currentTimeMillis() + "}}";
                DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                        uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes(CharsetUtil.UTF_8)));

                // 构建http请求
                request.headers().set(HttpHeaderNames.HOST, uri.getHost());
                request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
                request.headers().set("messageType", "normal");
                request.headers().set("businessType", "testServerState");
                ctx.writeAndFlush(request);

            }
        }
        super.userEventTriggered(ctx, evt);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpResponse) {
            response = (HttpResponse) msg;
            System.out.println("header:" + response.headers().toString());
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            String responseStr = buf.toString(CharsetUtil.UTF_8);
            if (responseStr.contains("HeartBeatHandler")) {
                System.out.println("收到心跳，" + responseStr);
            } else if (responseStr.contains("RegisterIPC_Ret")) {
                System.out.println("收到注册回复：" + responseStr);
            } else {
                System.out.println(responseStr);
            }
            buf.release();
        }
        System.out.println(info() + "client-channelRead");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(info() + "client-channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(info() + "client-channelUnregistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(info() + "client-channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(info() + "client-channelInactive");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println(info() + "client-channelReadComplete");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println(info() + "client-channelWritabilityChanged");
    }

    protected String info() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis() + " " + this.getClass().getSimpleName() + ": ";
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(info() + "client-exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }
}
