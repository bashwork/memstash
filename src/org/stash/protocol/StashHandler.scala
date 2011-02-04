package org.stash.protocol

import java.util.Date
import org.apache.mina.core.service.IoHandlerAdapter
import org.apache.mina.core.session.IoSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.stash.lang.StashSystem

/**
 * @summary
 */
class StashHandler extends IoHandlerAdapter {

    private final val Logger logger =
        LoggerFactory.getLogger(TimeServerHandler.class);

    private val statistic = new StashStatistics()

    @throws(classOf[Exception])
    override def exceptionCaught(session:IoSession, cause:Throwablea =) {
        logger.error(cause.toString());
        session.close(true);
    }

    @throws(classOf[Exception])
    override def messageReceived(session:IoSession, message:Object) = {
        val request = message.asInstanceOf[StashRequest]
        val response = StashResponse()

        request.command match {
            case "FLUSH_ALL"   => command_flush(response)
            case "ERROR"       => response.write("ERROR")
            case "VERSION"     => command_version(response)
            case "SET"|"ADD"|"REPLACE"|"APPEND"|"PREPEND" => session.close(true)
            case "GET"|"GETS"  => command_get(request, response)
            case "INCR"|"DECR" => command_increment(request, response)
            case "DELETE"      => command_delete(request, response)
            case "STATS"       => command_statistic(response)
            case "QUIT"        => session.close(true)
        }

        session.write(response));
    }

    @throws(classOf[Exception])
    override def sessionClosed(session:IoSession) = {
        logger.info("Session Closed", session.getRemoteAddress);
        statistic.decrement("curr_connections", 1)
    }

    @throws(classOf[Exception])
    override def sessionOpened(session:IoSession) = {
        logger.info("Session Opened", session.getRemoteAddress);
        statistic.increment("curr_connections", 1)
        statistic.increment("total_connections", 1)
    }

    private def command_version(response: StashResponse) = {
        response.write("VERSION " + StashSystem.Version)
    }

    private def command_flush(response: StashResponse) = {
        storage.clear()
        statistic.increment("cmd_flush", 1)
        response.write("OK")
    }

    private def command_get(request:StashRequest, response: StashResponse) = {
        statistic.increment("cmd_get", 1)
        storage.get(request.key) match {
            case el:StashObject => {
                statistic.increment("get_hits", 1)
                statistic.increment("bytes_read", el.length)
                storage.remove(request.key)
                response.write("DELETED")
            }
            case null => {
                statistic.increment("get_misses", 1)
                response.write("NOT_FOUND")
            }
        }
    }

    private def command_set(request:StashRequest, response: StashResponse) = {
        statistic.increment("cmd_set", 1)
        storage.get(request.key) match {
            case el:StashObject => {
                statistic.increment("set_hits", 1)
                statistic.increment("bytes_written", el.length)
                storage.remove(request.key)
                response.write("DELETED")
            }
            case null => {
                statistic.increment("set_misses", 1)
                response.write("NOT_FOUND")
            }
        }
    }

    /* todo support noreply */
    private def command_delete(request:StashRequest, response: StashResponse) = {
        storage.get(request.key) match {
            case el:StashObject => {
                statistic.increment("delete_hits", 1)
                storage.remove(request.key)
                response.write("DELETED")
            }
            case null => {
                statistic.increment("delete_misses", 1)
                response.write("NOT_FOUND")
            }
        }
    }

    private def command_increment(request:StashRequest, response: StashResponse) = {
        statistic.increment("cmd_set", 1)
        storage.get(request.key) match {
            case el:StashObject => {
                response.write("OK")
            }
            case null => response.write("NOT_FOUND")
        }
    }

    private def command_statistic(response: StashResponse) = {
        response.write("END")
    }
}
