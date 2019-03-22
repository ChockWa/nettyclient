package com.chockwa.nettyclient;

import com.chockwa.nettyclient.netty.ClientHandler;
import com.chockwa.nettyclient.netty.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyclientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NettyclientApplication.class, args);
    }

    @Autowired
    NettyClient nettyClient;

    @Override
    public void run(String... args) throws Exception {
        nettyClient.run(new ClientHandler());
    }
}
