package org.stash;

import java.net.InetSocketAddress;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * @summary Top level information about the protocal
 */
object MemStash { final val version = "0.1" }

/**
 * @summary
 */
class MemStash(val address:String, val port:Int, val threads:Int, val storage:CacheStorage) {

    private final val acceptor:SocketAcceptor

    def startBlocking = {
        start
        do { Thread.sleep(3000) } while(true)
    }

    def start = {
        acceptor = new NioSocketAcceptor()
        acceptor.setBackLog(1000)

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

        acceptor.setHandler(new StashHandler(storage, this));
        acceptor.bind(new InetSocketAddress(address, port));
    }

    def stop = acceptor.unbind
}

