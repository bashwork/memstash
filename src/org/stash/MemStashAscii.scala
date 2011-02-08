package org.stash;

import java.net.InetSocketAddress;
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import org.stash.storage.StashStorage
import org.stash.protocol.{StashCodecFactory, StashHandler}


/**
 * @summary
 */
class MemStashAscii(val address:String, val port:Int, val threads:Int, val storage:StashStorage) {

    private val acceptor = new NioSocketAcceptor()

    def startBlocking = {
        start
        do { Thread.sleep(3000) } while(true)
    }

    def start = {
        acceptor.setBacklog(1000)

        val chain = acceptor.getFilterChain()

        chain.addLast("logger", new LoggingFilter());
        chain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));

        val config = acceptor.getSessionConfig()
        config.setSendBufferSize(1024000);
        config.setReceiveBufferSize(1024000);
        config.setReadBufferSize(2048);
        config.setReuseAddress(true);
        config.setTcpNoDelay(true)
        config.setIdleTime(IdleStatus.BOTH_IDLE, 30)

        acceptor.setHandler(new StashHandler(storage));
        acceptor.bind(new InetSocketAddress(address, port));
    }

    def stop = acceptor.unbind
}

