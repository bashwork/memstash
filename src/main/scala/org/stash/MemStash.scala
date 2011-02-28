package org.stash

import java.net.InetSocketAddress
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.codec.textline.TextLineCodecFactory
import org.apache.mina.filter.logging.LoggingFilter
import org.apache.mina.transport.socket.SocketAcceptor
import org.apache.mina.transport.socket.nio.NioSocketAcceptor
import org.slf4j.{Logger, LoggerFactory}
//import org.apache.log4j.xml.DOMConfigurator

import org.stash.storage.StashStorage
import org.stash.protocol.{StashCodecFactory, StashHandler}


/**
 * The main mina protocol manager
 *
 * @param options A map of the options to configure the server with
 * @param storage The backend storage selection to use
 */
class MemStash(val options:Map[String,Any], val storage:StashStorage) {

    private val acceptor = new NioSocketAcceptor()
    private val logger = LoggerFactory.getLogger(this.getClass)

    //DOMConfigurator.configure("log4j.xml")

    /**
     * Start the memstash application and block forever
     */
    def startBlocking : Unit = {
        start
        logger.info("Blocking main thread")
        do { Thread.sleep(3000) } while(true)
    }

    /**
     * Start the memstash application
     */
    def start : Unit = {

        implicit def _atos(a:Any) = a.asInstanceOf[String]
        implicit def _atoi(a:Any) = a.toString.toInt

        //val executors = Executors.newCachedThreadPool()
        //acceptor = new NioSocketAcceptor(executor, new NioProcessor(executor))
        acceptor.setBacklog(1000)

        val chain = acceptor.getFilterChain()

        if (options("logging") == "debug") chain.addLast("logger", new LoggingFilter())
        chain.addLast("codec", new ProtocolCodecFilter(new StashCodecFactory(false)))

        val config = acceptor.getSessionConfig()
        config.setSendBufferSize(1024000)
        config.setReceiveBufferSize(1024000)
        config.setReadBufferSize(2048)
        config.setReuseAddress(true)
        config.setTcpNoDelay(true)
        config.setIdleTime(IdleStatus.BOTH_IDLE, 30)

        val sockaddress = if (options("address") == null || options("address").isEmpty)
            new InetSocketAddress(options("port")) else
            new InetSocketAddress(options("address"), options("port"))

        acceptor.setHandler(new StashHandler(storage))
        acceptor.bind(sockaddress)
        logger.info("Starting memstash on {}", sockaddress)
    }

    /**
     * Stop the memstash application
     */
    def stop : Unit = acceptor.unbind
}

