package org.stash;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;

import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaTimeServer {

    private static final int PORT = 9123;

    public static void main(String[] args) throws Exception {
        SocketAcceptor acceptor = new NioSocketAcceptor();
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

        chain.addLast("codec", new ProtocolCodecFilter(
            new TextLineCodecFactory()));
        chain.addLast("logger", new LoggingFilter());

        acceptor.setHandler(new TimeServerHandler());
        acceptor.bind(new InetSocketAddress(PORT));
        System.out.println("Time Server Started");
    }
}

