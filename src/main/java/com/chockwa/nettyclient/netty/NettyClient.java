package com.chockwa.nettyclient.netty;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.chockwa.nettyclient.alarmInfo.*;
import com.chockwa.nettyclient.properties.NettyProperties;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/19 10:32
 * @description:
 */
@Component
public class NettyClient {


    @Autowired
    private NettyProperties nettyProperties;

    public void run(ChannelHandler... handlers) throws InterruptedException, URISyntaxException {
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

    private void sendTestData(ChannelFuture f) throws URISyntaxException {

        AlarmInfoPlateResult alarmInfoPlateResult = new AlarmInfoPlateResult();
        AlarmInfoPlate alarmInfoPlate = new AlarmInfoPlate();

        Car car = new Car();
        car.setCarColor("white");
        car.setCarType(1);
        car.setDirection(0);
        alarmInfoPlate.setCar(car);
        alarmInfoPlate.setChannel(1);

        Drive drive = new Drive();
        drive.setDriveType(0);
        drive.setRecognize(0);
        drive.setSpeed(60);
        alarmInfoPlate.setDrive(drive);

        Image image = new Image();
        image.setFile("yyy");
        image.setFileLength(50);
        image.setThumb("50");
        image.setThumbLength(3);
        alarmInfoPlate.setImage(image);


        alarmInfoPlate.setNowTime(System.currentTimeMillis());
        alarmInfoPlate.setTimestamp(System.currentTimeMillis());

        Plate plate = new Plate();
        plate.setConfidence(100);
        plate.setLicense("粤B54321");
        plate.setPlateColor(1);
        plate.setPlateType(1);
        Rect rect = new Rect();
        rect.setLeft(0);
        rect.setRight(1);
        rect.setTop(5);
        rect.setBottom(30);

        plate.setRect(rect);

        alarmInfoPlate.setPlate(plate);
        alarmInfoPlateResult.setAlarmInfoPlate(alarmInfoPlate);

        String data = JSONUtil.toJsonStr(alarmInfoPlateResult);
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
        String reg = "{\"RegisterIPC\":{\"devname\":\"test\",\"ipaddr\":\"192.168.1.100\",\"user\":\"test\",\"pass\":\"123456\",\"serialno\":\"eff50e18-e3d3862b\"}}";
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
